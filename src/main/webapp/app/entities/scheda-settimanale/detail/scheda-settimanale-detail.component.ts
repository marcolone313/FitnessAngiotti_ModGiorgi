import { Component, OnInit, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, finalize } from 'rxjs';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISchedaSettimanale } from '../scheda-settimanale.model';
import { IAllenamentoGiornaliero } from 'app/entities/allenamento-giornaliero/allenamento-giornaliero.model';
import { ICorsa } from 'app/entities/corsa/corsa.model';
import { IGym } from 'app/entities/gym/gym.model';
import { ICircuito } from 'app/entities/circuito/circuito.model';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

// Definire i tipi di tab possibili
type TabType = 'allenamenti' | 'corsa' | 'gym' | 'circuito';

// Definire i tipi di allenamento
type TipoAllenamento = 'CORSA' | 'GYM' | 'CIRCUITO';

@Component({
  standalone: true,
  selector: 'jhi-scheda-settimanale-detail',
  templateUrl: './scheda-settimanale-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SchedaSettimanaleDetailComponent implements OnInit {
  schedaSettimanale = input<ISchedaSettimanale | null>(null);
  allenamenti: IAllenamentoGiornaliero[] = [];
  corsa: ICorsa[] = [];
  gym: IGym[] = [];
  circuiti: ICircuito[] = [];

  isLoadingAllenamenti = false;
  isLoadingCorsa = false;
  isLoadingGym = false;
  isLoadingCircuiti = false;

  activeTab: TabType = 'allenamenti'; // Default tab

  // Tenere traccia di quali tab sono state caricate
  private loadedTabs: Record<TabType, boolean> = {
    allenamenti: false,
    corsa: false,
    gym: false,
    circuito: false,
  };

  // Cache degli ID degli allenamenti per tipo
  private allenamentoIdsByType: Record<TipoAllenamento, number[]> = {
    CORSA: [],
    GYM: [],
    CIRCUITO: [],
  };

  private http = inject(HttpClient);
  private applicationConfigService = inject(ApplicationConfigService);

  ngOnInit(): void {
    // Prima caricata, apriamo la tab allenamenti
    this.setActiveTab('allenamenti');
  }

  setActiveTab(tab: TabType): void {
    this.activeTab = tab;

    // Carica i dati se non sono ancora stati caricati
    if (!this.loadedTabs[tab]) {
      this.loadTabData(tab);
    }
  }

  loadTabData(tab: TabType): void {
    const schedaId = this.schedaSettimanale()?.id;
    if (!schedaId) return;

    switch (tab) {
      case 'allenamenti':
        this.loadAllenamenti(schedaId);
        break;
      case 'corsa':
        // Se abbiamo già caricato gli allenamenti, possiamo usare la cache
        if (this.loadedTabs.allenamenti) {
          this.loadCorsa();
        } else {
          // Altrimenti, carichiamo prima gli allenamenti e poi la corsa
          this.loadAllenamenti(schedaId, () => this.loadCorsa());
        }
        break;
      case 'gym':
        if (this.loadedTabs.allenamenti) {
          this.loadGym();
        } else {
          this.loadAllenamenti(schedaId, () => this.loadGym());
        }
        break;
      case 'circuito':
        if (this.loadedTabs.allenamenti) {
          this.loadCircuiti();
        } else {
          this.loadAllenamenti(schedaId, () => this.loadCircuiti());
        }
        break;
    }
  }

  loadAllenamenti(schedaId: number, callback?: () => void): void {
    if (this.isLoadingAllenamenti) return;

    this.isLoadingAllenamenti = true;
    // Aumentiamo il numero di elementi per pagina per assicurarci di caricare tutti
    const params = new HttpParams().set('schedaSettimanaleId.equals', schedaId.toString()).set('size', '100');

    const apiUrl = this.applicationConfigService.getEndpointFor('api/allenamento-giornalieros');

    this.http
      .get<IAllenamentoGiornaliero[]>(apiUrl, { params })
      .pipe(
        finalize(() => {
          this.isLoadingAllenamenti = false;
          this.loadedTabs.allenamenti = true;
        }),
      )
      .subscribe({
        next: data => {
          this.allenamenti = data;

          // Organizza gli ID degli allenamenti per tipo per le future query
          this.categorizeAllenamenti();

          // Esegui il callback se fornito (per caricare i dati correlati)
          if (callback) {
            callback();
          }
        },
        error: error => {
          console.error('Error loading allenamenti:', error);
        },
      });
  }

  // Categorizza gli allenamenti per tipo e memorizza gli ID per query future
  private categorizeAllenamenti(): void {
    this.allenamentoIdsByType = {
      CORSA: [],
      GYM: [],
      CIRCUITO: [],
    };

    this.allenamenti.forEach(allenamento => {
      const tipo = allenamento.tipo as TipoAllenamento;
      if (tipo && this.allenamentoIdsByType[tipo]) {
        this.allenamentoIdsByType[tipo].push(allenamento.id);
      }
    });
  }

  loadCorsa(): void {
    if (this.isLoadingCorsa || this.loadedTabs.corsa) return;

    const corsaIds = this.allenamentoIdsByType['CORSA'];
    if (!corsaIds.length) {
      this.loadedTabs.corsa = true;
      return;
    }

    this.isLoadingCorsa = true;
    const params = new HttpParams().set('allenamentoGiornalieroId.in', corsaIds.join(',')).set('size', '100');

    const apiUrl = this.applicationConfigService.getEndpointFor('api/corsas');

    this.http
      .get<ICorsa[]>(apiUrl, { params })
      .pipe(
        finalize(() => {
          this.isLoadingCorsa = false;
          this.loadedTabs.corsa = true;
        }),
      )
      .subscribe({
        next: data => {
          this.corsa = data;
        },
        error: error => {
          console.error('Error loading corsa:', error);
        },
      });
  }

  loadGym(): void {
    if (this.isLoadingGym || this.loadedTabs.gym) return;

    const gymIds = this.allenamentoIdsByType['GYM'];
    if (!gymIds.length) {
      this.loadedTabs.gym = true;
      return;
    }

    this.isLoadingGym = true;
    const params = new HttpParams().set('allenamentoGiornalieroId.in', gymIds.join(',')).set('size', '100');

    const apiUrl = this.applicationConfigService.getEndpointFor('api/gyms');

    this.http
      .get<IGym[]>(apiUrl, { params })
      .pipe(
        finalize(() => {
          this.isLoadingGym = false;
          this.loadedTabs.gym = true;
        }),
      )
      .subscribe({
        next: data => {
          this.gym = data;
        },
        error: error => {
          console.error('Error loading gym:', error);
        },
      });
  }

  loadCircuiti(): void {
    if (this.isLoadingCircuiti || this.loadedTabs.circuito) return;

    const circuitoIds = this.allenamentoIdsByType['CIRCUITO'];
    if (!circuitoIds.length) {
      this.loadedTabs.circuito = true;
      return;
    }

    this.isLoadingCircuiti = true;
    const params = new HttpParams().set('allenamentoGiornalieroId.in', circuitoIds.join(',')).set('size', '100');

    const apiUrl = this.applicationConfigService.getEndpointFor('api/circuitos');

    this.http
      .get<ICircuito[]>(apiUrl, { params })
      .pipe(
        finalize(() => {
          this.isLoadingCircuiti = false;
          this.loadedTabs.circuito = true;
        }),
      )
      .subscribe({
        next: data => {
          this.circuiti = data;
        },
        error: error => {
          console.error('Error loading circuiti:', error);
        },
      });
  }

  exportScheda(): void {
    const id = this.schedaSettimanale()?.id;
    if (!id) return;

    this.http.get(`/api/scheda-settimanales/${id}/export`, { responseType: 'blob' }).subscribe(blob => {
      const a = document.createElement('a');
      a.href = window.URL.createObjectURL(blob);
      a.download = `scheda-${id}.json`;
      a.click();
      window.URL.revokeObjectURL(a.href);
    });
  }

  previousState(): void {
    window.history.back();
  }
}
