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
import { IEsercizio } from 'app/entities/esercizio/esercizio.model';
import { EsercizioService } from 'app/entities/esercizio/service/esercizio.service';
import { PassaggioEsercizioService } from '../service/passaggio-esercizio.service';
import { IPassaggioEsercizio } from '../passaggio-esercizio.model';
import { PassaggioEsercizioFormGroup, PassaggioEsercizioFormService } from './passaggio-esercizio-form.service';

@Component({
  standalone: true,
  selector: 'jhi-passaggio-esercizio-update',
  templateUrl: './passaggio-esercizio-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PassaggioEsercizioUpdateComponent implements OnInit {
  isSaving = false;
  passaggioEsercizio: IPassaggioEsercizio | null = null;

  eserciziosSharedCollection: IEsercizio[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected passaggioEsercizioService = inject(PassaggioEsercizioService);
  protected passaggioEsercizioFormService = inject(PassaggioEsercizioFormService);
  protected esercizioService = inject(EsercizioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PassaggioEsercizioFormGroup = this.passaggioEsercizioFormService.createPassaggioEsercizioFormGroup();

  compareEsercizio = (o1: IEsercizio | null, o2: IEsercizio | null): boolean => this.esercizioService.compareEsercizio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ passaggioEsercizio }) => {
      this.passaggioEsercizio = passaggioEsercizio;
      if (passaggioEsercizio) {
        this.updateForm(passaggioEsercizio);
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
    const passaggioEsercizio = this.passaggioEsercizioFormService.getPassaggioEsercizio(this.editForm);
    if (passaggioEsercizio.id !== null) {
      this.subscribeToSaveResponse(this.passaggioEsercizioService.update(passaggioEsercizio));
    } else {
      this.subscribeToSaveResponse(this.passaggioEsercizioService.create(passaggioEsercizio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPassaggioEsercizio>>): void {
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

  protected updateForm(passaggioEsercizio: IPassaggioEsercizio): void {
    this.passaggioEsercizio = passaggioEsercizio;
    this.passaggioEsercizioFormService.resetForm(this.editForm, passaggioEsercizio);

    this.eserciziosSharedCollection = this.esercizioService.addEsercizioToCollectionIfMissing<IEsercizio>(
      this.eserciziosSharedCollection,
      passaggioEsercizio.esercizio,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.esercizioService
      .query()
      .pipe(map((res: HttpResponse<IEsercizio[]>) => res.body ?? []))
      .pipe(
        map((esercizios: IEsercizio[]) =>
          this.esercizioService.addEsercizioToCollectionIfMissing<IEsercizio>(esercizios, this.passaggioEsercizio?.esercizio),
        ),
      )
      .subscribe((esercizios: IEsercizio[]) => (this.eserciziosSharedCollection = esercizios));
  }
}
