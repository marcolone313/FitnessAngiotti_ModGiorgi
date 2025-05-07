import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISchedaSettimanale } from '../scheda-settimanale.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../scheda-settimanale.test-samples';

import { RestSchedaSettimanale, SchedaSettimanaleService } from './scheda-settimanale.service';

const requireRestSample: RestSchedaSettimanale = {
  ...sampleWithRequiredData,
  dataCreazione: sampleWithRequiredData.dataCreazione?.format(DATE_FORMAT),
};

describe('SchedaSettimanale Service', () => {
  let service: SchedaSettimanaleService;
  let httpMock: HttpTestingController;
  let expectedResult: ISchedaSettimanale | ISchedaSettimanale[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SchedaSettimanaleService);
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

    it('should create a SchedaSettimanale', () => {
      const schedaSettimanale = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(schedaSettimanale).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SchedaSettimanale', () => {
      const schedaSettimanale = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(schedaSettimanale).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SchedaSettimanale', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SchedaSettimanale', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SchedaSettimanale', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSchedaSettimanaleToCollectionIfMissing', () => {
      it('should add a SchedaSettimanale to an empty array', () => {
        const schedaSettimanale: ISchedaSettimanale = sampleWithRequiredData;
        expectedResult = service.addSchedaSettimanaleToCollectionIfMissing([], schedaSettimanale);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(schedaSettimanale);
      });

      it('should not add a SchedaSettimanale to an array that contains it', () => {
        const schedaSettimanale: ISchedaSettimanale = sampleWithRequiredData;
        const schedaSettimanaleCollection: ISchedaSettimanale[] = [
          {
            ...schedaSettimanale,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSchedaSettimanaleToCollectionIfMissing(schedaSettimanaleCollection, schedaSettimanale);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SchedaSettimanale to an array that doesn't contain it", () => {
        const schedaSettimanale: ISchedaSettimanale = sampleWithRequiredData;
        const schedaSettimanaleCollection: ISchedaSettimanale[] = [sampleWithPartialData];
        expectedResult = service.addSchedaSettimanaleToCollectionIfMissing(schedaSettimanaleCollection, schedaSettimanale);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(schedaSettimanale);
      });

      it('should add only unique SchedaSettimanale to an array', () => {
        const schedaSettimanaleArray: ISchedaSettimanale[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const schedaSettimanaleCollection: ISchedaSettimanale[] = [sampleWithRequiredData];
        expectedResult = service.addSchedaSettimanaleToCollectionIfMissing(schedaSettimanaleCollection, ...schedaSettimanaleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const schedaSettimanale: ISchedaSettimanale = sampleWithRequiredData;
        const schedaSettimanale2: ISchedaSettimanale = sampleWithPartialData;
        expectedResult = service.addSchedaSettimanaleToCollectionIfMissing([], schedaSettimanale, schedaSettimanale2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(schedaSettimanale);
        expect(expectedResult).toContain(schedaSettimanale2);
      });

      it('should accept null and undefined values', () => {
        const schedaSettimanale: ISchedaSettimanale = sampleWithRequiredData;
        expectedResult = service.addSchedaSettimanaleToCollectionIfMissing([], null, schedaSettimanale, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(schedaSettimanale);
      });

      it('should return initial array if no SchedaSettimanale is added', () => {
        const schedaSettimanaleCollection: ISchedaSettimanale[] = [sampleWithRequiredData];
        expectedResult = service.addSchedaSettimanaleToCollectionIfMissing(schedaSettimanaleCollection, undefined, null);
        expect(expectedResult).toEqual(schedaSettimanaleCollection);
      });
    });

    describe('compareSchedaSettimanale', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSchedaSettimanale(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSchedaSettimanale(entity1, entity2);
        const compareResult2 = service.compareSchedaSettimanale(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSchedaSettimanale(entity1, entity2);
        const compareResult2 = service.compareSchedaSettimanale(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSchedaSettimanale(entity1, entity2);
        const compareResult2 = service.compareSchedaSettimanale(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
