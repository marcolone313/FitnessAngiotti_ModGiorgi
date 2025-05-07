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
import { ISchedaSettimanale } from 'app/entities/scheda-settimanale/scheda-settimanale.model';
import { SchedaSettimanaleService } from 'app/entities/scheda-settimanale/service/scheda-settimanale.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { Voto } from 'app/entities/enumerations/voto.model';
import { ReportSettimanaleService } from '../service/report-settimanale.service';
import { IReportSettimanale } from '../report-settimanale.model';
import { ReportSettimanaleFormGroup, ReportSettimanaleFormService } from './report-settimanale-form.service';

@Component({
  standalone: true,
  selector: 'jhi-report-settimanale-update',
  templateUrl: './report-settimanale-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportSettimanaleUpdateComponent implements OnInit {
  isSaving = false;
  reportSettimanale: IReportSettimanale | null = null;
  votoValues = Object.keys(Voto);

  schedaSettimanalesCollection: ISchedaSettimanale[] = [];
  clientesSharedCollection: ICliente[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected reportSettimanaleService = inject(ReportSettimanaleService);
  protected reportSettimanaleFormService = inject(ReportSettimanaleFormService);
  protected schedaSettimanaleService = inject(SchedaSettimanaleService);
  protected clienteService = inject(ClienteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportSettimanaleFormGroup = this.reportSettimanaleFormService.createReportSettimanaleFormGroup();

  compareSchedaSettimanale = (o1: ISchedaSettimanale | null, o2: ISchedaSettimanale | null): boolean =>
    this.schedaSettimanaleService.compareSchedaSettimanale(o1, o2);

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportSettimanale }) => {
      this.reportSettimanale = reportSettimanale;
      if (reportSettimanale) {
        this.updateForm(reportSettimanale);
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
    const reportSettimanale = this.reportSettimanaleFormService.getReportSettimanale(this.editForm);
    if (reportSettimanale.id !== null) {
      this.subscribeToSaveResponse(this.reportSettimanaleService.update(reportSettimanale));
    } else {
      this.subscribeToSaveResponse(this.reportSettimanaleService.create(reportSettimanale));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportSettimanale>>): void {
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

  protected updateForm(reportSettimanale: IReportSettimanale): void {
    this.reportSettimanale = reportSettimanale;
    this.reportSettimanaleFormService.resetForm(this.editForm, reportSettimanale);

    this.schedaSettimanalesCollection = this.schedaSettimanaleService.addSchedaSettimanaleToCollectionIfMissing<ISchedaSettimanale>(
      this.schedaSettimanalesCollection,
      reportSettimanale.schedaSettimanale,
    );
    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      reportSettimanale.cliente,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.schedaSettimanaleService
      .query({ 'reportSettimanaleId.specified': 'false' })
      .pipe(map((res: HttpResponse<ISchedaSettimanale[]>) => res.body ?? []))
      .pipe(
        map((schedaSettimanales: ISchedaSettimanale[]) =>
          this.schedaSettimanaleService.addSchedaSettimanaleToCollectionIfMissing<ISchedaSettimanale>(
            schedaSettimanales,
            this.reportSettimanale?.schedaSettimanale,
          ),
        ),
      )
      .subscribe((schedaSettimanales: ISchedaSettimanale[]) => (this.schedaSettimanalesCollection = schedaSettimanales));

    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) =>
          this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.reportSettimanale?.cliente),
        ),
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }
}
