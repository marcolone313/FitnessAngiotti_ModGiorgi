import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICircuitoToEsercizio, NewCircuitoToEsercizio } from '../circuito-to-esercizio.model';

export type PartialUpdateCircuitoToEsercizio = Partial<ICircuitoToEsercizio> & Pick<ICircuitoToEsercizio, 'id'>;

export type EntityResponseType = HttpResponse<ICircuitoToEsercizio>;
export type EntityArrayResponseType = HttpResponse<ICircuitoToEsercizio[]>;

@Injectable({ providedIn: 'root' })
export class CircuitoToEsercizioService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/circuito-to-esercizios');

  create(circuitoToEsercizio: NewCircuitoToEsercizio): Observable<EntityResponseType> {
    return this.http.post<ICircuitoToEsercizio>(this.resourceUrl, circuitoToEsercizio, { observe: 'response' });
  }

  update(circuitoToEsercizio: ICircuitoToEsercizio): Observable<EntityResponseType> {
    return this.http.put<ICircuitoToEsercizio>(
      `${this.resourceUrl}/${this.getCircuitoToEsercizioIdentifier(circuitoToEsercizio)}`,
      circuitoToEsercizio,
      { observe: 'response' },
    );
  }

  partialUpdate(circuitoToEsercizio: PartialUpdateCircuitoToEsercizio): Observable<EntityResponseType> {
    return this.http.patch<ICircuitoToEsercizio>(
      `${this.resourceUrl}/${this.getCircuitoToEsercizioIdentifier(circuitoToEsercizio)}`,
      circuitoToEsercizio,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICircuitoToEsercizio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICircuitoToEsercizio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCircuitoToEsercizioIdentifier(circuitoToEsercizio: Pick<ICircuitoToEsercizio, 'id'>): number {
    return circuitoToEsercizio.id;
  }

  compareCircuitoToEsercizio(o1: Pick<ICircuitoToEsercizio, 'id'> | null, o2: Pick<ICircuitoToEsercizio, 'id'> | null): boolean {
    return o1 && o2 ? this.getCircuitoToEsercizioIdentifier(o1) === this.getCircuitoToEsercizioIdentifier(o2) : o1 === o2;
  }

  addCircuitoToEsercizioToCollectionIfMissing<Type extends Pick<ICircuitoToEsercizio, 'id'>>(
    circuitoToEsercizioCollection: Type[],
    ...circuitoToEserciziosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const circuitoToEsercizios: Type[] = circuitoToEserciziosToCheck.filter(isPresent);
    if (circuitoToEsercizios.length > 0) {
      const circuitoToEsercizioCollectionIdentifiers = circuitoToEsercizioCollection.map(circuitoToEsercizioItem =>
        this.getCircuitoToEsercizioIdentifier(circuitoToEsercizioItem),
      );
      const circuitoToEserciziosToAdd = circuitoToEsercizios.filter(circuitoToEsercizioItem => {
        const circuitoToEsercizioIdentifier = this.getCircuitoToEsercizioIdentifier(circuitoToEsercizioItem);
        if (circuitoToEsercizioCollectionIdentifiers.includes(circuitoToEsercizioIdentifier)) {
          return false;
        }
        circuitoToEsercizioCollectionIdentifiers.push(circuitoToEsercizioIdentifier);
        return true;
      });
      return [...circuitoToEserciziosToAdd, ...circuitoToEsercizioCollection];
    }
    return circuitoToEsercizioCollection;
  }
}
