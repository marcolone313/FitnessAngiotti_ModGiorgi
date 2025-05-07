import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICircuito, NewCircuito } from '../circuito.model';

export type PartialUpdateCircuito = Partial<ICircuito> & Pick<ICircuito, 'id'>;

export type EntityResponseType = HttpResponse<ICircuito>;
export type EntityArrayResponseType = HttpResponse<ICircuito[]>;

@Injectable({ providedIn: 'root' })
export class CircuitoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/circuitos');

  create(circuito: NewCircuito): Observable<EntityResponseType> {
    return this.http.post<ICircuito>(this.resourceUrl, circuito, { observe: 'response' });
  }

  update(circuito: ICircuito): Observable<EntityResponseType> {
    return this.http.put<ICircuito>(`${this.resourceUrl}/${this.getCircuitoIdentifier(circuito)}`, circuito, { observe: 'response' });
  }

  partialUpdate(circuito: PartialUpdateCircuito): Observable<EntityResponseType> {
    return this.http.patch<ICircuito>(`${this.resourceUrl}/${this.getCircuitoIdentifier(circuito)}`, circuito, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICircuito>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICircuito[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCircuitoIdentifier(circuito: Pick<ICircuito, 'id'>): number {
    return circuito.id;
  }

  compareCircuito(o1: Pick<ICircuito, 'id'> | null, o2: Pick<ICircuito, 'id'> | null): boolean {
    return o1 && o2 ? this.getCircuitoIdentifier(o1) === this.getCircuitoIdentifier(o2) : o1 === o2;
  }

  addCircuitoToCollectionIfMissing<Type extends Pick<ICircuito, 'id'>>(
    circuitoCollection: Type[],
    ...circuitosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const circuitos: Type[] = circuitosToCheck.filter(isPresent);
    if (circuitos.length > 0) {
      const circuitoCollectionIdentifiers = circuitoCollection.map(circuitoItem => this.getCircuitoIdentifier(circuitoItem));
      const circuitosToAdd = circuitos.filter(circuitoItem => {
        const circuitoIdentifier = this.getCircuitoIdentifier(circuitoItem);
        if (circuitoCollectionIdentifiers.includes(circuitoIdentifier)) {
          return false;
        }
        circuitoCollectionIdentifiers.push(circuitoIdentifier);
        return true;
      });
      return [...circuitosToAdd, ...circuitoCollection];
    }
    return circuitoCollection;
  }
}
