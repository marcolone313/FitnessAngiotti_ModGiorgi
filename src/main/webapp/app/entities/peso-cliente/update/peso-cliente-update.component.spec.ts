import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { PesoClienteService } from '../service/peso-cliente.service';
import { IPesoCliente } from '../peso-cliente.model';
import { PesoClienteFormService } from './peso-cliente-form.service';

import { PesoClienteUpdateComponent } from './peso-cliente-update.component';

describe('PesoCliente Management Update Component', () => {
  let comp: PesoClienteUpdateComponent;
  let fixture: ComponentFixture<PesoClienteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pesoClienteFormService: PesoClienteFormService;
  let pesoClienteService: PesoClienteService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PesoClienteUpdateComponent],
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
      .overrideTemplate(PesoClienteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PesoClienteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pesoClienteFormService = TestBed.inject(PesoClienteFormService);
    pesoClienteService = TestBed.inject(PesoClienteService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cliente query and add missing value', () => {
      const pesoCliente: IPesoCliente = { id: 456 };
      const cliente: ICliente = { id: 31100 };
      pesoCliente.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 24737 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pesoCliente });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pesoCliente: IPesoCliente = { id: 456 };
      const cliente: ICliente = { id: 22866 };
      pesoCliente.cliente = cliente;

      activatedRoute.data = of({ pesoCliente });
      comp.ngOnInit();

      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.pesoCliente).toEqual(pesoCliente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPesoCliente>>();
      const pesoCliente = { id: 123 };
      jest.spyOn(pesoClienteFormService, 'getPesoCliente').mockReturnValue(pesoCliente);
      jest.spyOn(pesoClienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pesoCliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pesoCliente }));
      saveSubject.complete();

      // THEN
      expect(pesoClienteFormService.getPesoCliente).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pesoClienteService.update).toHaveBeenCalledWith(expect.objectContaining(pesoCliente));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPesoCliente>>();
      const pesoCliente = { id: 123 };
      jest.spyOn(pesoClienteFormService, 'getPesoCliente').mockReturnValue({ id: null });
      jest.spyOn(pesoClienteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pesoCliente: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pesoCliente }));
      saveSubject.complete();

      // THEN
      expect(pesoClienteFormService.getPesoCliente).toHaveBeenCalled();
      expect(pesoClienteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPesoCliente>>();
      const pesoCliente = { id: 123 };
      jest.spyOn(pesoClienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pesoCliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pesoClienteService.update).toHaveBeenCalled();
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
