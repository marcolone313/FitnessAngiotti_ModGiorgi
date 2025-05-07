import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAllenamentoGiornaliero, NewAllenamentoGiornaliero } from '../allenamento-giornaliero.model';

export type PartialUpdateAllenamentoGiornaliero = Partial<IAllenamentoGiornaliero> & Pick<IAllenamentoGiornaliero, 'id'>;

type RestOf<T extends IAllenamentoGiornaliero | NewAllenamentoGiornaliero> = Omit<T, 'dataAllenamento'> & {
  dataAllenamento?: string | null;
};

export type RestAllenamentoGiornaliero = RestOf<IAllenamentoGiornaliero>;

export type NewRestAllenamentoGiornaliero = RestOf<NewAllenamentoGiornaliero>;

export type PartialUpdateRestAllenamentoGiornaliero = RestOf<PartialUpdateAllenamentoGiornaliero>;

export type EntityResponseType = HttpResponse<IAllenamentoGiornaliero>;
export type EntityArrayResponseType = HttpResponse<IAllenamentoGiornaliero[]>;

@Injectable({ providedIn: 'root' })
export class AllenamentoGiornalieroService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/allenamento-giornalieros');

  create(allenamentoGiornaliero: NewAllenamentoGiornaliero): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(allenamentoGiornaliero);
    return this.http
      .post<RestAllenamentoGiornaliero>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(allenamentoGiornaliero: IAllenamentoGiornaliero): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(allenamentoGiornaliero);
    return this.http
      .put<RestAllenamentoGiornaliero>(`${this.resourceUrl}/${this.getAllenamentoGiornalieroIdentifier(allenamentoGiornaliero)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(allenamentoGiornaliero: PartialUpdateAllenamentoGiornaliero): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(allenamentoGiornaliero);
    return this.http
      .patch<RestAllenamentoGiornaliero>(`${this.resourceUrl}/${this.getAllenamentoGiornalieroIdentifier(allenamentoGiornaliero)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAllenamentoGiornaliero>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAllenamentoGiornaliero[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAllenamentoGiornalieroIdentifier(allenamentoGiornaliero: Pick<IAllenamentoGiornaliero, 'id'>): number {
    return allenamentoGiornaliero.id;
  }

  compareAllenamentoGiornaliero(o1: Pick<IAllenamentoGiornaliero, 'id'> | null, o2: Pick<IAllenamentoGiornaliero, 'id'> | null): boolean {
    return o1 && o2 ? this.getAllenamentoGiornalieroIdentifier(o1) === this.getAllenamentoGiornalieroIdentifier(o2) : o1 === o2;
  }

  addAllenamentoGiornalieroToCollectionIfMissing<Type extends Pick<IAllenamentoGiornaliero, 'id'>>(
    allenamentoGiornalieroCollection: Type[],
    ...allenamentoGiornalierosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const allenamentoGiornalieros: Type[] = allenamentoGiornalierosToCheck.filter(isPresent);
    if (allenamentoGiornalieros.length > 0) {
      const allenamentoGiornalieroCollectionIdentifiers = allenamentoGiornalieroCollection.map(allenamentoGiornalieroItem =>
        this.getAllenamentoGiornalieroIdentifier(allenamentoGiornalieroItem),
      );
      const allenamentoGiornalierosToAdd = allenamentoGiornalieros.filter(allenamentoGiornalieroItem => {
        const allenamentoGiornalieroIdentifier = this.getAllenamentoGiornalieroIdentifier(allenamentoGiornalieroItem);
        if (allenamentoGiornalieroCollectionIdentifiers.includes(allenamentoGiornalieroIdentifier)) {
          return false;
        }
        allenamentoGiornalieroCollectionIdentifiers.push(allenamentoGiornalieroIdentifier);
        return true;
      });
      return [...allenamentoGiornalierosToAdd, ...allenamentoGiornalieroCollection];
    }
    return allenamentoGiornalieroCollection;
  }

  protected convertDateFromClient<T extends IAllenamentoGiornaliero | NewAllenamentoGiornaliero | PartialUpdateAllenamentoGiornaliero>(
    allenamentoGiornaliero: T,
  ): RestOf<T> {
    return {
      ...allenamentoGiornaliero,
      dataAllenamento: allenamentoGiornaliero.dataAllenamento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAllenamentoGiornaliero: RestAllenamentoGiornaliero): IAllenamentoGiornaliero {
    return {
      ...restAllenamentoGiornaliero,
      dataAllenamento: restAllenamentoGiornaliero.dataAllenamento ? dayjs(restAllenamentoGiornaliero.dataAllenamento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAllenamentoGiornaliero>): HttpResponse<IAllenamentoGiornaliero> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAllenamentoGiornaliero[]>): HttpResponse<IAllenamentoGiornaliero[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
