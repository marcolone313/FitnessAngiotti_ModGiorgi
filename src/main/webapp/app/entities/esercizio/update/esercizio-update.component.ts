import { Component, ElementRef, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { TipoEsercizio } from 'app/entities/enumerations/tipo-esercizio.model';
import { EsercizioService } from '../service/esercizio.service';
import { IEsercizio } from '../esercizio.model';
import { EsercizioFormGroup, EsercizioFormService } from './esercizio-form.service';

@Component({
  standalone: true,
  selector: 'jhi-esercizio-update',
  templateUrl: './esercizio-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EsercizioUpdateComponent implements OnInit {
  isSaving = false;
  esercizio: IEsercizio | null = null;
  tipoEsercizioValues = Object.keys(TipoEsercizio);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected esercizioService = inject(EsercizioService);
  protected esercizioFormService = inject(EsercizioFormService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EsercizioFormGroup = this.esercizioFormService.createEsercizioFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ esercizio }) => {
      this.esercizio = esercizio;
      if (esercizio) {
        this.updateForm(esercizio);
      }
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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector(`#${idInput}`)) {
      this.elementRef.nativeElement.querySelector(`#${idInput}`).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const esercizio = this.esercizioFormService.getEsercizio(this.editForm);
    if (esercizio.id !== null) {
      this.subscribeToSaveResponse(this.esercizioService.update(esercizio));
    } else {
      this.subscribeToSaveResponse(this.esercizioService.create(esercizio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEsercizio>>): void {
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

  protected updateForm(esercizio: IEsercizio): void {
    this.esercizio = esercizio;
    this.esercizioFormService.resetForm(this.editForm, esercizio);
  }
}
