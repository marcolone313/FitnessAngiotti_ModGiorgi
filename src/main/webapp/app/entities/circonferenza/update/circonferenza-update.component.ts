import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ICirconferenza } from '../circonferenza.model';
import { CirconferenzaService } from '../service/circonferenza.service';
import { CirconferenzaFormGroup, CirconferenzaFormService } from './circonferenza-form.service';

@Component({
  standalone: true,
  selector: 'jhi-circonferenza-update',
  templateUrl: './circonferenza-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CirconferenzaUpdateComponent implements OnInit {
  isSaving = false;
  circonferenza: ICirconferenza | null = null;

  clientesSharedCollection: ICliente[] = [];

  protected circonferenzaService = inject(CirconferenzaService);
  protected circonferenzaFormService = inject(CirconferenzaFormService);
  protected clienteService = inject(ClienteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CirconferenzaFormGroup = this.circonferenzaFormService.createCirconferenzaFormGroup();

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ circonferenza }) => {
      this.circonferenza = circonferenza;
      if (circonferenza) {
        this.updateForm(circonferenza);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const circonferenza = this.circonferenzaFormService.getCirconferenza(this.editForm);
    if (circonferenza.id !== null) {
      this.subscribeToSaveResponse(this.circonferenzaService.update(circonferenza));
    } else {
      this.subscribeToSaveResponse(this.circonferenzaService.create(circonferenza));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICirconferenza>>): void {
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

  protected updateForm(circonferenza: ICirconferenza): void {
    this.circonferenza = circonferenza;
    this.circonferenzaFormService.resetForm(this.editForm, circonferenza);

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      circonferenza.cliente,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.circonferenza?.cliente)),
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }
}
