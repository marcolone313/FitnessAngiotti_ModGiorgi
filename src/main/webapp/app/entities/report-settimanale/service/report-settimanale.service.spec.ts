import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IReportSettimanale } from '../report-settimanale.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../report-settimanale.test-samples';

import { ReportSettimanaleService, RestReportSettimanale } from './report-settimanale.service';

const requireRestSample: RestReportSettimanale = {
  ...sampleWithRequiredData,
  dataCreazione: sampleWithRequiredData.dataCreazione?.format(DATE_FORMAT),
  dataScadenza: sampleWithRequiredData.dataScadenza?.format(DATE_FORMAT),
  dataCompletamento: sampleWithRequiredData.dataCompletamento?.format(DATE_FORMAT),
};

describe('ReportSettimanale Service', () => {
  let service: ReportSettimanaleService;
  let httpMock: HttpTestingController;
  let expectedResult: IReportSettimanale | IReportSettimanale[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReportSettimanaleService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ReportSettimanale', () => {
      const reportSettimanale = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reportSettimanale).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReportSettimanale', () => {
      const reportSettimanale = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reportSettimanale).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReportSettimanale', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReportSettimanale', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReportSettimanale', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReportSettimanaleToCollectionIfMissing', () => {
      it('should add a ReportSettimanale to an empty array', () => {
        const reportSettimanale: IReportSettimanale = sampleWithRequiredData;
        expectedResult = service.addReportSettimanaleToCollectionIfMissing([], reportSettimanale);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportSettimanale);
      });

      it('should not add a ReportSettimanale to an array that contains it', () => {
        const reportSettimanale: IReportSettimanale = sampleWithRequiredData;
        const reportSettimanaleCollection: IReportSettimanale[] = [
          {
            ...reportSettimanale,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportSettimanaleToCollectionIfMissing(reportSettimanaleCollection, reportSettimanale);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReportSettimanale to an array that doesn't contain it", () => {
        const reportSettimanale: IReportSettimanale = sampleWithRequiredData;
        const reportSettimanaleCollection: IReportSettimanale[] = [sampleWithPartialData];
        expectedResult = service.addReportSettimanaleToCollectionIfMissing(reportSettimanaleCollection, reportSettimanale);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportSettimanale);
      });

      it('should add only unique ReportSettimanale to an array', () => {
        const reportSettimanaleArray: IReportSettimanale[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportSettimanaleCollection: IReportSettimanale[] = [sampleWithRequiredData];
        expectedResult = service.addReportSettimanaleToCollectionIfMissing(reportSettimanaleCollection, ...reportSettimanaleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reportSettimanale: IReportSettimanale = sampleWithRequiredData;
        const reportSettimanale2: IReportSettimanale = sampleWithPartialData;
        expectedResult = service.addReportSettimanaleToCollectionIfMissing([], reportSettimanale, reportSettimanale2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reportSettimanale);
        expect(expectedResult).toContain(reportSettimanale2);
      });

      it('should accept null and undefined values', () => {
        const reportSettimanale: IReportSettimanale = sampleWithRequiredData;
        expectedResult = service.addReportSettimanaleToCollectionIfMissing([], null, reportSettimanale, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reportSettimanale);
      });

      it('should return initial array if no ReportSettimanale is added', () => {
        const reportSettimanaleCollection: IReportSettimanale[] = [sampleWithRequiredData];
        expectedResult = service.addReportSettimanaleToCollectionIfMissing(reportSettimanaleCollection, undefined, null);
        expect(expectedResult).toEqual(reportSettimanaleCollection);
      });
    });

    describe('compareReportSettimanale', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReportSettimanale(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareReportSettimanale(entity1, entity2);
        const compareResult2 = service.compareReportSettimanale(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareReportSettimanale(entity1, entity2);
        const compareResult2 = service.compareReportSettimanale(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareReportSettimanale(entity1, entity2);
        const compareResult2 = service.compareReportSettimanale(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
