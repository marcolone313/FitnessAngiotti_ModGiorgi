import { AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, inject, input } from '@angular/core';
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
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import dayjs from 'dayjs/esm';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { PesoClienteDeleteDialogComponent } from 'app/entities/peso-cliente/delete/peso-cliente-delete-dialog.component';
import { PlicometriaDeleteDialogComponent } from 'app/entities/plicometria/delete/plicometria-delete-dialog.component';
import { CirconferenzaDeleteDialogComponent } from 'app/entities/circonferenza/delete/circonferenza-delete-dialog.component';
import { SchedaSettimanaleService } from 'app/entities/scheda-settimanale/service/scheda-settimanale.service';
import { ISchedaSettimanale } from 'app/entities/scheda-settimanale/scheda-settimanale.model';
import { ReportSettimanaleService } from 'app/entities/report-settimanale/service/report-settimanale.service';
import { IReportSettimanale } from 'app/entities/report-settimanale/report-settimanale.model';
import { SchedaSettimanaleDeleteDialogComponent } from 'app/entities/scheda-settimanale/delete/scheda-settimanale-delete-dialog.component';
import { ReportSettimanaleDeleteDialogComponent } from 'app/entities/report-settimanale/delete/report-settimanale-delete-dialog.component';
import { DietaService } from 'app/entities/dieta/service/dieta.service';
import { IDieta } from 'app/entities/dieta/dieta.model';
import { DietaDeleteDialogComponent } from 'app/entities/dieta/delete/dieta-delete-dialog.component';
import { filter, finalize } from 'rxjs/operators';
import { forkJoin, of } from 'rxjs';

