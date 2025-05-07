import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { DietaService } from '../service/dieta.service';
import { IDieta } from '../dieta.model';
import { DietaFormService } from './dieta-form.service';

import { DietaUpdateComponent } from './dieta-update.component';

describe('Dieta Management Update Component', () => {
  let comp: DietaUpdateComponent;
  let fixture: ComponentFixture<DietaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dietaFormService: DietaFormService;
  let dietaService: DietaService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DietaUpdateComponent],
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
      .overrideTemplate(DietaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DietaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dietaFormService = TestBed.inject(DietaFormService);
    dietaService = TestBed.inject(DietaService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cliente query and add missing value', () => {
      const dieta: IDieta = { id: 456 };
      const cliente: ICliente = { id: 960 };
      dieta.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 8180 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dieta });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dieta: IDieta = { id: 456 };
      const cliente: ICliente = { id: 18625 };
      dieta.cliente = cliente;

      activatedRoute.data = of({ dieta });
      comp.ngOnInit();

      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.dieta).toEqual(dieta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDieta>>();
      const dieta = { id: 123 };
      jest.spyOn(dietaFormService, 'getDieta').mockReturnValue(dieta);
      jest.spyOn(dietaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dieta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dieta }));
      saveSubject.complete();

      // THEN
      expect(dietaFormService.getDieta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dietaService.update).toHaveBeenCalledWith(expect.objectContaining(dieta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDieta>>();
      const dieta = { id: 123 };
      jest.spyOn(dietaFormService, 'getDieta').mockReturnValue({ id: null });
      jest.spyOn(dietaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dieta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dieta }));
      saveSubject.complete();

      // THEN
      expect(dietaFormService.getDieta).toHaveBeenCalled();
      expect(dietaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDieta>>();
      const dieta = { id: 123 };
      jest.spyOn(dietaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dieta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dietaService.update).toHaveBeenCalled();
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
