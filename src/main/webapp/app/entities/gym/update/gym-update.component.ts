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
import { IEsercizio } from 'app/entities/esercizio/esercizio.model';
import { EsercizioService } from 'app/entities/esercizio/service/esercizio.service';
import { GymService } from '../service/gym.service';
import { IGym } from '../gym.model';
import { GymFormGroup, GymFormService } from './gym-form.service';

@Component({
  standalone: true,
  selector: 'jhi-gym-update',
  templateUrl: './gym-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GymUpdateComponent implements OnInit {
  isSaving = false;
  gym: IGym | null = null;

  allenamentoGiornalierosSharedCollection: IAllenamentoGiornaliero[] = [];
  eserciziosSharedCollection: IEsercizio[] = [];

  //mod inserimento tempi limite
  recuperoMinutes = 0;
  recuperoSeconds = 0;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected gymService = inject(GymService);
  protected gymFormService = inject(GymFormService);
  protected allenamentoGiornalieroService = inject(AllenamentoGiornalieroService);
  protected esercizioService = inject(EsercizioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GymFormGroup = this.gymFormService.createGymFormGroup();

  compareAllenamentoGiornaliero = (o1: IAllenamentoGiornaliero | null, o2: IAllenamentoGiornaliero | null): boolean =>
    this.allenamentoGiornalieroService.compareAllenamentoGiornaliero(o1, o2);

  compareEsercizio = (o1: IEsercizio | null, o2: IEsercizio | null): boolean => this.esercizioService.compareEsercizio(o1, o2);

  /*Mod inserimento tempo limite
  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gym }) => {
      this.gym = gym;
      if (gym) {
        this.updateForm(gym);
      }

      this.loadRelationshipsOptions();
    });
  }*/

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gym }) => {
      this.gym = gym;
      if (gym) {
        this.updateForm(gym);

        // Analizza la durata esistente se presente
        if (gym.recupero) {
          this.parseDuration(gym.recupero);
        }
      }

      this.loadRelationshipsOptions();
    });
  }

  // Metodo per analizzare una Duration esistente
  parseDuration(durationStr: string): void {
    try {
      // Rimuovi PT all'inizio
      let cleanStr = durationStr.replace(/^PT/, '');

      // Estrai minuti e secondi
      const minutesMatch = cleanStr.match(/(\d+)M/);
      let minutes = 0;
      if (minutesMatch) {
        minutes = parseInt(minutesMatch[1], 10);
        cleanStr = cleanStr.replace(/\d+M/, '');
      }

      let seconds = 0;
      const secondsMatch = cleanStr.match(/(\d+)S/);
      if (secondsMatch) {
        seconds = parseInt(secondsMatch[1], 10);
      }

      this.recuperoMinutes = minutes;
      this.recuperoSeconds = seconds;
    } catch (error) {
      console.error(`Error parsing duration: ${durationStr}`, error);
    }
  }

  updateRecuperoValue(): void {
    // Calcola la durata in formato ISO
    let durationStr = '';

    if (this.recuperoMinutes > 0 || this.recuperoSeconds > 0) {
      durationStr = 'PT';
      if (this.recuperoMinutes > 0) {
        durationStr += `${this.recuperoMinutes}M`;
      }
      if (this.recuperoSeconds > 0) {
        durationStr += `${this.recuperoSeconds}S`;
      }
    }

    // Aggiorna il FormControl
    this.editForm.get('recupero')?.setValue(durationStr);
    this.editForm.get('recupero')?.markAsDirty();
    this.editForm.get('recupero')?.updateValueAndValidity();
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

  /*Mod tempo limite
  save(): void {
    this.isSaving = true;
    const gym = this.gymFormService.getGym(this.editForm);
    
    // Converti i campi di durata in formato ISO
    if (gym.recupero && !gym.recupero.startsWith('PT')) {
      gym.recupero = `PT${gym.recupero}S`;
    }
    
    if (gym.id !== null) {
      this.subscribeToSaveResponse(this.gymService.update(gym));
    } else {
      this.subscribeToSaveResponse(this.gymService.create(gym));
    }
  }*/

  save(): void {
    this.isSaving = true;
    const gym = this.gymFormService.getGym(this.editForm);

    // Converti i campi di minuti e secondi in durate ISO 8601
    const totalRecuperoSeconds = this.recuperoMinutes * 60 + this.recuperoSeconds;

    if (totalRecuperoSeconds > 0) {
      if (this.recuperoSeconds > 0 && this.recuperoMinutes > 0) {
        gym.recupero = `PT${this.recuperoMinutes}M${this.recuperoSeconds}S`;
      } else if (this.recuperoMinutes > 0) {
        gym.recupero = `PT${this.recuperoMinutes}M`;
      } else if (this.recuperoSeconds > 0) {
        gym.recupero = `PT${this.recuperoSeconds}S`;
      } else {
        gym.recupero = null;
      }
    } else {
      gym.recupero = null;
    }

    if (gym.id !== null) {
      this.subscribeToSaveResponse(this.gymService.update(gym));
    } else {
      this.subscribeToSaveResponse(this.gymService.create(gym));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGym>>): void {
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

  protected updateForm(gym: IGym): void {
    this.gym = gym;
    this.gymFormService.resetForm(this.editForm, gym);

    this.allenamentoGiornalierosSharedCollection =
      this.allenamentoGiornalieroService.addAllenamentoGiornalieroToCollectionIfMissing<IAllenamentoGiornaliero>(
        this.allenamentoGiornalierosSharedCollection,
        gym.allenamentoGiornaliero,
      );
    this.eserciziosSharedCollection = this.esercizioService.addEsercizioToCollectionIfMissing<IEsercizio>(
      this.eserciziosSharedCollection,
      gym.esercizio,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.allenamentoGiornalieroService
      .query()
      .pipe(map((res: HttpResponse<IAllenamentoGiornaliero[]>) => res.body ?? []))
      .pipe(
        map((allenamentoGiornalieros: IAllenamentoGiornaliero[]) =>
          this.allenamentoGiornalieroService.addAllenamentoGiornalieroToCollectionIfMissing<IAllenamentoGiornaliero>(
            allenamentoGiornalieros,
            this.gym?.allenamentoGiornaliero,
          ),
        ),
      )
      .subscribe(
        (allenamentoGiornalieros: IAllenamentoGiornaliero[]) => (this.allenamentoGiornalierosSharedCollection = allenamentoGiornalieros),
      );

    this.esercizioService
      .query()
      .pipe(map((res: HttpResponse<IEsercizio[]>) => res.body ?? []))
      .pipe(
        map((esercizios: IEsercizio[]) =>
          this.esercizioService.addEsercizioToCollectionIfMissing<IEsercizio>(esercizios, this.gym?.esercizio),
        ),
      )
      .subscribe((esercizios: IEsercizio[]) => (this.eserciziosSharedCollection = esercizios));
  }
}
