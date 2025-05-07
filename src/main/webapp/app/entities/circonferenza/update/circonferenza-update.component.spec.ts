import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { CirconferenzaService } from '../service/circonferenza.service';
import { ICirconferenza } from '../circonferenza.model';
import { CirconferenzaFormService } from './circonferenza-form.service';

import { CirconferenzaUpdateComponent } from './circonferenza-update.component';

describe('Circonferenza Management Update Component', () => {
  let comp: CirconferenzaUpdateComponent;
  let fixture: ComponentFixture<CirconferenzaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let circonferenzaFormService: CirconferenzaFormService;
  let circonferenzaService: CirconferenzaService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CirconferenzaUpdateComponent],
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
      .overrideTemplate(CirconferenzaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CirconferenzaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    circonferenzaFormService = TestBed.inject(CirconferenzaFormService);
    circonferenzaService = TestBed.inject(CirconferenzaService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cliente query and add missing value', () => {
      const circonferenza: ICirconferenza = { id: 456 };
      const cliente: ICliente = { id: 28927 };
      circonferenza.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 25054 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ circonferenza });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const circonferenza: ICirconferenza = { id: 456 };
      const cliente: ICliente = { id: 32092 };
      circonferenza.cliente = cliente;

      activatedRoute.data = of({ circonferenza });
      comp.ngOnInit();

      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.circonferenza).toEqual(circonferenza);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICirconferenza>>();
      const circonferenza = { id: 123 };
      jest.spyOn(circonferenzaFormService, 'getCirconferenza').mockReturnValue(circonferenza);
      jest.spyOn(circonferenzaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ circonferenza });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: circonferenza }));
      saveSubject.complete();

      // THEN
      expect(circonferenzaFormService.getCirconferenza).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(circonferenzaService.update).toHaveBeenCalledWith(expect.objectContaining(circonferenza));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICirconferenza>>();
      const circonferenza = { id: 123 };
      jest.spyOn(circonferenzaFormService, 'getCirconferenza').mockReturnValue({ id: null });
      jest.spyOn(circonferenzaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ circonferenza: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: circonferenza }));
      saveSubject.complete();

      // THEN
      expect(circonferenzaFormService.getCirconferenza).toHaveBeenCalled();
      expect(circonferenzaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICirconferenza>>();
      const circonferenza = { id: 123 };
      jest.spyOn(circonferenzaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ circonferenza });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(circonferenzaService.update).toHaveBeenCalled();
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
