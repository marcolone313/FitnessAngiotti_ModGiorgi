import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAllenamentoGiornaliero } from 'app/entities/allenamento-giornaliero/allenamento-giornaliero.model';
import { AllenamentoGiornalieroService } from 'app/entities/allenamento-giornaliero/service/allenamento-giornaliero.service';
import { CircuitoService } from '../service/circuito.service';
import { ICircuito } from '../circuito.model';
import { CircuitoFormGroup, CircuitoFormService } from './circuito-form.service';

@Component({
  standalone: true,
  selector: 'jhi-circuito-update',
  templateUrl: './circuito-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CircuitoUpdateComponent implements OnInit {
  isSaving = false;
  circuito: ICircuito | null = null;

  //mod tempo limite
  tempoLimiteMinutes = 0;
  tempoLimiteSeconds = 0;

  allenamentoGiornalierosCollection: IAllenamentoGiornaliero[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected circuitoService = inject(CircuitoService);
  protected circuitoFormService = inject(CircuitoFormService);
  protected allenamentoGiornalieroService = inject(AllenamentoGiornalieroService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CircuitoFormGroup = this.circuitoFormService.createCircuitoFormGroup();

  compareAllenamentoGiornaliero = (o1: IAllenamentoGiornaliero | null, o2: IAllenamentoGiornaliero | null): boolean =>
    this.allenamentoGiornalieroService.compareAllenamentoGiornaliero(o1, o2);

  /*ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ circuito }) => {
      this.circuito = circuito;
      if (circuito) {
        this.updateForm(circuito);
      }

      this.loadRelationshipsOptions();
    });
  }*/

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ circuito }) => {
      this.circuito = circuito;
      if (circuito) {
        this.updateForm(circuito);

        // Analizza la durata esistente per il tempo limite
        if (circuito.tempoLimite) {
          this.parseDuration(circuito.tempoLimite);
        }
      }

      this.loadRelationshipsOptions();
    });
  }

  // Metodo per analizzare una Duration esistente
  parseDuration(durationStr: string): void {
    // Per durate nel formato PT1M30S o solo PT90S
    try {
      // Rimuovi PT all'inizio e S alla fine
      let cleanStr = durationStr.replace(/^PT/, '').replace(/S$/, '');

      // Estrai minuti e secondi
      const minutesMatch = cleanStr.match(/(\d+)M/);
      let minutes = 0;
      if (minutesMatch) {
        minutes = parseInt(minutesMatch[1], 10);
        cleanStr = cleanStr.replace(/\d+M/, '');
      }

      let seconds = 0;
      if (cleanStr) {
        seconds = parseInt(cleanStr, 10);
      }

      this.tempoLimiteMinutes = minutes;
      this.tempoLimiteSeconds = seconds;
    } catch (error) {
      console.error(`Error parsing duration: ${durationStr}`, error);
    }
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('fitnessAngiottiApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  /*save(): void {
    this.isSaving = true;
    const circuito = this.circuitoFormService.getCircuito(this.editForm);
    
    // Converti i campi di durata in formato ISO
    if (circuito.tempoLimite && !circuito.tempoLimite.startsWith('PT')) {
      circuito.tempoLimite = `PT${circuito.tempoLimite}S`;
    }
    
    if (circuito.tempoImpiegato && !circuito.tempoImpiegato.startsWith('PT')) {
      circuito.tempoImpiegato = `PT${circuito.tempoImpiegato}S`;
    }
    
    if (circuito.id !== null) {
      this.subscribeToSaveResponse(this.circuitoService.update(circuito));
    } else {
      this.subscribeToSaveResponse(this.circuitoService.create(circuito));
    }
  }*/

  save(): void {
    this.isSaving = true;
    const circuito = this.circuitoFormService.getCircuito(this.editForm);

    // Converti i campi di minuti e secondi in durate ISO 8601 solo per tempoLimite
    const totalTempoLimiteSeconds = this.tempoLimiteMinutes * 60 + this.tempoLimiteSeconds;

    if (totalTempoLimiteSeconds > 0) {
      if (this.tempoLimiteSeconds > 0 && this.tempoLimiteMinutes > 0) {
        circuito.tempoLimite = `PT${this.tempoLimiteMinutes}M${this.tempoLimiteSeconds}S`;
      } else if (this.tempoLimiteMinutes > 0) {
        circuito.tempoLimite = `PT${this.tempoLimiteMinutes}M`;
      } else if (this.tempoLimiteSeconds > 0) {
        circuito.tempoLimite = `PT${this.tempoLimiteSeconds}S`;
      } else {
        circuito.tempoLimite = null;
      }
    } else {
      circuito.tempoLimite = null;
    }

    if (circuito.id !== null) {
      this.subscribeToSaveResponse(this.circuitoService.update(circuito));
    } else {
      this.subscribeToSaveResponse(this.circuitoService.create(circuito));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICircuito>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(circuito: ICircuito): void {
    this.circuito = circuito;
    this.circuitoFormService.resetForm(this.editForm, circuito);

    this.allenamentoGiornalierosCollection =
      this.allenamentoGiornalieroService.addAllenamentoGiornalieroToCollectionIfMissing<IAllenamentoGiornaliero>(
        this.allenamentoGiornalierosCollection,
        circuito.allenamentoGiornaliero,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.allenamentoGiornalieroService
      .query({ 'circuitoId.specified': 'false' })
      .pipe(map((res: HttpResponse<IAllenamentoGiornaliero[]>) => res.body ?? []))
      .pipe(
        map((allenamentoGiornalieros: IAllenamentoGiornaliero[]) =>
          this.allenamentoGiornalieroService.addAllenamentoGiornalieroToCollectionIfMissing<IAllenamentoGiornaliero>(
            allenamentoGiornalieros,
            this.circuito?.allenamentoGiornaliero,
          ),
        ),
      )
      .subscribe(
        (allenamentoGiornalieros: IAllenamentoGiornaliero[]) => (this.allenamentoGiornalierosCollection = allenamentoGiornalieros),
      );
  }
}
