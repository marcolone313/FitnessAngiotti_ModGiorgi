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
import { ISchedaSettimanale } from 'app/entities/scheda-settimanale/scheda-settimanale.model';
import { SchedaSettimanaleService } from 'app/entities/scheda-settimanale/service/scheda-settimanale.service';
import { TipoAllenamento } from 'app/entities/enumerations/tipo-allenamento.model';
import { AllenamentoGiornalieroService } from '../service/allenamento-giornaliero.service';
import { IAllenamentoGiornaliero } from '../allenamento-giornaliero.model';
import { AllenamentoGiornalieroFormGroup, AllenamentoGiornalieroFormService } from './allenamento-giornaliero-form.service';

@Component({
  standalone: true,
  selector: 'jhi-allenamento-giornaliero-update',
  templateUrl: './allenamento-giornaliero-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AllenamentoGiornalieroUpdateComponent implements OnInit {
  isSaving = false;
  allenamentoGiornaliero: IAllenamentoGiornaliero | null = null;
  tipoAllenamentoValues = Object.keys(TipoAllenamento);

  schedaSettimanalesSharedCollection: ISchedaSettimanale[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected allenamentoGiornalieroService = inject(AllenamentoGiornalieroService);
  protected allenamentoGiornalieroFormService = inject(AllenamentoGiornalieroFormService);
  protected schedaSettimanaleService = inject(SchedaSettimanaleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AllenamentoGiornalieroFormGroup = this.allenamentoGiornalieroFormService.createAllenamentoGiornalieroFormGroup();

  compareSchedaSettimanale = (o1: ISchedaSettimanale | null, o2: ISchedaSettimanale | null): boolean =>
    this.schedaSettimanaleService.compareSchedaSettimanale(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ allenamentoGiornaliero }) => {
      this.allenamentoGiornaliero = allenamentoGiornaliero;
      if (allenamentoGiornaliero) {
        this.updateForm(allenamentoGiornaliero);
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
    const allenamentoGiornaliero = this.allenamentoGiornalieroFormService.getAllenamentoGiornaliero(this.editForm);
    if (allenamentoGiornaliero.id !== null) {
      this.subscribeToSaveResponse(this.allenamentoGiornalieroService.update(allenamentoGiornaliero));
    } else {
      this.subscribeToSaveResponse(this.allenamentoGiornalieroService.create(allenamentoGiornaliero));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAllenamentoGiornaliero>>): void {
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

  protected updateForm(allenamentoGiornaliero: IAllenamentoGiornaliero): void {
    this.allenamentoGiornaliero = allenamentoGiornaliero;
    this.allenamentoGiornalieroFormService.resetForm(this.editForm, allenamentoGiornaliero);

    this.schedaSettimanalesSharedCollection = this.schedaSettimanaleService.addSchedaSettimanaleToCollectionIfMissing<ISchedaSettimanale>(
      this.schedaSettimanalesSharedCollection,
      allenamentoGiornaliero.schedaSettimanale,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.schedaSettimanaleService
      .query()
      .pipe(map((res: HttpResponse<ISchedaSettimanale[]>) => res.body ?? []))
      .pipe(
        map((schedaSettimanales: ISchedaSettimanale[]) =>
          this.schedaSettimanaleService.addSchedaSettimanaleToCollectionIfMissing<ISchedaSettimanale>(
            schedaSettimanales,
            this.allenamentoGiornaliero?.schedaSettimanale,
          ),
        ),
      )
      .subscribe((schedaSettimanales: ISchedaSettimanale[]) => (this.schedaSettimanalesSharedCollection = schedaSettimanales));
  }
}
