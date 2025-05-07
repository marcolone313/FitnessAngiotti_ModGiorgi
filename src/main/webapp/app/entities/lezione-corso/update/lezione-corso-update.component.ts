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
import { ICorsoAcademy } from 'app/entities/corso-academy/corso-academy.model';
import { CorsoAcademyService } from 'app/entities/corso-academy/service/corso-academy.service';
import { LezioneCorsoService } from '../service/lezione-corso.service';
import { ILezioneCorso } from '../lezione-corso.model';
import { LezioneCorsoFormGroup, LezioneCorsoFormService } from './lezione-corso-form.service';

@Component({
  standalone: true,
  selector: 'jhi-lezione-corso-update',
  templateUrl: './lezione-corso-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LezioneCorsoUpdateComponent implements OnInit {
  isSaving = false;
  lezioneCorso: ILezioneCorso | null = null;

  corsoAcademiesSharedCollection: ICorsoAcademy[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected lezioneCorsoService = inject(LezioneCorsoService);
  protected lezioneCorsoFormService = inject(LezioneCorsoFormService);
  protected corsoAcademyService = inject(CorsoAcademyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LezioneCorsoFormGroup = this.lezioneCorsoFormService.createLezioneCorsoFormGroup();

  compareCorsoAcademy = (o1: ICorsoAcademy | null, o2: ICorsoAcademy | null): boolean =>
    this.corsoAcademyService.compareCorsoAcademy(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lezioneCorso }) => {
      this.lezioneCorso = lezioneCorso;
      if (lezioneCorso) {
        this.updateForm(lezioneCorso);
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
    const lezioneCorso = this.lezioneCorsoFormService.getLezioneCorso(this.editForm);
    if (lezioneCorso.id !== null) {
      this.subscribeToSaveResponse(this.lezioneCorsoService.update(lezioneCorso));
    } else {
      this.subscribeToSaveResponse(this.lezioneCorsoService.create(lezioneCorso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILezioneCorso>>): void {
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

  protected updateForm(lezioneCorso: ILezioneCorso): void {
    this.lezioneCorso = lezioneCorso;
    this.lezioneCorsoFormService.resetForm(this.editForm, lezioneCorso);

    this.corsoAcademiesSharedCollection = this.corsoAcademyService.addCorsoAcademyToCollectionIfMissing<ICorsoAcademy>(
      this.corsoAcademiesSharedCollection,
      lezioneCorso.corsoAcademy,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.corsoAcademyService
      .query()
      .pipe(map((res: HttpResponse<ICorsoAcademy[]>) => res.body ?? []))
      .pipe(
        map((corsoAcademies: ICorsoAcademy[]) =>
          this.corsoAcademyService.addCorsoAcademyToCollectionIfMissing<ICorsoAcademy>(corsoAcademies, this.lezioneCorso?.corsoAcademy),
        ),
      )
      .subscribe((corsoAcademies: ICorsoAcademy[]) => (this.corsoAcademiesSharedCollection = corsoAcademies));
  }
}
