import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ISchedaSettimanale } from '../scheda-settimanale.model';
import { SchedaSettimanaleService } from '../service/scheda-settimanale.service';
import { SchedaSettimanaleFormGroup, SchedaSettimanaleFormService } from './scheda-settimanale-form.service';

@Component({
  standalone: true,
  selector: 'jhi-scheda-settimanale-update',
  templateUrl: './scheda-settimanale-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SchedaSettimanaleUpdateComponent implements OnInit {
  isSaving = false;
  schedaSettimanale: ISchedaSettimanale | null = null;

  clientesSharedCollection: ICliente[] = [];

  protected schedaSettimanaleService = inject(SchedaSettimanaleService);
  protected schedaSettimanaleFormService = inject(SchedaSettimanaleFormService);
  protected clienteService = inject(ClienteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SchedaSettimanaleFormGroup = this.schedaSettimanaleFormService.createSchedaSettimanaleFormGroup();

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ schedaSettimanale }) => {
      this.schedaSettimanale = schedaSettimanale;
      if (schedaSettimanale) {
        this.updateForm(schedaSettimanale);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const schedaSettimanale = this.schedaSettimanaleFormService.getSchedaSettimanale(this.editForm);
    if (schedaSettimanale.id !== null) {
      this.subscribeToSaveResponse(this.schedaSettimanaleService.update(schedaSettimanale));
    } else {
      this.subscribeToSaveResponse(this.schedaSettimanaleService.create(schedaSettimanale));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchedaSettimanale>>): void {
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

  protected updateForm(schedaSettimanale: ISchedaSettimanale): void {
    this.schedaSettimanale = schedaSettimanale;
    this.schedaSettimanaleFormService.resetForm(this.editForm, schedaSettimanale);

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      schedaSettimanale.cliente,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) =>
          this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.schedaSettimanale?.cliente),
        ),
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }
}
