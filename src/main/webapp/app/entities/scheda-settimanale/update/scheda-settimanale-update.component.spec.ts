import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { SchedaSettimanaleService } from '../service/scheda-settimanale.service';
import { ISchedaSettimanale } from '../scheda-settimanale.model';
import { SchedaSettimanaleFormService } from './scheda-settimanale-form.service';

import { SchedaSettimanaleUpdateComponent } from './scheda-settimanale-update.component';

describe('SchedaSettimanale Management Update Component', () => {
  let comp: SchedaSettimanaleUpdateComponent;
  let fixture: ComponentFixture<SchedaSettimanaleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let schedaSettimanaleFormService: SchedaSettimanaleFormService;
  let schedaSettimanaleService: SchedaSettimanaleService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SchedaSettimanaleUpdateComponent],
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
      .overrideTemplate(SchedaSettimanaleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SchedaSettimanaleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    schedaSettimanaleFormService = TestBed.inject(SchedaSettimanaleFormService);
    schedaSettimanaleService = TestBed.inject(SchedaSettimanaleService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cliente query and add missing value', () => {
      const schedaSettimanale: ISchedaSettimanale = { id: 456 };
      const cliente: ICliente = { id: 32118 };
      schedaSettimanale.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 6919 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ schedaSettimanale });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const schedaSettimanale: ISchedaSettimanale = { id: 456 };
      const cliente: ICliente = { id: 19134 };
      schedaSettimanale.cliente = cliente;

      activatedRoute.data = of({ schedaSettimanale });
      comp.ngOnInit();

      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.schedaSettimanale).toEqual(schedaSettimanale);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISchedaSettimanale>>();
      const schedaSettimanale = { id: 123 };
      jest.spyOn(schedaSettimanaleFormService, 'getSchedaSettimanale').mockReturnValue(schedaSettimanale);
      jest.spyOn(schedaSettimanaleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ schedaSettimanale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: schedaSettimanale }));
      saveSubject.complete();

      // THEN
      expect(schedaSettimanaleFormService.getSchedaSettimanale).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(schedaSettimanaleService.update).toHaveBeenCalledWith(expect.objectContaining(schedaSettimanale));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISchedaSettimanale>>();
      const schedaSettimanale = { id: 123 };
      jest.spyOn(schedaSettimanaleFormService, 'getSchedaSettimanale').mockReturnValue({ id: null });
      jest.spyOn(schedaSettimanaleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ schedaSettimanale: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: schedaSettimanale }));
      saveSubject.complete();

      // THEN
      expect(schedaSettimanaleFormService.getSchedaSettimanale).toHaveBeenCalled();
      expect(schedaSettimanaleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISchedaSettimanale>>();
      const schedaSettimanale = { id: 123 };
      jest.spyOn(schedaSettimanaleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ schedaSettimanale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(schedaSettimanaleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
