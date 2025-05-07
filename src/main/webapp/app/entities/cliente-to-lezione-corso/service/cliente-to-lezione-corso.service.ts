import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClienteToLezioneCorso, NewClienteToLezioneCorso } from '../cliente-to-lezione-corso.model';

export type PartialUpdateClienteToLezioneCorso = Partial<IClienteToLezioneCorso> & Pick<IClienteToLezioneCorso, 'id'>;

type RestOf<T extends IClienteToLezioneCorso | NewClienteToLezioneCorso> = Omit<T, 'dataCompletamento'> & {
  dataCompletamento?: string | null;
};

export type RestClienteToLezioneCorso = RestOf<IClienteToLezioneCorso>;

export type NewRestClienteToLezioneCorso = RestOf<NewClienteToLezioneCorso>;

export type PartialUpdateRestClienteToLezioneCorso = RestOf<PartialUpdateClienteToLezioneCorso>;

export type EntityResponseType = HttpResponse<IClienteToLezioneCorso>;
export type EntityArrayResponseType = HttpResponse<IClienteToLezioneCorso[]>;

@Injectable({ providedIn: 'root' })
export class ClienteToLezioneCorsoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cliente-to-lezione-corsos');

  create(clienteToLezioneCorso: NewClienteToLezioneCorso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clienteToLezioneCorso);
    return this.http
      .post<RestClienteToLezioneCorso>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(clienteToLezioneCorso: IClienteToLezioneCorso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clienteToLezioneCorso);
    return this.http
      .put<RestClienteToLezioneCorso>(`${this.resourceUrl}/${this.getClienteToLezioneCorsoIdentifier(clienteToLezioneCorso)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(clienteToLezioneCorso: PartialUpdateClienteToLezioneCorso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clienteToLezioneCorso);
    return this.http
      .patch<RestClienteToLezioneCorso>(`${this.resourceUrl}/${this.getClienteToLezioneCorsoIdentifier(clienteToLezioneCorso)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestClienteToLezioneCorso>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestClienteToLezioneCorso[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getClienteToLezioneCorsoIdentifier(clienteToLezioneCorso: Pick<IClienteToLezioneCorso, 'id'>): number {
    return clienteToLezioneCorso.id;
  }

  compareClienteToLezioneCorso(o1: Pick<IClienteToLezioneCorso, 'id'> | null, o2: Pick<IClienteToLezioneCorso, 'id'> | null): boolean {
    return o1 && o2 ? this.getClienteToLezioneCorsoIdentifier(o1) === this.getClienteToLezioneCorsoIdentifier(o2) : o1 === o2;
  }

  addClienteToLezioneCorsoToCollectionIfMissing<Type extends Pick<IClienteToLezioneCorso, 'id'>>(
    clienteToLezioneCorsoCollection: Type[],
    ...clienteToLezioneCorsosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const clienteToLezioneCorsos: Type[] = clienteToLezioneCorsosToCheck.filter(isPresent);
    if (clienteToLezioneCorsos.length > 0) {
      const clienteToLezioneCorsoCollectionIdentifiers = clienteToLezioneCorsoCollection.map(clienteToLezioneCorsoItem =>
        this.getClienteToLezioneCorsoIdentifier(clienteToLezioneCorsoItem),
      );
      const clienteToLezioneCorsosToAdd = clienteToLezioneCorsos.filter(clienteToLezioneCorsoItem => {
        const clienteToLezioneCorsoIdentifier = this.getClienteToLezioneCorsoIdentifier(clienteToLezioneCorsoItem);
        if (clienteToLezioneCorsoCollectionIdentifiers.includes(clienteToLezioneCorsoIdentifier)) {
          return false;
        }
        clienteToLezioneCorsoCollectionIdentifiers.push(clienteToLezioneCorsoIdentifier);
        return true;
      });
      return [...clienteToLezioneCorsosToAdd, ...clienteToLezioneCorsoCollection];
    }
    return clienteToLezioneCorsoCollection;
  }

  protected convertDateFromClient<T extends IClienteToLezioneCorso | NewClienteToLezioneCorso | PartialUpdateClienteToLezioneCorso>(
    clienteToLezioneCorso: T,
  ): RestOf<T> {
    return {
      ...clienteToLezioneCorso,
      dataCompletamento: clienteToLezioneCorso.dataCompletamento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restClienteToLezioneCorso: RestClienteToLezioneCorso): IClienteToLezioneCorso {
    return {
      ...restClienteToLezioneCorso,
      dataCompletamento: restClienteToLezioneCorso.dataCompletamento ? dayjs(restClienteToLezioneCorso.dataCompletamento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestClienteToLezioneCorso>): HttpResponse<IClienteToLezioneCorso> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestClienteToLezioneCorso[]>): HttpResponse<IClienteToLezioneCorso[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
