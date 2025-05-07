import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LivelloCorso } from 'app/entities/enumerations/livello-corso.model';
import { ICorsoAcademy } from '../corso-academy.model';
import { CorsoAcademyService } from '../service/corso-academy.service';
import { CorsoAcademyFormGroup, CorsoAcademyFormService } from './corso-academy-form.service';

@Component({
  standalone: true,
  selector: 'jhi-corso-academy-update',
  templateUrl: './corso-academy-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CorsoAcademyUpdateComponent implements OnInit {
  isSaving = false;
  corsoAcademy: ICorsoAcademy | null = null;
  livelloCorsoValues = Object.keys(LivelloCorso);

  protected corsoAcademyService = inject(CorsoAcademyService);
  protected corsoAcademyFormService = inject(CorsoAcademyFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CorsoAcademyFormGroup = this.corsoAcademyFormService.createCorsoAcademyFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ corsoAcademy }) => {
      this.corsoAcademy = corsoAcademy;
      if (corsoAcademy) {
        this.updateForm(corsoAcademy);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const corsoAcademy = this.corsoAcademyFormService.getCorsoAcademy(this.editForm);
    if (corsoAcademy.id !== null) {
      this.subscribeToSaveResponse(this.corsoAcademyService.update(corsoAcademy));
    } else {
      this.subscribeToSaveResponse(this.corsoAcademyService.create(corsoAcademy));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICorsoAcademy>>): void {
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

  protected updateForm(corsoAcademy: ICorsoAcademy): void {
    this.corsoAcademy = corsoAcademy;
    this.corsoAcademyFormService.resetForm(this.editForm, corsoAcademy);
  }
}
