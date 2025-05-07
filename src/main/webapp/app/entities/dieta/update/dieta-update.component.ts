import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IDieta } from '../dieta.model';
import { DietaService } from '../service/dieta.service';
import { DietaFormGroup, DietaFormService } from './dieta-form.service';

@Component({
  standalone: true,
  selector: 'jhi-dieta-update',
  templateUrl: './dieta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DietaUpdateComponent implements OnInit {
  isSaving = false;
  dieta: IDieta | null = null;

  clientesSharedCollection: ICliente[] = [];

  protected dietaService = inject(DietaService);
  protected dietaFormService = inject(DietaFormService);
  protected clienteService = inject(ClienteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DietaFormGroup = this.dietaFormService.createDietaFormGroup();

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dieta }) => {
      this.dieta = dieta;
      if (dieta) {
        this.updateForm(dieta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dieta = this.dietaFormService.getDieta(this.editForm);
    if (dieta.id !== null) {
      this.subscribeToSaveResponse(this.dietaService.update(dieta));
    } else {
      this.subscribeToSaveResponse(this.dietaService.create(dieta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDieta>>): void {
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

  protected updateForm(dieta: IDieta): void {
    this.dieta = dieta;
    this.dietaFormService.resetForm(this.editForm, dieta);

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      dieta.cliente,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.dieta?.cliente)))
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }
}
