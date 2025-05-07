import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICorsoAcademy, NewCorsoAcademy } from '../corso-academy.model';

export type PartialUpdateCorsoAcademy = Partial<ICorsoAcademy> & Pick<ICorsoAcademy, 'id'>;

export type EntityResponseType = HttpResponse<ICorsoAcademy>;
export type EntityArrayResponseType = HttpResponse<ICorsoAcademy[]>;

@Injectable({ providedIn: 'root' })
export class CorsoAcademyService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/corso-academies');

  create(corsoAcademy: NewCorsoAcademy): Observable<EntityResponseType> {
    return this.http.post<ICorsoAcademy>(this.resourceUrl, corsoAcademy, { observe: 'response' });
  }

  update(corsoAcademy: ICorsoAcademy): Observable<EntityResponseType> {
    return this.http.put<ICorsoAcademy>(`${this.resourceUrl}/${this.getCorsoAcademyIdentifier(corsoAcademy)}`, corsoAcademy, {
      observe: 'response',
    });
  }

  partialUpdate(corsoAcademy: PartialUpdateCorsoAcademy): Observable<EntityResponseType> {
    return this.http.patch<ICorsoAcademy>(`${this.resourceUrl}/${this.getCorsoAcademyIdentifier(corsoAcademy)}`, corsoAcademy, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICorsoAcademy>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICorsoAcademy[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCorsoAcademyIdentifier(corsoAcademy: Pick<ICorsoAcademy, 'id'>): number {
    return corsoAcademy.id;
  }

  compareCorsoAcademy(o1: Pick<ICorsoAcademy, 'id'> | null, o2: Pick<ICorsoAcademy, 'id'> | null): boolean {
    return o1 && o2 ? this.getCorsoAcademyIdentifier(o1) === this.getCorsoAcademyIdentifier(o2) : o1 === o2;
  }

  addCorsoAcademyToCollectionIfMissing<Type extends Pick<ICorsoAcademy, 'id'>>(
    corsoAcademyCollection: Type[],
    ...corsoAcademiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const corsoAcademies: Type[] = corsoAcademiesToCheck.filter(isPresent);
    if (corsoAcademies.length > 0) {
      const corsoAcademyCollectionIdentifiers = corsoAcademyCollection.map(corsoAcademyItem =>
        this.getCorsoAcademyIdentifier(corsoAcademyItem),
      );
      const corsoAcademiesToAdd = corsoAcademies.filter(corsoAcademyItem => {
        const corsoAcademyIdentifier = this.getCorsoAcademyIdentifier(corsoAcademyItem);
        if (corsoAcademyCollectionIdentifiers.includes(corsoAcademyIdentifier)) {
          return false;
        }
        corsoAcademyCollectionIdentifiers.push(corsoAcademyIdentifier);
        return true;
      });
      return [...corsoAcademiesToAdd, ...corsoAcademyCollection];
    }
    return corsoAcademyCollection;
  }
}
