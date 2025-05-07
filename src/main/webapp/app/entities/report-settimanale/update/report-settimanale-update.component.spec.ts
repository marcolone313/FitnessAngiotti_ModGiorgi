import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISchedaSettimanale } from 'app/entities/scheda-settimanale/scheda-settimanale.model';
import { SchedaSettimanaleService } from 'app/entities/scheda-settimanale/service/scheda-settimanale.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IReportSettimanale } from '../report-settimanale.model';
import { ReportSettimanaleService } from '../service/report-settimanale.service';
import { ReportSettimanaleFormService } from './report-settimanale-form.service';

import { ReportSettimanaleUpdateComponent } from './report-settimanale-update.component';

describe('ReportSettimanale Management Update Component', () => {
  let comp: ReportSettimanaleUpdateComponent;
  let fixture: ComponentFixture<ReportSettimanaleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportSettimanaleFormService: ReportSettimanaleFormService;
  let reportSettimanaleService: ReportSettimanaleService;
  let schedaSettimanaleService: SchedaSettimanaleService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReportSettimanaleUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ReportSettimanaleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportSettimanaleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportSettimanaleFormService = TestBed.inject(ReportSettimanaleFormService);
    reportSettimanaleService = TestBed.inject(ReportSettimanaleService);
    schedaSettimanaleService = TestBed.inject(SchedaSettimanaleService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call schedaSettimanale query and add missing value', () => {
      const reportSettimanale: IReportSettimanale = { id: 456 };
      const schedaSettimanale: ISchedaSettimanale = { id: 8479 };
      reportSettimanale.schedaSettimanale = schedaSettimanale;

      const schedaSettimanaleCollection: ISchedaSettimanale[] = [{ id: 23903 }];
      jest.spyOn(schedaSettimanaleService, 'query').mockReturnValue(of(new HttpResponse({ body: schedaSettimanaleCollection })));
      const expectedCollection: ISchedaSettimanale[] = [schedaSettimanale, ...schedaSettimanaleCollection];
      jest.spyOn(schedaSettimanaleService, 'addSchedaSettimanaleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportSettimanale });
      comp.ngOnInit();

      expect(schedaSettimanaleService.query).toHaveBeenCalled();
      expect(schedaSettimanaleService.addSchedaSettimanaleToCollectionIfMissing).toHaveBeenCalledWith(
        schedaSettimanaleCollection,
        schedaSettimanale,
      );
      expect(comp.schedaSettimanalesCollection).toEqual(expectedCollection);
    });

    it('Should call Cliente query and add missing value', () => {
      const reportSettimanale: IReportSettimanale = { id: 456 };
      const cliente: ICliente = { id: 2830 };
      reportSettimanale.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 2420 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportSettimanale });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reportSettimanale: IReportSettimanale = { id: 456 };
      const schedaSettimanale: ISchedaSettimanale = { id: 10192 };
      reportSettimanale.schedaSettimanale = schedaSettimanale;
      const cliente: ICliente = { id: 14789 };
      reportSettimanale.cliente = cliente;

      activatedRoute.data = of({ reportSettimanale });
      comp.ngOnInit();

      expect(comp.schedaSettimanalesCollection).toContain(schedaSettimanale);
      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.reportSettimanale).toEqual(reportSettimanale);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportSettimanale>>();
      const reportSettimanale = { id: 123 };
      jest.spyOn(reportSettimanaleFormService, 'getReportSettimanale').mockReturnValue(reportSettimanale);
      jest.spyOn(reportSettimanaleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportSettimanale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportSettimanale }));
      saveSubject.complete();

      // THEN
      expect(reportSettimanaleFormService.getReportSettimanale).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportSettimanaleService.update).toHaveBeenCalledWith(expect.objectContaining(reportSettimanale));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportSettimanale>>();
      const reportSettimanale = { id: 123 };
      jest.spyOn(reportSettimanaleFormService, 'getReportSettimanale').mockReturnValue({ id: null });
      jest.spyOn(reportSettimanaleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportSettimanale: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportSettimanale }));
      saveSubject.complete();

      // THEN
      expect(reportSettimanaleFormService.getReportSettimanale).toHaveBeenCalled();
      expect(reportSettimanaleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportSettimanale>>();
      const reportSettimanale = { id: 123 };
      jest.spyOn(reportSettimanaleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportSettimanale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportSettimanaleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSchedaSettimanale', () => {
      it('Should forward to schedaSettimanaleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(schedaSettimanaleService, 'compareSchedaSettimanale');
        comp.compareSchedaSettimanale(entity, entity2);
        expect(schedaSettimanaleService.compareSchedaSettimanale).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCliente', () => {
      it('Should forward to clienteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clienteService, 'compareCliente');
        comp.compareCliente(entity, entity2);
        expect(clienteService.compareCliente).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
