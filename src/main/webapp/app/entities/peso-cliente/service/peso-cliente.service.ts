import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPesoCliente, NewPesoCliente } from '../peso-cliente.model';

export type PartialUpdatePesoCliente = Partial<IPesoCliente> & Pick<IPesoCliente, 'id'>;

type RestOf<T extends IPesoCliente | NewPesoCliente> = Omit<T, 'dataInserimento'> & {
  dataInserimento?: string | null;
};

export type RestPesoCliente = RestOf<IPesoCliente>;

export type NewRestPesoCliente = RestOf<NewPesoCliente>;

export type PartialUpdateRestPesoCliente = RestOf<PartialUpdatePesoCliente>;

export type EntityResponseType = HttpResponse<IPesoCliente>;
export type EntityArrayResponseType = HttpResponse<IPesoCliente[]>;

@Injectable({ providedIn: 'root' })
export class PesoClienteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/peso-clientes');

  create(pesoCliente: NewPesoCliente): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pesoCliente);
    return this.http
      .post<RestPesoCliente>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pesoCliente: IPesoCliente): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pesoCliente);
    return this.http
      .put<RestPesoCliente>(`${this.resourceUrl}/${this.getPesoClienteIdentifier(pesoCliente)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pesoCliente: PartialUpdatePesoCliente): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pesoCliente);
    return this.http
      .patch<RestPesoCliente>(`${this.resourceUrl}/${this.getPesoClienteIdentifier(pesoCliente)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPesoCliente>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPesoCliente[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPesoClienteIdentifier(pesoCliente: Pick<IPesoCliente, 'id'>): number {
    return pesoCliente.id;
  }

  comparePesoCliente(o1: Pick<IPesoCliente, 'id'> | null, o2: Pick<IPesoCliente, 'id'> | null): boolean {
    return o1 && o2 ? this.getPesoClienteIdentifier(o1) === this.getPesoClienteIdentifier(o2) : o1 === o2;
  }

  addPesoClienteToCollectionIfMissing<Type extends Pick<IPesoCliente, 'id'>>(
    pesoClienteCollection: Type[],
    ...pesoClientesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pesoClientes: Type[] = pesoClientesToCheck.filter(isPresent);
    if (pesoClientes.length > 0) {
      const pesoClienteCollectionIdentifiers = pesoClienteCollection.map(pesoClienteItem => this.getPesoClienteIdentifier(pesoClienteItem));
      const pesoClientesToAdd = pesoClientes.filter(pesoClienteItem => {
        const pesoClienteIdentifier = this.getPesoClienteIdentifier(pesoClienteItem);
        if (pesoClienteCollectionIdentifiers.includes(pesoClienteIdentifier)) {
          return false;
        }
        pesoClienteCollectionIdentifiers.push(pesoClienteIdentifier);
        return true;
      });
      return [...pesoClientesToAdd, ...pesoClienteCollection];
    }
    return pesoClienteCollection;
  }

  protected convertDateFromClient<T extends IPesoCliente | NewPesoCliente | PartialUpdatePesoCliente>(pesoCliente: T): RestOf<T> {
    return {
      ...pesoCliente,
      dataInserimento: pesoCliente.dataInserimento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPesoCliente: RestPesoCliente): IPesoCliente {
    return {
      ...restPesoCliente,
      dataInserimento: restPesoCliente.dataInserimento ? dayjs(restPesoCliente.dataInserimento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPesoCliente>): HttpResponse<IPesoCliente> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPesoCliente[]>): HttpResponse<IPesoCliente[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
