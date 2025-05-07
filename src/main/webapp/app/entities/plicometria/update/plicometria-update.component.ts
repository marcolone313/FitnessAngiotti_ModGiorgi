import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IPlicometria } from '../plicometria.model';
import { PlicometriaService } from '../service/plicometria.service';
import { PlicometriaFormGroup, PlicometriaFormService } from './plicometria-form.service';

@Component({
  standalone: true,
  selector: 'jhi-plicometria-update',
  templateUrl: './plicometria-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlicometriaUpdateComponent implements OnInit {
  isSaving = false;
  plicometria: IPlicometria | null = null;

  clientesSharedCollection: ICliente[] = [];

  protected plicometriaService = inject(PlicometriaService);
  protected plicometriaFormService = inject(PlicometriaFormService);
  protected clienteService = inject(ClienteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlicometriaFormGroup = this.plicometriaFormService.createPlicometriaFormGroup();

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plicometria }) => {
      this.plicometria = plicometria;
      if (plicometria) {
        this.updateForm(plicometria);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plicometria = this.plicometriaFormService.getPlicometria(this.editForm);
    if (plicometria.id !== null) {
      this.subscribeToSaveResponse(this.plicometriaService.update(plicometria));
    } else {
      this.subscribeToSaveResponse(this.plicometriaService.create(plicometria));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlicometria>>): void {
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

  protected updateForm(plicometria: IPlicometria): void {
    this.plicometria = plicometria;
    this.plicometriaFormService.resetForm(this.editForm, plicometria);

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      plicometria.cliente,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.plicometria?.cliente)),
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }
}
