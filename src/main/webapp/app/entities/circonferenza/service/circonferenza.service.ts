import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICirconferenza, NewCirconferenza } from '../circonferenza.model';

export type PartialUpdateCirconferenza = Partial<ICirconferenza> & Pick<ICirconferenza, 'id'>;

type RestOf<T extends ICirconferenza | NewCirconferenza> = Omit<T, 'dataInserimento'> & {
  dataInserimento?: string | null;
};

export type RestCirconferenza = RestOf<ICirconferenza>;

export type NewRestCirconferenza = RestOf<NewCirconferenza>;

export type PartialUpdateRestCirconferenza = RestOf<PartialUpdateCirconferenza>;

export type EntityResponseType = HttpResponse<ICirconferenza>;
export type EntityArrayResponseType = HttpResponse<ICirconferenza[]>;

@Injectable({ providedIn: 'root' })
export class CirconferenzaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/circonferenzas');

  create(circonferenza: NewCirconferenza): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(circonferenza);
    return this.http
      .post<RestCirconferenza>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(circonferenza: ICirconferenza): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(circonferenza);
    return this.http
      .put<RestCirconferenza>(`${this.resourceUrl}/${this.getCirconferenzaIdentifier(circonferenza)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(circonferenza: PartialUpdateCirconferenza): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(circonferenza);
    return this.http
      .patch<RestCirconferenza>(`${this.resourceUrl}/${this.getCirconferenzaIdentifier(circonferenza)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCirconferenza>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCirconferenza[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCirconferenzaIdentifier(circonferenza: Pick<ICirconferenza, 'id'>): number {
    return circonferenza.id;
  }

  compareCirconferenza(o1: Pick<ICirconferenza, 'id'> | null, o2: Pick<ICirconferenza, 'id'> | null): boolean {
    return o1 && o2 ? this.getCirconferenzaIdentifier(o1) === this.getCirconferenzaIdentifier(o2) : o1 === o2;
  }

  addCirconferenzaToCollectionIfMissing<Type extends Pick<ICirconferenza, 'id'>>(
    circonferenzaCollection: Type[],
    ...circonferenzasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const circonferenzas: Type[] = circonferenzasToCheck.filter(isPresent);
    if (circonferenzas.length > 0) {
      const circonferenzaCollectionIdentifiers = circonferenzaCollection.map(circonferenzaItem =>
        this.getCirconferenzaIdentifier(circonferenzaItem),
      );
      const circonferenzasToAdd = circonferenzas.filter(circonferenzaItem => {
        const circonferenzaIdentifier = this.getCirconferenzaIdentifier(circonferenzaItem);
        if (circonferenzaCollectionIdentifiers.includes(circonferenzaIdentifier)) {
          return false;
        }
        circonferenzaCollectionIdentifiers.push(circonferenzaIdentifier);
        return true;
      });
      return [...circonferenzasToAdd, ...circonferenzaCollection];
    }
    return circonferenzaCollection;
  }

  protected convertDateFromClient<T extends ICirconferenza | NewCirconferenza | PartialUpdateCirconferenza>(circonferenza: T): RestOf<T> {
    return {
      ...circonferenza,
      dataInserimento: circonferenza.dataInserimento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCirconferenza: RestCirconferenza): ICirconferenza {
    return {
      ...restCirconferenza,
      dataInserimento: restCirconferenza.dataInserimento ? dayjs(restCirconferenza.dataInserimento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCirconferenza>): HttpResponse<ICirconferenza> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCirconferenza[]>): HttpResponse<ICirconferenza[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
