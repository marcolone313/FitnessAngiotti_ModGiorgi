import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlicometria, NewPlicometria } from '../plicometria.model';

export type PartialUpdatePlicometria = Partial<IPlicometria> & Pick<IPlicometria, 'id'>;

type RestOf<T extends IPlicometria | NewPlicometria> = Omit<T, 'dataInserimento'> & {
  dataInserimento?: string | null;
};

export type RestPlicometria = RestOf<IPlicometria>;

export type NewRestPlicometria = RestOf<NewPlicometria>;

export type PartialUpdateRestPlicometria = RestOf<PartialUpdatePlicometria>;

export type EntityResponseType = HttpResponse<IPlicometria>;
export type EntityArrayResponseType = HttpResponse<IPlicometria[]>;

@Injectable({ providedIn: 'root' })
export class PlicometriaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/plicometrias');

  create(plicometria: NewPlicometria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plicometria);
    return this.http
      .post<RestPlicometria>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(plicometria: IPlicometria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plicometria);
    return this.http
      .put<RestPlicometria>(`${this.resourceUrl}/${this.getPlicometriaIdentifier(plicometria)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(plicometria: PartialUpdatePlicometria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plicometria);
    return this.http
      .patch<RestPlicometria>(`${this.resourceUrl}/${this.getPlicometriaIdentifier(plicometria)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPlicometria>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPlicometria[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlicometriaIdentifier(plicometria: Pick<IPlicometria, 'id'>): number {
    return plicometria.id;
  }

  comparePlicometria(o1: Pick<IPlicometria, 'id'> | null, o2: Pick<IPlicometria, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlicometriaIdentifier(o1) === this.getPlicometriaIdentifier(o2) : o1 === o2;
  }

  addPlicometriaToCollectionIfMissing<Type extends Pick<IPlicometria, 'id'>>(
    plicometriaCollection: Type[],
    ...plicometriasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const plicometrias: Type[] = plicometriasToCheck.filter(isPresent);
    if (plicometrias.length > 0) {
      const plicometriaCollectionIdentifiers = plicometriaCollection.map(plicometriaItem => this.getPlicometriaIdentifier(plicometriaItem));
      const plicometriasToAdd = plicometrias.filter(plicometriaItem => {
        const plicometriaIdentifier = this.getPlicometriaIdentifier(plicometriaItem);
        if (plicometriaCollectionIdentifiers.includes(plicometriaIdentifier)) {
          return false;
        }
        plicometriaCollectionIdentifiers.push(plicometriaIdentifier);
        return true;
      });
      return [...plicometriasToAdd, ...plicometriaCollection];
    }
    return plicometriaCollection;
  }

  protected convertDateFromClient<T extends IPlicometria | NewPlicometria | PartialUpdatePlicometria>(plicometria: T): RestOf<T> {
    return {
      ...plicometria,
      dataInserimento: plicometria.dataInserimento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPlicometria: RestPlicometria): IPlicometria {
    return {
      ...restPlicometria,
      dataInserimento: restPlicometria.dataInserimento ? dayjs(restPlicometria.dataInserimento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPlicometria>): HttpResponse<IPlicometria> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPlicometria[]>): HttpResponse<IPlicometria[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
