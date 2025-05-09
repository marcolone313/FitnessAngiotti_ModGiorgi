import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISchedaSettimanale } from '../scheda-settimanale.model';

import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';

import { IAllenamentoGiornaliero } from 'app/entities/allenamento-giornaliero/allenamento-giornaliero.model';

@Component({
  standalone: true,
  selector: 'jhi-scheda-settimanale-detail',
  templateUrl: './scheda-settimanale-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SchedaSettimanaleDetailComponent {
  schedaSettimanale = input<ISchedaSettimanale | null>(null);
  allenamenti: IAllenamentoGiornaliero[] = [];
  private http = inject(HttpClient);

  ngOnInit(): void {
    const id = this.schedaSettimanale()?.id;
    if (id) {
      this.http.get<IAllenamentoGiornaliero[]>(`/api/allenamenti-giornalieri/by-scheda/${id}`).subscribe(data => {
        this.allenamenti = data;
      });
    }
  }

  getAllenamentiPerTipo(tipo: string): IAllenamentoGiornaliero[] {
    return this.allenamenti.filter(a => a.tipo === tipo);
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
