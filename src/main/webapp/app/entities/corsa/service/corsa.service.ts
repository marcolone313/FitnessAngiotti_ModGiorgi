import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICorsa, NewCorsa } from '../corsa.model';

export type PartialUpdateCorsa = Partial<ICorsa> & Pick<ICorsa, 'id'>;

export type EntityResponseType = HttpResponse<ICorsa>;
export type EntityArrayResponseType = HttpResponse<ICorsa[]>;

@Injectable({ providedIn: 'root' })
export class CorsaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/corsas');

  create(corsa: NewCorsa): Observable<EntityResponseType> {
    return this.http.post<ICorsa>(this.resourceUrl, corsa, { observe: 'response' });
  }

  update(corsa: ICorsa): Observable<EntityResponseType> {
    return this.http.put<ICorsa>(`${this.resourceUrl}/${this.getCorsaIdentifier(corsa)}`, corsa, { observe: 'response' });
  }

  partialUpdate(corsa: PartialUpdateCorsa): Observable<EntityResponseType> {
    return this.http.patch<ICorsa>(`${this.resourceUrl}/${this.getCorsaIdentifier(corsa)}`, corsa, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICorsa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICorsa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCorsaIdentifier(corsa: Pick<ICorsa, 'id'>): number {
    return corsa.id;
  }

  compareCorsa(o1: Pick<ICorsa, 'id'> | null, o2: Pick<ICorsa, 'id'> | null): boolean {
    return o1 && o2 ? this.getCorsaIdentifier(o1) === this.getCorsaIdentifier(o2) : o1 === o2;
  }

  addCorsaToCollectionIfMissing<Type extends Pick<ICorsa, 'id'>>(
    corsaCollection: Type[],
    ...corsasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const corsas: Type[] = corsasToCheck.filter(isPresent);
    if (corsas.length > 0) {
      const corsaCollectionIdentifiers = corsaCollection.map(corsaItem => this.getCorsaIdentifier(corsaItem));
      const corsasToAdd = corsas.filter(corsaItem => {
        const corsaIdentifier = this.getCorsaIdentifier(corsaItem);
        if (corsaCollectionIdentifiers.includes(corsaIdentifier)) {
          return false;
        }
        corsaCollectionIdentifiers.push(corsaIdentifier);
        return true;
      });
      return [...corsasToAdd, ...corsaCollection];
    }
    return corsaCollection;
  }
}