@Component({
  standalone: true,
  selector: 'jhi-cliente-detail',
  templateUrl: './cliente-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, ReactiveFormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ClienteDetailComponent implements OnInit, AfterViewInit {
  cliente = input<ICliente | null>(null);
  protected dataUtils = inject(DataUtils);

  // Services
  plicometriaSrv = inject(PlicometriaService);
  pesoSrv = inject(PesoClienteService);
  circonferenzaSrv = inject(CirconferenzaService);
  schedaSettimanaleService = inject(SchedaSettimanaleService);
  reportSettimanaleService = inject(ReportSettimanaleService);
  dietaService = inject(DietaService);
  modalService = inject(NgbModal);
  fb = inject(FormBuilder);
  cdr = inject(ChangeDetectorRef);

  // Data collections
  plicometrie: IPlicometria[] = [];
  pesi: IPesoCliente[] = [];
  circonferenze: ICirconferenza[] = [];
  schedeSettimanali: ISchedaSettimanale[] = [];
  reportSettimanali: IReportSettimanale[] = [];
  diete: IDieta[] = [];

  // Tab control
  activeTab = 'peso';

  // Quick add forms
  private _pesoForm: FormGroup | null = null;
  private _plicometriaForm: FormGroup | null = null;
  private _circonferenzaForm: FormGroup | null = null;

  // Loading states
  isLoadingPeso = false;
  isLoadingPlic = false;
  isLoadingCirc = false;
  isLoadingSchede = false;
  isLoadingReport = false;
  isLoadingDiete = false;

  // Data loading status
  private hasLoadedPeso = false;
  private hasLoadedPlic = false;
  private hasLoadedCirc = false;
  private hasLoadedSchede = false;
  private hasLoadedReport = false;
  private hasLoadedDiete = false;

  // Pagination
  itemsPerPage = 10;
  pesoPage = 1;
  plicPage = 1;
  circPage = 1;
  schedePage = 1;
  reportPage = 1;
  dietaPage = 1;

  // Form getters with lazy initialization
  get pesoForm(): FormGroup {
    if (!this._pesoForm) {
      this._pesoForm = this.fb.group({
        peso: ['', [Validators.required, Validators.min(0)]],
        mese: ['', [Validators.required, Validators.min(1), Validators.max(12)]],
        dataInserimento: [dayjs(), [Validators.required]],
      });
    }
    return this._pesoForm;
  }

  get plicometriaForm(): FormGroup {
    if (!this._plicometriaForm) {
      this._plicometriaForm = this.fb.group({
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
    }
    return this._plicometriaForm;
  }

  get circonferenzaForm(): FormGroup {
    if (!this._circonferenzaForm) {
      this._circonferenzaForm = this.fb.group({
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
    return this._circonferenzaForm;
  }

  ngOnInit(): void {
    // Initialize first tab data
    this.loadInitialData();
  }

  // Load critical data on init, then queue up background loading of other tabs
  loadInitialData(): void {
    // First load the peso data (for the default tab)
    this.loadPesoData();
    this.hasLoadedPeso = true;

    // Schedule the loading of other tab data in the background
    setTimeout(() => {
      this.loadRemainingData();
    }, 300);
  }

  // Load other tab data in the background to improve perceived performance
  loadRemainingData(): void {
    const idCliente = this.cliente()?.id;
    if (!idCliente) return;

    // Create query objects for each data type
    const queryObject = {
      'clienteId.equals': idCliente,
      size: this.itemsPerPage,
      sort: ['id,desc'], // Ordinamento per ID invece che per data
    };

    // Load all data types in parallel using forkJoin
    forkJoin([
      !this.hasLoadedPlic ? this.plicometriaSrv.query({ ...queryObject, page: this.plicPage - 1 }) : of(null),
      !this.hasLoadedCirc ? this.circonferenzaSrv.query({ ...queryObject, page: this.circPage - 1 }) : of(null),
      !this.hasLoadedSchede ? this.schedaSettimanaleService.query({ ...queryObject, page: this.schedePage - 1 }) : of(null),
      !this.hasLoadedReport ? this.reportSettimanaleService.query({ ...queryObject, page: this.reportPage - 1 }) : of(null),
      !this.hasLoadedDiete ? this.dietaService.query({ ...queryObject, page: this.dietaPage - 1 }) : of(null),
    ]).subscribe({
      next: ([plicRes, circRes, schedeRes, reportRes, dieteRes]) => {
        // Process plicometria data if it was loaded
        if (plicRes) {
          this.plicometrie = plicRes.body || [];
          this.hasLoadedPlic = true;
        }

        // Process circonferenza data if it was loaded
        if (circRes) {
          this.circonferenze = circRes.body || [];
          this.hasLoadedCirc = true;
        }

        // Process schede settimanali data if it was loaded
        if (schedeRes) {
          this.schedeSettimanali = schedeRes.body || [];
          this.hasLoadedSchede = true;
        }

        // Process report settimanali data if it was loaded
        if (reportRes) {
          this.reportSettimanali = reportRes.body || [];
          this.hasLoadedReport = true;
        }

        // Process diete data if it was loaded
        if (dieteRes) {
          this.diete = dieteRes.body || [];
          this.hasLoadedDiete = true;
        }

        // Update the view
        this.cdr.markForCheck();
      },
    });
  }

  setActiveTab(tab: string): void {
    if (this.activeTab !== tab) {
      this.activeTab = tab;

      // Load data if not already loaded
      switch (tab) {
        case 'peso':
          if (!this.hasLoadedPeso) {
            this.loadPesoData();
          }
          break;
        case 'plicometria':
          if (!this.hasLoadedPlic) {
            this.loadPlicometriaData();
          }
          break;
        case 'circonferenza':
          if (!this.hasLoadedCirc) {
            this.loadCirconferenzaData();
          }
          break;
        case 'dieta':
          if (!this.hasLoadedDiete) {
            this.loadDieteData();
          }
          break;
        case 'schedaSettimanale':
          if (!this.hasLoadedSchede) {
            this.loadSchedeSettimanaliData();
          }
          break;
        case 'reportSettimanale':
          if (!this.hasLoadedReport) {
            this.loadReportSettimanaliData();
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
    // Initial loading is now done in ngOnInit
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
      sort: ['id,desc'], // Ordinamento per ID invece che per data
    };

    this.pesoSrv
      .query(queryObject)
      .pipe(
        finalize(() => {
          this.isLoadingPeso = false;
          this.hasLoadedPeso = true;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: res => {
          this.pesi = res.body || [];
        },
        error: () => {
          this.pesi = [];
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
      sort: ['id,desc'], // Ordinamento per ID invece che per data
    };

    this.plicometriaSrv
      .query(queryObject)
      .pipe(
        finalize(() => {
          this.isLoadingPlic = false;
          this.hasLoadedPlic = true;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: res => {
          this.plicometrie = res.body || [];
        },
        error: () => {
          this.plicometrie = [];
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
      sort: ['id,desc'], // Ordinamento per ID invece che per data
    };

    this.circonferenzaSrv
      .query(queryObject)
      .pipe(
        finalize(() => {
          this.isLoadingCirc = false;
          this.hasLoadedCirc = true;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: res => {
          this.circonferenze = res.body || [];
        },
        error: () => {
          this.circonferenze = [];
        },
      });
  }

  loadDieteData(): void {
    const idCliente = this.cliente()?.id;
    if (!idCliente) return;

    this.isLoadingDiete = true;
    this.cdr.markForCheck();

    const queryObject = {
      'clienteId.equals': idCliente,
      page: this.dietaPage - 1,
      size: this.itemsPerPage,
      sort: ['id,desc'],
    };

    this.dietaService
      .query(queryObject)
      .pipe(
        finalize(() => {
          this.isLoadingDiete = false;
          this.hasLoadedDiete = true;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: res => {
          this.diete = res.body || [];
        },
        error: () => {
          this.diete = [];
        },
      });
  }

  loadSchedeSettimanaliData(): void {
    const idCliente = this.cliente()?.id;
    if (!idCliente) return;

    this.isLoadingSchede = true;
    this.cdr.markForCheck();

    const queryObject = {
      'clienteId.equals': idCliente,
      page: this.schedePage - 1,
      size: this.itemsPerPage,
      sort: ['id,desc'],
    };

    this.schedaSettimanaleService
      .query(queryObject)
      .pipe(
        finalize(() => {
          this.isLoadingSchede = false;
          this.hasLoadedSchede = true;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: res => {
          this.schedeSettimanali = res.body || [];
        },
        error: () => {
          this.schedeSettimanali = [];
        },
      });
  }

  loadReportSettimanaliData(): void {
    const idCliente = this.cliente()?.id;
    if (!idCliente) return;

    this.isLoadingReport = true;
    this.cdr.markForCheck();

    const queryObject = {
      'clienteId.equals': idCliente,
      page: this.reportPage - 1,
      size: this.itemsPerPage,
      sort: ['id,desc'],
    };

    this.reportSettimanaleService
      .query(queryObject)
      .pipe(
        finalize(() => {
          this.isLoadingReport = false;
          this.hasLoadedReport = true;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: res => {
          this.reportSettimanali = res.body || [];
        },
        error: () => {
          this.reportSettimanali = [];
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

  navigateToDietaPage(page: number): void {
    this.dietaPage = page;
    this.loadDieteData();
  }

  navigateToSchedePage(page: number): void {
    this.schedePage = page;
    this.loadSchedeSettimanaliData();
  }

  navigateToReportPage(page: number): void {
    this.reportPage = page;
    this.loadReportSettimanaliData();
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

  deleteDieta(dieta: IDieta): void {
    const modalRef = this.modalService.open(DietaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dieta = dieta;

    modalRef.closed.pipe(filter((reason: string) => reason === ITEM_DELETED_EVENT)).subscribe(() => {
      this.loadDieteData();
    });
  }

  deleteSchedaSettimanale(schedaSettimanale: ISchedaSettimanale): void {
    const modalRef = this.modalService.open(SchedaSettimanaleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.schedaSettimanale = schedaSettimanale;

    modalRef.closed.pipe(filter((reason: string) => reason === ITEM_DELETED_EVENT)).subscribe(() => {
      this.loadSchedeSettimanaliData();
    });
  }

  deleteReportSettimanale(reportSettimanale: IReportSettimanale): void {
    const modalRef = this.modalService.open(ReportSettimanaleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.reportSettimanale = reportSettimanale;

    modalRef.closed.pipe(filter((reason: string) => reason === ITEM_DELETED_EVENT)).subscribe(() => {
      this.loadReportSettimanaliData();
    });
  }

  // Quick add methods with optimistic updates
  addNewPeso(): void {
    if (this.pesoForm.invalid) return;

    const newPeso: NewPesoCliente = {
      id: null,
      ...this.pesoForm.value,
      cliente: {
        id: this.cliente()?.id || 0,
        email: this.cliente()?.email || '',
      },
    };

    this.isLoadingPeso = true;
    this.cdr.markForCheck();

    this.pesoSrv.create(newPeso).subscribe({
      next: response => {
        // Optimistic update - add the new record to the array
        if (response.body) {
          this.pesi = [response.body, ...this.pesi];
        }

        this.pesoForm.reset({
          peso: '',
          mese: '',
          dataInserimento: dayjs(),
        });
        this.isLoadingPeso = false;
        this.cdr.markForCheck();
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
        id: this.cliente()?.id || 0,
        email: this.cliente()?.email || '',
      },
    };

    this.isLoadingPlic = true;
    this.cdr.markForCheck();

    this.plicometriaSrv.create(newPlicometria).subscribe({
      next: response => {
        // Optimistic update - add the new record to the array
        if (response.body) {
          this.plicometrie = [response.body, ...this.plicometrie];
        }

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
        this.isLoadingPlic = false;
        this.cdr.markForCheck();
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
        id: this.cliente()?.id || 0,
        email: this.cliente()?.email || '',
      },
    };

    this.isLoadingCirc = true;
    this.cdr.markForCheck();

    this.circonferenzaSrv.create(newCirconferenza).subscribe({
      next: response => {
        // Optimistic update - add the new record to the array
        if (response.body) {
          this.circonferenze = [response.body, ...this.circonferenze];
        }

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
        this.isLoadingCirc = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.isLoadingCirc = false;
        this.cdr.markForCheck();
      },
    });
  }
}
