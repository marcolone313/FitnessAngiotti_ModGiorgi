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
import { CorsaService } from '../service/corsa.service';
import { ICorsa } from '../corsa.model';
import { CorsaFormGroup, CorsaFormService } from './corsa-form.service';

@Component({
  standalone: true,
  selector: 'jhi-corsa-update',
  templateUrl: './corsa-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CorsaUpdateComponent implements OnInit {
  isSaving = false;
  corsa: ICorsa | null = null;

  allenamentoGiornalierosCollection: IAllenamentoGiornaliero[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected corsaService = inject(CorsaService);
  protected corsaFormService = inject(CorsaFormService);
  protected allenamentoGiornalieroService = inject(AllenamentoGiornalieroService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CorsaFormGroup = this.corsaFormService.createCorsaFormGroup();

  compareAllenamentoGiornaliero = (o1: IAllenamentoGiornaliero | null, o2: IAllenamentoGiornaliero | null): boolean =>
    this.allenamentoGiornalieroService.compareAllenamentoGiornaliero(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ corsa }) => {
      this.corsa = corsa;
      if (corsa) {
        this.updateForm(corsa);
      }

      this.loadRelationshipsOptions();
    });
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

  save(): void {
    this.isSaving = true;
    const corsa = this.corsaFormService.getCorsa(this.editForm);
    if (corsa.id !== null) {
      this.subscribeToSaveResponse(this.corsaService.update(corsa));
    } else {
      this.subscribeToSaveResponse(this.corsaService.create(corsa));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICorsa>>): void {
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

  protected updateForm(corsa: ICorsa): void {
    this.corsa = corsa;
    this.corsaFormService.resetForm(this.editForm, corsa);

    this.allenamentoGiornalierosCollection =
      this.allenamentoGiornalieroService.addAllenamentoGiornalieroToCollectionIfMissing<IAllenamentoGiornaliero>(
        this.allenamentoGiornalierosCollection,
        corsa.allenamentoGiornaliero,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.allenamentoGiornalieroService
      .query({ 'corsaId.specified': 'false' })
      .pipe(map((res: HttpResponse<IAllenamentoGiornaliero[]>) => res.body ?? []))
      .pipe(
        map((allenamentoGiornalieros: IAllenamentoGiornaliero[]) =>
          this.allenamentoGiornalieroService.addAllenamentoGiornalieroToCollectionIfMissing<IAllenamentoGiornaliero>(
            allenamentoGiornalieros,
            this.corsa?.allenamentoGiornaliero,
          ),
        ),
      )
      .subscribe(
        (allenamentoGiornalieros: IAllenamentoGiornaliero[]) => (this.allenamentoGiornalierosCollection = allenamentoGiornalieros),
      );
  }
}
