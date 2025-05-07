import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPassaggioEsercizio, NewPassaggioEsercizio } from '../passaggio-esercizio.model';

export type PartialUpdatePassaggioEsercizio = Partial<IPassaggioEsercizio> & Pick<IPassaggioEsercizio, 'id'>;

export type EntityResponseType = HttpResponse<IPassaggioEsercizio>;
export type EntityArrayResponseType = HttpResponse<IPassaggioEsercizio[]>;

@Injectable({ providedIn: 'root' })
export class PassaggioEsercizioService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/passaggio-esercizios');

  create(passaggioEsercizio: NewPassaggioEsercizio): Observable<EntityResponseType> {
    return this.http.post<IPassaggioEsercizio>(this.resourceUrl, passaggioEsercizio, { observe: 'response' });
  }

  update(passaggioEsercizio: IPassaggioEsercizio): Observable<EntityResponseType> {
    return this.http.put<IPassaggioEsercizio>(
      `${this.resourceUrl}/${this.getPassaggioEsercizioIdentifier(passaggioEsercizio)}`,
      passaggioEsercizio,
      { observe: 'response' },
    );
  }

  partialUpdate(passaggioEsercizio: PartialUpdatePassaggioEsercizio): Observable<EntityResponseType> {
    return this.http.patch<IPassaggioEsercizio>(
      `${this.resourceUrl}/${this.getPassaggioEsercizioIdentifier(passaggioEsercizio)}`,
      passaggioEsercizio,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPassaggioEsercizio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPassaggioEsercizio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPassaggioEsercizioIdentifier(passaggioEsercizio: Pick<IPassaggioEsercizio, 'id'>): number {
    return passaggioEsercizio.id;
  }

  comparePassaggioEsercizio(o1: Pick<IPassaggioEsercizio, 'id'> | null, o2: Pick<IPassaggioEsercizio, 'id'> | null): boolean {
    return o1 && o2 ? this.getPassaggioEsercizioIdentifier(o1) === this.getPassaggioEsercizioIdentifier(o2) : o1 === o2;
  }

  addPassaggioEsercizioToCollectionIfMissing<Type extends Pick<IPassaggioEsercizio, 'id'>>(
    passaggioEsercizioCollection: Type[],
    ...passaggioEserciziosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const passaggioEsercizios: Type[] = passaggioEserciziosToCheck.filter(isPresent);
    if (passaggioEsercizios.length > 0) {
      const passaggioEsercizioCollectionIdentifiers = passaggioEsercizioCollection.map(passaggioEsercizioItem =>
        this.getPassaggioEsercizioIdentifier(passaggioEsercizioItem),
      );
      const passaggioEserciziosToAdd = passaggioEsercizios.filter(passaggioEsercizioItem => {
        const passaggioEsercizioIdentifier = this.getPassaggioEsercizioIdentifier(passaggioEsercizioItem);
        if (passaggioEsercizioCollectionIdentifiers.includes(passaggioEsercizioIdentifier)) {
          return false;
        }
        passaggioEsercizioCollectionIdentifiers.push(passaggioEsercizioIdentifier);
        return true;
      });
      return [...passaggioEserciziosToAdd, ...passaggioEsercizioCollection];
    }
    return passaggioEsercizioCollection;
  }
}
