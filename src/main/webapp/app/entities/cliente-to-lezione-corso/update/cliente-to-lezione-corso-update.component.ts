import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ILezioneCorso } from 'app/entities/lezione-corso/lezione-corso.model';
import { LezioneCorsoService } from 'app/entities/lezione-corso/service/lezione-corso.service';
import { ClienteToLezioneCorsoService } from '../service/cliente-to-lezione-corso.service';
import { IClienteToLezioneCorso } from '../cliente-to-lezione-corso.model';
import { ClienteToLezioneCorsoFormGroup, ClienteToLezioneCorsoFormService } from './cliente-to-lezione-corso-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cliente-to-lezione-corso-update',
  templateUrl: './cliente-to-lezione-corso-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClienteToLezioneCorsoUpdateComponent implements OnInit {
  isSaving = false;
  clienteToLezioneCorso: IClienteToLezioneCorso | null = null;

  clientesSharedCollection: ICliente[] = [];
  lezioneCorsosSharedCollection: ILezioneCorso[] = [];

  protected clienteToLezioneCorsoService = inject(ClienteToLezioneCorsoService);
  protected clienteToLezioneCorsoFormService = inject(ClienteToLezioneCorsoFormService);
  protected clienteService = inject(ClienteService);
  protected lezioneCorsoService = inject(LezioneCorsoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClienteToLezioneCorsoFormGroup = this.clienteToLezioneCorsoFormService.createClienteToLezioneCorsoFormGroup();

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  compareLezioneCorso = (o1: ILezioneCorso | null, o2: ILezioneCorso | null): boolean =>
    this.lezioneCorsoService.compareLezioneCorso(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clienteToLezioneCorso }) => {
      this.clienteToLezioneCorso = clienteToLezioneCorso;
      if (clienteToLezioneCorso) {
        this.updateForm(clienteToLezioneCorso);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clienteToLezioneCorso = this.clienteToLezioneCorsoFormService.getClienteToLezioneCorso(this.editForm);
    if (clienteToLezioneCorso.id !== null) {
      this.subscribeToSaveResponse(this.clienteToLezioneCorsoService.update(clienteToLezioneCorso));
    } else {
      this.subscribeToSaveResponse(this.clienteToLezioneCorsoService.create(clienteToLezioneCorso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClienteToLezioneCorso>>): void {
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

  protected updateForm(clienteToLezioneCorso: IClienteToLezioneCorso): void {
    this.clienteToLezioneCorso = clienteToLezioneCorso;
    this.clienteToLezioneCorsoFormService.resetForm(this.editForm, clienteToLezioneCorso);

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      clienteToLezioneCorso.cliente,
    );
    this.lezioneCorsosSharedCollection = this.lezioneCorsoService.addLezioneCorsoToCollectionIfMissing<ILezioneCorso>(
      this.lezioneCorsosSharedCollection,
      clienteToLezioneCorso.lezioneCorso,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) =>
          this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.clienteToLezioneCorso?.cliente),
        ),
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));

    this.lezioneCorsoService
      .query()
      .pipe(map((res: HttpResponse<ILezioneCorso[]>) => res.body ?? []))
      .pipe(
        map((lezioneCorsos: ILezioneCorso[]) =>
          this.lezioneCorsoService.addLezioneCorsoToCollectionIfMissing<ILezioneCorso>(
            lezioneCorsos,
            this.clienteToLezioneCorso?.lezioneCorso,
          ),
        ),
      )
      .subscribe((lezioneCorsos: ILezioneCorso[]) => (this.lezioneCorsosSharedCollection = lezioneCorsos));
  }
}
