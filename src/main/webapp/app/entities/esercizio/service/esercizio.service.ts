import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEsercizio, NewEsercizio } from '../esercizio.model';

export type PartialUpdateEsercizio = Partial<IEsercizio> & Pick<IEsercizio, 'id'>;

export type EntityResponseType = HttpResponse<IEsercizio>;
export type EntityArrayResponseType = HttpResponse<IEsercizio[]>;

@Injectable({ providedIn: 'root' })
export class EsercizioService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/esercizios');

  create(esercizio: NewEsercizio): Observable<EntityResponseType> {
    return this.http.post<IEsercizio>(this.resourceUrl, esercizio, { observe: 'response' });
  }

  update(esercizio: IEsercizio): Observable<EntityResponseType> {
    return this.http.put<IEsercizio>(`${this.resourceUrl}/${this.getEsercizioIdentifier(esercizio)}`, esercizio, { observe: 'response' });
  }

  partialUpdate(esercizio: PartialUpdateEsercizio): Observable<EntityResponseType> {
    return this.http.patch<IEsercizio>(`${this.resourceUrl}/${this.getEsercizioIdentifier(esercizio)}`, esercizio, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEsercizio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEsercizio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEsercizioIdentifier(esercizio: Pick<IEsercizio, 'id'>): number {
    return esercizio.id;
  }

  compareEsercizio(o1: Pick<IEsercizio, 'id'> | null, o2: Pick<IEsercizio, 'id'> | null): boolean {
    return o1 && o2 ? this.getEsercizioIdentifier(o1) === this.getEsercizioIdentifier(o2) : o1 === o2;
  }

  addEsercizioToCollectionIfMissing<Type extends Pick<IEsercizio, 'id'>>(
    esercizioCollection: Type[],
    ...eserciziosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const esercizios: Type[] = eserciziosToCheck.filter(isPresent);
    if (esercizios.length > 0) {
      const esercizioCollectionIdentifiers = esercizioCollection.map(esercizioItem => this.getEsercizioIdentifier(esercizioItem));
      const eserciziosToAdd = esercizios.filter(esercizioItem => {
        const esercizioIdentifier = this.getEsercizioIdentifier(esercizioItem);
        if (esercizioCollectionIdentifiers.includes(esercizioIdentifier)) {
          return false;
        }
        esercizioCollectionIdentifiers.push(esercizioIdentifier);
        return true;
      });
      return [...eserciziosToAdd, ...esercizioCollection];
    }
    return esercizioCollection;
  }
}
