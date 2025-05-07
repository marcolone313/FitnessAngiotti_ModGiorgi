import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILezioneCorso, NewLezioneCorso } from '../lezione-corso.model';

export type PartialUpdateLezioneCorso = Partial<ILezioneCorso> & Pick<ILezioneCorso, 'id'>;

export type EntityResponseType = HttpResponse<ILezioneCorso>;
export type EntityArrayResponseType = HttpResponse<ILezioneCorso[]>;

@Injectable({ providedIn: 'root' })
export class LezioneCorsoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lezione-corsos');

  create(lezioneCorso: NewLezioneCorso): Observable<EntityResponseType> {
    return this.http.post<ILezioneCorso>(this.resourceUrl, lezioneCorso, { observe: 'response' });
  }

  update(lezioneCorso: ILezioneCorso): Observable<EntityResponseType> {
    return this.http.put<ILezioneCorso>(`${this.resourceUrl}/${this.getLezioneCorsoIdentifier(lezioneCorso)}`, lezioneCorso, {
      observe: 'response',
    });
  }

  partialUpdate(lezioneCorso: PartialUpdateLezioneCorso): Observable<EntityResponseType> {
    return this.http.patch<ILezioneCorso>(`${this.resourceUrl}/${this.getLezioneCorsoIdentifier(lezioneCorso)}`, lezioneCorso, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILezioneCorso>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILezioneCorso[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLezioneCorsoIdentifier(lezioneCorso: Pick<ILezioneCorso, 'id'>): number {
    return lezioneCorso.id;
  }

  compareLezioneCorso(o1: Pick<ILezioneCorso, 'id'> | null, o2: Pick<ILezioneCorso, 'id'> | null): boolean {
    return o1 && o2 ? this.getLezioneCorsoIdentifier(o1) === this.getLezioneCorsoIdentifier(o2) : o1 === o2;
  }

  addLezioneCorsoToCollectionIfMissing<Type extends Pick<ILezioneCorso, 'id'>>(
    lezioneCorsoCollection: Type[],
    ...lezioneCorsosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const lezioneCorsos: Type[] = lezioneCorsosToCheck.filter(isPresent);
    if (lezioneCorsos.length > 0) {
      const lezioneCorsoCollectionIdentifiers = lezioneCorsoCollection.map(lezioneCorsoItem =>
        this.getLezioneCorsoIdentifier(lezioneCorsoItem),
      );
      const lezioneCorsosToAdd = lezioneCorsos.filter(lezioneCorsoItem => {
        const lezioneCorsoIdentifier = this.getLezioneCorsoIdentifier(lezioneCorsoItem);
        if (lezioneCorsoCollectionIdentifiers.includes(lezioneCorsoIdentifier)) {
          return false;
        }
        lezioneCorsoCollectionIdentifiers.push(lezioneCorsoIdentifier);
        return true;
      });
      return [...lezioneCorsosToAdd, ...lezioneCorsoCollection];
    }
    return lezioneCorsoCollection;
  }
}
