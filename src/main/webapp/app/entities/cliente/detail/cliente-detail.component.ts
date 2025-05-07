import { AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { ICliente } from '../cliente.model';
import { PlicometriaService } from 'app/entities/plicometria/service/plicometria.service';
import { IPlicometria, NewPlicometria } from 'app/entities/plicometria/plicometria.model';
import { PesoClienteService } from 'app/entities/peso-cliente/service/peso-cliente.service';
import { IPesoCliente, NewPesoCliente } from 'app/entities/peso-cliente/peso-cliente.model';
import { CirconferenzaService } from 'app/entities/circonferenza/service/circonferenza.service';
import { ICirconferenza, NewCirconferenza } from 'app/entities/circonferenza/circonferenza.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PesoClienteUpdateComponent } from 'app/entities/peso-cliente/update/peso-cliente-update.component';
import { PlicometriaUpdateComponent } from 'app/entities/plicometria/update/plicometria-update.component';
import { CirconferenzaUpdateComponent } from 'app/entities/circonferenza/update/circonferenza-update.component';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import dayjs from 'dayjs/esm';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { PesoClienteDeleteDialogComponent } from 'app/entities/peso-cliente/delete/peso-cliente-delete-dialog.component';
import { PlicometriaDeleteDialogComponent } from 'app/entities/plicometria/delete/plicometria-delete-dialog.component';
import { CirconferenzaDeleteDialogComponent } from 'app/entities/circonferenza/delete/circonferenza-delete-dialog.component';
import { filter } from 'rxjs/operators';

@Component({
  standalone: true,
  selector: 'jhi-cliente-detail',
  templateUrl: './cliente-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, ReactiveFormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ClienteDetailComponent implements AfterViewInit {
  cliente = input<ICliente | null>(null);
  protected dataUtils = inject(DataUtils);

  // Services
  plicometriaSrv = inject(PlicometriaService);
  pesoSrv = inject(PesoClienteService);
  circonferenzaSrv = inject(CirconferenzaService);
  modalService = inject(NgbModal);
  fb = inject(FormBuilder);
  cdr = inject(ChangeDetectorRef);

  // Data collections
  plicometrie: IPlicometria[] = [];
  pesi: IPesoCliente[] = [];
  circonferenze: ICirconferenza[] = [];

  // Tab control
  activeTab = 'peso';

  // Quick add forms
  pesoForm: FormGroup;
  plicometriaForm: FormGroup;
  circonferenzaForm: FormGroup;

  // Loading states
  isLoadingPeso = false;
  isLoadingPlic = false;
  isLoadingCirc = false;

  // Pagination
  itemsPerPage = 10;
  pesoPage = 1;
  plicPage = 1;
  circPage = 1;

  constructor() {
    // Initialize forms
    this.pesoForm = this.fb.group({
      peso: ['', [Validators.required, Validators.min(0)]],
      mese: ['', [Validators.required, Validators.min(1), Validators.max(12)]],
      dataInserimento: [dayjs(), [Validators.required]],
    });

    this.plicometriaForm = this.fb.group({
      tricipite: ['', [Validators.min(0)]],
      petto: ['', [Validators.min(0)]],
      ascella: ['', [Validators.min(0)]],
      sottoscapolare: ['', [Validators.min(0)]],
      soprailliaca: ['', [Validators.min(0)]],
      addome: ['', [Validators.min(0)]],
      coscia: ['', [Validators.min(0)]],
      mese: ['', [Validators.required, Validators.min(1), Validators.max(12)]],
      dataInserimento: [dayjs(), [Validators.required]],
    });

    this.circonferenzaForm = this.fb.group({
      torace: ['', [Validators.min(0)]],
      braccio: ['', [Validators.min(0)]],
      avambraccio: ['', [Validators.min(0)]],
      ombelico: ['', [Validators.min(0)]],
      fianchi: ['', [Validators.min(0)]],
      sottoOmbelico: ['', [Validators.min(0)]],
      vita: ['', [Validators.min(0)]],
      coscia: ['', [Validators.min(0)]],
      mese: ['', [Validators.required, Validators.min(1), Validators.max(12)]],
      dataInserimento: [dayjs(), [Validators.required]],
    });
  }

  setActiveTab(tab: string): void {
    if (this.activeTab !== tab) {
      this.activeTab = tab;

      // Caricamento lazy dei dati in base alla tab selezionata
      switch (tab) {
        case 'peso':
          if (this.pesi.length === 0) {
            this.loadPesoData();
          }
          break;
        case 'plicometria':
          if (this.plicometrie.length === 0) {
            this.loadPlicometriaData();
          }
          break;
        case 'circonferenza':
          if (this.circonferenze.length === 0) {
            this.loadCirconferenzaData();
          }
          break;
      }

      this.cdr.markForCheck();
    }
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  ngAfterViewInit(): void {
    // Carica solo i dati della tab iniziale
    this.loadPesoData();
  }

  loadPesoData(): void {
    const idCliente = this.cliente()?.id;
    if (!idCliente) return;

    this.isLoadingPeso = true;
    this.cdr.markForCheck();

    const queryObject = {
      'clienteId.equals': idCliente,
      page: this.pesoPage - 1,
      size: this.itemsPerPage,
      sort: ['dataInserimento,desc'],
    };

    this.pesoSrv.query(queryObject).subscribe({
      next: res => {
        this.pesi = res.body || [];
        this.isLoadingPeso = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.pesi = [];
        this.isLoadingPeso = false;
        this.cdr.markForCheck();
      },
    });
  }

  loadPlicometriaData(): void {
    const idCliente = this.cliente()?.id;
    if (!idCliente) return;

    this.isLoadingPlic = true;
    this.cdr.markForCheck();

    const queryObject = {
      'clienteId.equals': idCliente,
      page: this.plicPage - 1,
      size: this.itemsPerPage,
      sort: ['dataInserimento,desc'],
    };

    this.plicometriaSrv.query(queryObject).subscribe({
      next: res => {
        this.plicometrie = res.body || [];
        this.isLoadingPlic = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.plicometrie = [];
        this.isLoadingPlic = false;
        this.cdr.markForCheck();
      },
    });
  }

  loadCirconferenzaData(): void {
    const idCliente = this.cliente()?.id;
    if (!idCliente) return;

    this.isLoadingCirc = true;
    this.cdr.markForCheck();

    const queryObject = {
      'clienteId.equals': idCliente,
      page: this.circPage - 1,
      size: this.itemsPerPage,
      sort: ['dataInserimento,desc'],
    };

    this.circonferenzaSrv.query(queryObject).subscribe({
      next: res => {
        this.circonferenze = res.body || [];
        this.isLoadingCirc = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.circonferenze = [];
        this.isLoadingCirc = false;
        this.cdr.markForCheck();
      },
    });
  }

  // Funzioni di paginazione
  navigateToPesoPage(page: number): void {
    this.pesoPage = page;
    this.loadPesoData();
  }

  navigateToPlicPage(page: number): void {
    this.plicPage = page;
    this.loadPlicometriaData();
  }

  navigateToCircPage(page: number): void {
    this.circPage = page;
    this.loadCirconferenzaData();
  }

  // Open edit modals
  editPeso(peso: IPesoCliente): void {
    const modalRef = this.modalService.open(PesoClienteUpdateComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pesoCliente = peso;

    modalRef.closed.subscribe(() => {
      this.loadPesoData();
    });
  }

  editPlicometria(plicometria: IPlicometria): void {
    const modalRef = this.modalService.open(PlicometriaUpdateComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.plicometria = plicometria;

    modalRef.closed.subscribe(() => {
      this.loadPlicometriaData();
    });
  }

  editCirconferenza(circonferenza: ICirconferenza): void {
    const modalRef = this.modalService.open(CirconferenzaUpdateComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.circonferenza = circonferenza;

    modalRef.closed.subscribe(() => {
      this.loadCirconferenzaData();
    });
  }

  // Delete operations
  deletePeso(peso: IPesoCliente): void {
    const modalRef = this.modalService.open(PesoClienteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pesoCliente = peso;

    modalRef.closed.pipe(filter((reason: string) => reason === ITEM_DELETED_EVENT)).subscribe(() => {
      this.loadPesoData();
    });
  }

  deletePlicometria(plicometria: IPlicometria): void {
    const modalRef = this.modalService.open(PlicometriaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.plicometria = plicometria;

    modalRef.closed.pipe(filter((reason: string) => reason === ITEM_DELETED_EVENT)).subscribe(() => {
      this.loadPlicometriaData();
    });
  }

  deleteCirconferenza(circonferenza: ICirconferenza): void {
    const modalRef = this.modalService.open(CirconferenzaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.circonferenza = circonferenza;

    modalRef.closed.pipe(filter((reason: string) => reason === ITEM_DELETED_EVENT)).subscribe(() => {
      this.loadCirconferenzaData();
    });
  }

  // Quick add methods
  addNewPeso(): void {
    if (this.pesoForm.invalid) return;

    const newPeso: NewPesoCliente = {
      id: null,
      ...this.pesoForm.value,
      cliente: {
        id: this.cliente()?.id,
        email: this.cliente()?.email,
      },
    };

    this.isLoadingPeso = true;
    this.cdr.markForCheck();

    this.pesoSrv.create(newPeso).subscribe({
      next: () => {
        this.pesoForm.reset({
          peso: '',
          mese: '',
          dataInserimento: dayjs(),
        });
        this.loadPesoData();
      },
      error: () => {
        this.isLoadingPeso = false;
        this.cdr.markForCheck();
      },
    });
  }

  addNewPlicometria(): void {
    if (this.plicometriaForm.invalid) return;

    const newPlicometria: NewPlicometria = {
      id: null,
      ...this.plicometriaForm.value,
      cliente: {
        id: this.cliente()?.id,
        email: this.cliente()?.email,
      },
    };

    this.isLoadingPlic = true;
    this.cdr.markForCheck();

    this.plicometriaSrv.create(newPlicometria).subscribe({
      next: () => {
        this.plicometriaForm.reset({
          tricipite: '',
          petto: '',
          ascella: '',
          sottoscapolare: '',
          soprailliaca: '',
          addome: '',
          coscia: '',
          mese: '',
          dataInserimento: dayjs(),
        });
        this.loadPlicometriaData();
      },
      error: () => {
        this.isLoadingPlic = false;
        this.cdr.markForCheck();
      },
    });
  }

  addNewCirconferenza(): void {
    if (this.circonferenzaForm.invalid) return;

    const newCirconferenza: NewCirconferenza = {
      id: null,
      ...this.circonferenzaForm.value,
      cliente: {
        id: this.cliente()?.id,
        email: this.cliente()?.email,
      },
    };

    this.isLoadingCirc = true;
    this.cdr.markForCheck();

    this.circonferenzaSrv.create(newCirconferenza).subscribe({
      next: () => {
        this.circonferenzaForm.reset({
          torace: '',
          braccio: '',
          avambraccio: '',
          ombelico: '',
          fianchi: '',
          sottoOmbelico: '',
          vita: '',
          coscia: '',
          mese: '',
          dataInserimento: dayjs(),
        });
        this.loadCirconferenzaData();
      },
      error: () => {
        this.isLoadingCirc = false;
        this.cdr.markForCheck();
      },
    });
  }
}
