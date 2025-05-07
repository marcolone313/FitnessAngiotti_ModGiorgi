import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ILezioneCorso } from 'app/entities/lezione-corso/lezione-corso.model';
import { LezioneCorsoService } from 'app/entities/lezione-corso/service/lezione-corso.service';
import { IClienteToLezioneCorso } from '../cliente-to-lezione-corso.model';
import { ClienteToLezioneCorsoService } from '../service/cliente-to-lezione-corso.service';
import { ClienteToLezioneCorsoFormService } from './cliente-to-lezione-corso-form.service';

import { ClienteToLezioneCorsoUpdateComponent } from './cliente-to-lezione-corso-update.component';

describe('ClienteToLezioneCorso Management Update Component', () => {
  let comp: ClienteToLezioneCorsoUpdateComponent;
  let fixture: ComponentFixture<ClienteToLezioneCorsoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clienteToLezioneCorsoFormService: ClienteToLezioneCorsoFormService;
  let clienteToLezioneCorsoService: ClienteToLezioneCorsoService;
  let clienteService: ClienteService;
  let lezioneCorsoService: LezioneCorsoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ClienteToLezioneCorsoUpdateComponent],
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
      .overrideTemplate(ClienteToLezioneCorsoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClienteToLezioneCorsoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clienteToLezioneCorsoFormService = TestBed.inject(ClienteToLezioneCorsoFormService);
    clienteToLezioneCorsoService = TestBed.inject(ClienteToLezioneCorsoService);
    clienteService = TestBed.inject(ClienteService);
    lezioneCorsoService = TestBed.inject(LezioneCorsoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cliente query and add missing value', () => {
      const clienteToLezioneCorso: IClienteToLezioneCorso = { id: 456 };
      const cliente: ICliente = { id: 1758 };
      clienteToLezioneCorso.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 8344 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ clienteToLezioneCorso });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LezioneCorso query and add missing value', () => {
      const clienteToLezioneCorso: IClienteToLezioneCorso = { id: 456 };
      const lezioneCorso: ILezioneCorso = { id: 16039 };
      clienteToLezioneCorso.lezioneCorso = lezioneCorso;

      const lezioneCorsoCollection: ILezioneCorso[] = [{ id: 2913 }];
      jest.spyOn(lezioneCorsoService, 'query').mockReturnValue(of(new HttpResponse({ body: lezioneCorsoCollection })));
      const additionalLezioneCorsos = [lezioneCorso];
      const expectedCollection: ILezioneCorso[] = [...additionalLezioneCorsos, ...lezioneCorsoCollection];
      jest.spyOn(lezioneCorsoService, 'addLezioneCorsoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ clienteToLezioneCorso });
      comp.ngOnInit();

      expect(lezioneCorsoService.query).toHaveBeenCalled();
      expect(lezioneCorsoService.addLezioneCorsoToCollectionIfMissing).toHaveBeenCalledWith(
        lezioneCorsoCollection,
        ...additionalLezioneCorsos.map(expect.objectContaining),
      );
      expect(comp.lezioneCorsosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const clienteToLezioneCorso: IClienteToLezioneCorso = { id: 456 };
      const cliente: ICliente = { id: 24560 };
      clienteToLezioneCorso.cliente = cliente;
      const lezioneCorso: ILezioneCorso = { id: 30029 };
      clienteToLezioneCorso.lezioneCorso = lezioneCorso;

      activatedRoute.data = of({ clienteToLezioneCorso });
      comp.ngOnInit();

      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.lezioneCorsosSharedCollection).toContain(lezioneCorso);
      expect(comp.clienteToLezioneCorso).toEqual(clienteToLezioneCorso);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClienteToLezioneCorso>>();
      const clienteToLezioneCorso = { id: 123 };
      jest.spyOn(clienteToLezioneCorsoFormService, 'getClienteToLezioneCorso').mockReturnValue(clienteToLezioneCorso);
      jest.spyOn(clienteToLezioneCorsoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clienteToLezioneCorso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clienteToLezioneCorso }));
      saveSubject.complete();

      // THEN
      expect(clienteToLezioneCorsoFormService.getClienteToLezioneCorso).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(clienteToLezioneCorsoService.update).toHaveBeenCalledWith(expect.objectContaining(clienteToLezioneCorso));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClienteToLezioneCorso>>();
      const clienteToLezioneCorso = { id: 123 };
      jest.spyOn(clienteToLezioneCorsoFormService, 'getClienteToLezioneCorso').mockReturnValue({ id: null });
      jest.spyOn(clienteToLezioneCorsoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clienteToLezioneCorso: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: clienteToLezioneCorso }));
      saveSubject.complete();

      // THEN
      expect(clienteToLezioneCorsoFormService.getClienteToLezioneCorso).toHaveBeenCalled();
      expect(clienteToLezioneCorsoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClienteToLezioneCorso>>();
      const clienteToLezioneCorso = { id: 123 };
      jest.spyOn(clienteToLezioneCorsoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ clienteToLezioneCorso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clienteToLezioneCorsoService.update).toHaveBeenCalled();
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

    describe('compareLezioneCorso', () => {
      it('Should forward to lezioneCorsoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(lezioneCorsoService, 'compareLezioneCorso');
        comp.compareLezioneCorso(entity, entity2);
        expect(lezioneCorsoService.compareLezioneCorso).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
