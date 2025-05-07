import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IPesoCliente } from '../peso-cliente.model';
import { PesoClienteService } from '../service/peso-cliente.service';
import { PesoClienteFormGroup, PesoClienteFormService } from './peso-cliente-form.service';

@Component({
  standalone: true,
  selector: 'jhi-peso-cliente-update',
  templateUrl: './peso-cliente-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PesoClienteUpdateComponent implements OnInit {
  isSaving = false;
  pesoCliente: IPesoCliente | null = null;

  clientesSharedCollection: ICliente[] = [];

  protected pesoClienteService = inject(PesoClienteService);
  protected pesoClienteFormService = inject(PesoClienteFormService);
  protected clienteService = inject(ClienteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PesoClienteFormGroup = this.pesoClienteFormService.createPesoClienteFormGroup();

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pesoCliente }) => {
      this.pesoCliente = pesoCliente;
      if (pesoCliente) {
        this.updateForm(pesoCliente);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pesoCliente = this.pesoClienteFormService.getPesoCliente(this.editForm);
    if (pesoCliente.id !== null) {
      this.subscribeToSaveResponse(this.pesoClienteService.update(pesoCliente));
    } else {
      this.subscribeToSaveResponse(this.pesoClienteService.create(pesoCliente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPesoCliente>>): void {
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

  protected updateForm(pesoCliente: IPesoCliente): void {
    this.pesoCliente = pesoCliente;
    this.pesoClienteFormService.resetForm(this.editForm, pesoCliente);

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      pesoCliente.cliente,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.pesoCliente?.cliente)),
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }
}
