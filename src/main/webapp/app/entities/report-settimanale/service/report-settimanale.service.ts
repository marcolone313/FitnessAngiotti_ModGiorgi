import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReportSettimanale, NewReportSettimanale } from '../report-settimanale.model';

export type PartialUpdateReportSettimanale = Partial<IReportSettimanale> & Pick<IReportSettimanale, 'id'>;

type RestOf<T extends IReportSettimanale | NewReportSettimanale> = Omit<T, 'dataCreazione' | 'dataScadenza' | 'dataCompletamento'> & {
  dataCreazione?: string | null;
  dataScadenza?: string | null;
  dataCompletamento?: string | null;
};

export type RestReportSettimanale = RestOf<IReportSettimanale>;

export type NewRestReportSettimanale = RestOf<NewReportSettimanale>;

export type PartialUpdateRestReportSettimanale = RestOf<PartialUpdateReportSettimanale>;

export type EntityResponseType = HttpResponse<IReportSettimanale>;
export type EntityArrayResponseType = HttpResponse<IReportSettimanale[]>;

@Injectable({ providedIn: 'root' })
export class ReportSettimanaleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/report-settimanales');

  create(reportSettimanale: NewReportSettimanale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportSettimanale);
    return this.http
      .post<RestReportSettimanale>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(reportSettimanale: IReportSettimanale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportSettimanale);
    return this.http
      .put<RestReportSettimanale>(`${this.resourceUrl}/${this.getReportSettimanaleIdentifier(reportSettimanale)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(reportSettimanale: PartialUpdateReportSettimanale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportSettimanale);
    return this.http
      .patch<RestReportSettimanale>(`${this.resourceUrl}/${this.getReportSettimanaleIdentifier(reportSettimanale)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestReportSettimanale>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReportSettimanale[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReportSettimanaleIdentifier(reportSettimanale: Pick<IReportSettimanale, 'id'>): number {
    return reportSettimanale.id;
  }

  compareReportSettimanale(o1: Pick<IReportSettimanale, 'id'> | null, o2: Pick<IReportSettimanale, 'id'> | null): boolean {
    return o1 && o2 ? this.getReportSettimanaleIdentifier(o1) === this.getReportSettimanaleIdentifier(o2) : o1 === o2;
  }

  addReportSettimanaleToCollectionIfMissing<Type extends Pick<IReportSettimanale, 'id'>>(
    reportSettimanaleCollection: Type[],
    ...reportSettimanalesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportSettimanales: Type[] = reportSettimanalesToCheck.filter(isPresent);
    if (reportSettimanales.length > 0) {
      const reportSettimanaleCollectionIdentifiers = reportSettimanaleCollection.map(reportSettimanaleItem =>
        this.getReportSettimanaleIdentifier(reportSettimanaleItem),
      );
      const reportSettimanalesToAdd = reportSettimanales.filter(reportSettimanaleItem => {
        const reportSettimanaleIdentifier = this.getReportSettimanaleIdentifier(reportSettimanaleItem);
        if (reportSettimanaleCollectionIdentifiers.includes(reportSettimanaleIdentifier)) {
          return false;
        }
        reportSettimanaleCollectionIdentifiers.push(reportSettimanaleIdentifier);
        return true;
      });
      return [...reportSettimanalesToAdd, ...reportSettimanaleCollection];
    }
    return reportSettimanaleCollection;
  }

  protected convertDateFromClient<T extends IReportSettimanale | NewReportSettimanale | PartialUpdateReportSettimanale>(
    reportSettimanale: T,
  ): RestOf<T> {
    return {
      ...reportSettimanale,
      dataCreazione: reportSettimanale.dataCreazione?.format(DATE_FORMAT) ?? null,
      dataScadenza: reportSettimanale.dataScadenza?.format(DATE_FORMAT) ?? null,
      dataCompletamento: reportSettimanale.dataCompletamento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restReportSettimanale: RestReportSettimanale): IReportSettimanale {
    return {
      ...restReportSettimanale,
      dataCreazione: restReportSettimanale.dataCreazione ? dayjs(restReportSettimanale.dataCreazione) : undefined,
      dataScadenza: restReportSettimanale.dataScadenza ? dayjs(restReportSettimanale.dataScadenza) : undefined,
      dataCompletamento: restReportSettimanale.dataCompletamento ? dayjs(restReportSettimanale.dataCompletamento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestReportSettimanale>): HttpResponse<IReportSettimanale> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestReportSettimanale[]>): HttpResponse<IReportSettimanale[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
