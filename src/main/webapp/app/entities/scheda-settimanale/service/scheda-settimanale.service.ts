import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISchedaSettimanale, NewSchedaSettimanale } from '../scheda-settimanale.model';

export type PartialUpdateSchedaSettimanale = Partial<ISchedaSettimanale> & Pick<ISchedaSettimanale, 'id'>;

type RestOf<T extends ISchedaSettimanale | NewSchedaSettimanale> = Omit<T, 'dataCreazione'> & {
  dataCreazione?: string | null;
};

export type RestSchedaSettimanale = RestOf<ISchedaSettimanale>;

export type NewRestSchedaSettimanale = RestOf<NewSchedaSettimanale>;

export type PartialUpdateRestSchedaSettimanale = RestOf<PartialUpdateSchedaSettimanale>;

export type EntityResponseType = HttpResponse<ISchedaSettimanale>;
export type EntityArrayResponseType = HttpResponse<ISchedaSettimanale[]>;

@Injectable({ providedIn: 'root' })
export class SchedaSettimanaleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/scheda-settimanales');

  create(schedaSettimanale: NewSchedaSettimanale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schedaSettimanale);
    return this.http
      .post<RestSchedaSettimanale>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  importScheda(scheda: ISchedaSettimanale): Observable<ISchedaSettimanale> {
    return this.http.post<ISchedaSettimanale>(`${this.resourceUrl}/import`, scheda);
  }

  update(schedaSettimanale: ISchedaSettimanale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schedaSettimanale);
    return this.http
      .put<RestSchedaSettimanale>(`${this.resourceUrl}/${this.getSchedaSettimanaleIdentifier(schedaSettimanale)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(schedaSettimanale: PartialUpdateSchedaSettimanale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(schedaSettimanale);
    return this.http
      .patch<RestSchedaSettimanale>(`${this.resourceUrl}/${this.getSchedaSettimanaleIdentifier(schedaSettimanale)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSchedaSettimanale>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSchedaSettimanale[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSchedaSettimanaleIdentifier(schedaSettimanale: Pick<ISchedaSettimanale, 'id'>): number {
    return schedaSettimanale.id;
  }

  compareSchedaSettimanale(o1: Pick<ISchedaSettimanale, 'id'> | null, o2: Pick<ISchedaSettimanale, 'id'> | null): boolean {
    return o1 && o2 ? this.getSchedaSettimanaleIdentifier(o1) === this.getSchedaSettimanaleIdentifier(o2) : o1 === o2;
  }

  addSchedaSettimanaleToCollectionIfMissing<Type extends Pick<ISchedaSettimanale, 'id'>>(
    schedaSettimanaleCollection: Type[],
    ...schedaSettimanalesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const schedaSettimanales: Type[] = schedaSettimanalesToCheck.filter(isPresent);
    if (schedaSettimanales.length > 0) {
      const schedaSettimanaleCollectionIdentifiers = schedaSettimanaleCollection.map(schedaSettimanaleItem =>
        this.getSchedaSettimanaleIdentifier(schedaSettimanaleItem),
      );
      const schedaSettimanalesToAdd = schedaSettimanales.filter(schedaSettimanaleItem => {
        const schedaSettimanaleIdentifier = this.getSchedaSettimanaleIdentifier(schedaSettimanaleItem);
        if (schedaSettimanaleCollectionIdentifiers.includes(schedaSettimanaleIdentifier)) {
          return false;
        }
        schedaSettimanaleCollectionIdentifiers.push(schedaSettimanaleIdentifier);
        return true;
      });
      return [...schedaSettimanalesToAdd, ...schedaSettimanaleCollection];
    }
    return schedaSettimanaleCollection;
  }

  protected convertDateFromClient<T extends ISchedaSettimanale | NewSchedaSettimanale | PartialUpdateSchedaSettimanale>(
    schedaSettimanale: T,
  ): RestOf<T> {
    return {
      ...schedaSettimanale,
      dataCreazione: schedaSettimanale.dataCreazione?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSchedaSettimanale: RestSchedaSettimanale): ISchedaSettimanale {
    return {
      ...restSchedaSettimanale,
      dataCreazione: restSchedaSettimanale.dataCreazione ? dayjs(restSchedaSettimanale.dataCreazione) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSchedaSettimanale>): HttpResponse<ISchedaSettimanale> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSchedaSettimanale[]>): HttpResponse<ISchedaSettimanale[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
