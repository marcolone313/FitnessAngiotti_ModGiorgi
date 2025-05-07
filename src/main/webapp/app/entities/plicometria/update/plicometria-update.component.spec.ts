import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { PlicometriaService } from '../service/plicometria.service';
import { IPlicometria } from '../plicometria.model';
import { PlicometriaFormService } from './plicometria-form.service';

import { PlicometriaUpdateComponent } from './plicometria-update.component';

describe('Plicometria Management Update Component', () => {
  let comp: PlicometriaUpdateComponent;
  let fixture: ComponentFixture<PlicometriaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let plicometriaFormService: PlicometriaFormService;
  let plicometriaService: PlicometriaService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PlicometriaUpdateComponent],
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
      .overrideTemplate(PlicometriaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlicometriaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    plicometriaFormService = TestBed.inject(PlicometriaFormService);
    plicometriaService = TestBed.inject(PlicometriaService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cliente query and add missing value', () => {
      const plicometria: IPlicometria = { id: 456 };
      const cliente: ICliente = { id: 6913 };
      plicometria.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 15033 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ plicometria });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const plicometria: IPlicometria = { id: 456 };
      const cliente: ICliente = { id: 5765 };
      plicometria.cliente = cliente;

      activatedRoute.data = of({ plicometria });
      comp.ngOnInit();

      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.plicometria).toEqual(plicometria);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlicometria>>();
      const plicometria = { id: 123 };
      jest.spyOn(plicometriaFormService, 'getPlicometria').mockReturnValue(plicometria);
      jest.spyOn(plicometriaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plicometria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: plicometria }));
      saveSubject.complete();

      // THEN
      expect(plicometriaFormService.getPlicometria).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(plicometriaService.update).toHaveBeenCalledWith(expect.objectContaining(plicometria));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlicometria>>();
      const plicometria = { id: 123 };
      jest.spyOn(plicometriaFormService, 'getPlicometria').mockReturnValue({ id: null });
      jest.spyOn(plicometriaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plicometria: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: plicometria }));
      saveSubject.complete();

      // THEN
      expect(plicometriaFormService.getPlicometria).toHaveBeenCalled();
      expect(plicometriaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlicometria>>();
      const plicometria = { id: 123 };
      jest.spyOn(plicometriaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plicometria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(plicometriaService.update).toHaveBeenCalled();
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
