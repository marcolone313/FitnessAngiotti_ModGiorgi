import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEsercizio } from 'app/entities/esercizio/esercizio.model';
import { EsercizioService } from 'app/entities/esercizio/service/esercizio.service';
import { ICircuito } from 'app/entities/circuito/circuito.model';
import { CircuitoService } from 'app/entities/circuito/service/circuito.service';
import { CircuitoToEsercizioService } from '../service/circuito-to-esercizio.service';
import { ICircuitoToEsercizio } from '../circuito-to-esercizio.model';
import { CircuitoToEsercizioFormGroup, CircuitoToEsercizioFormService } from './circuito-to-esercizio-form.service';

@Component({
  standalone: true,
  selector: 'jhi-circuito-to-esercizio-update',
  templateUrl: './circuito-to-esercizio-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CircuitoToEsercizioUpdateComponent implements OnInit {
  isSaving = false;
  circuitoToEsercizio: ICircuitoToEsercizio | null = null;

  eserciziosSharedCollection: IEsercizio[] = [];
  circuitosSharedCollection: ICircuito[] = [];

  protected circuitoToEsercizioService = inject(CircuitoToEsercizioService);
  protected circuitoToEsercizioFormService = inject(CircuitoToEsercizioFormService);
  protected esercizioService = inject(EsercizioService);
  protected circuitoService = inject(CircuitoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CircuitoToEsercizioFormGroup = this.circuitoToEsercizioFormService.createCircuitoToEsercizioFormGroup();

  compareEsercizio = (o1: IEsercizio | null, o2: IEsercizio | null): boolean => this.esercizioService.compareEsercizio(o1, o2);

  compareCircuito = (o1: ICircuito | null, o2: ICircuito | null): boolean => this.circuitoService.compareCircuito(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ circuitoToEsercizio }) => {
      this.circuitoToEsercizio = circuitoToEsercizio;
      if (circuitoToEsercizio) {
        this.updateForm(circuitoToEsercizio);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const circuitoToEsercizio = this.circuitoToEsercizioFormService.getCircuitoToEsercizio(this.editForm);
    if (circuitoToEsercizio.id !== null) {
      this.subscribeToSaveResponse(this.circuitoToEsercizioService.update(circuitoToEsercizio));
    } else {
      this.subscribeToSaveResponse(this.circuitoToEsercizioService.create(circuitoToEsercizio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICircuitoToEsercizio>>): void {
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

  protected updateForm(circuitoToEsercizio: ICircuitoToEsercizio): void {
    this.circuitoToEsercizio = circuitoToEsercizio;
    this.circuitoToEsercizioFormService.resetForm(this.editForm, circuitoToEsercizio);

    this.eserciziosSharedCollection = this.esercizioService.addEsercizioToCollectionIfMissing<IEsercizio>(
      this.eserciziosSharedCollection,
      circuitoToEsercizio.esercizio,
    );
    this.circuitosSharedCollection = this.circuitoService.addCircuitoToCollectionIfMissing<ICircuito>(
      this.circuitosSharedCollection,
      circuitoToEsercizio.circuito,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.esercizioService
      .query()
      .pipe(map((res: HttpResponse<IEsercizio[]>) => res.body ?? []))
      .pipe(
        map((esercizios: IEsercizio[]) =>
          this.esercizioService.addEsercizioToCollectionIfMissing<IEsercizio>(esercizios, this.circuitoToEsercizio?.esercizio),
        ),
      )
      .subscribe((esercizios: IEsercizio[]) => (this.eserciziosSharedCollection = esercizios));

    this.circuitoService
      .query()
      .pipe(map((res: HttpResponse<ICircuito[]>) => res.body ?? []))
      .pipe(
        map((circuitos: ICircuito[]) =>
          this.circuitoService.addCircuitoToCollectionIfMissing<ICircuito>(circuitos, this.circuitoToEsercizio?.circuito),
        ),
      )
      .subscribe((circuitos: ICircuito[]) => (this.circuitosSharedCollection = circuitos));
  }
}
