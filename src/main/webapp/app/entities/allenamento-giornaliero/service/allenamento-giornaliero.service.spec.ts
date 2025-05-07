import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAllenamentoGiornaliero } from '../allenamento-giornaliero.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../allenamento-giornaliero.test-samples';

import { AllenamentoGiornalieroService, RestAllenamentoGiornaliero } from './allenamento-giornaliero.service';

const requireRestSample: RestAllenamentoGiornaliero = {
  ...sampleWithRequiredData,
  dataAllenamento: sampleWithRequiredData.dataAllenamento?.format(DATE_FORMAT),
};

describe('AllenamentoGiornaliero Service', () => {
  let service: AllenamentoGiornalieroService;
  let httpMock: HttpTestingController;
  let expectedResult: IAllenamentoGiornaliero | IAllenamentoGiornaliero[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AllenamentoGiornalieroService);
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

    it('should create a AllenamentoGiornaliero', () => {
      const allenamentoGiornaliero = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(allenamentoGiornaliero).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AllenamentoGiornaliero', () => {
      const allenamentoGiornaliero = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(allenamentoGiornaliero).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AllenamentoGiornaliero', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AllenamentoGiornaliero', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AllenamentoGiornaliero', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAllenamentoGiornalieroToCollectionIfMissing', () => {
      it('should add a AllenamentoGiornaliero to an empty array', () => {
        const allenamentoGiornaliero: IAllenamentoGiornaliero = sampleWithRequiredData;
        expectedResult = service.addAllenamentoGiornalieroToCollectionIfMissing([], allenamentoGiornaliero);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(allenamentoGiornaliero);
      });

      it('should not add a AllenamentoGiornaliero to an array that contains it', () => {
        const allenamentoGiornaliero: IAllenamentoGiornaliero = sampleWithRequiredData;
        const allenamentoGiornalieroCollection: IAllenamentoGiornaliero[] = [
          {
            ...allenamentoGiornaliero,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAllenamentoGiornalieroToCollectionIfMissing(allenamentoGiornalieroCollection, allenamentoGiornaliero);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AllenamentoGiornaliero to an array that doesn't contain it", () => {
        const allenamentoGiornaliero: IAllenamentoGiornaliero = sampleWithRequiredData;
        const allenamentoGiornalieroCollection: IAllenamentoGiornaliero[] = [sampleWithPartialData];
        expectedResult = service.addAllenamentoGiornalieroToCollectionIfMissing(allenamentoGiornalieroCollection, allenamentoGiornaliero);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(allenamentoGiornaliero);
      });

      it('should add only unique AllenamentoGiornaliero to an array', () => {
        const allenamentoGiornalieroArray: IAllenamentoGiornaliero[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const allenamentoGiornalieroCollection: IAllenamentoGiornaliero[] = [sampleWithRequiredData];
        expectedResult = service.addAllenamentoGiornalieroToCollectionIfMissing(
          allenamentoGiornalieroCollection,
          ...allenamentoGiornalieroArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const allenamentoGiornaliero: IAllenamentoGiornaliero = sampleWithRequiredData;
        const allenamentoGiornaliero2: IAllenamentoGiornaliero = sampleWithPartialData;
        expectedResult = service.addAllenamentoGiornalieroToCollectionIfMissing([], allenamentoGiornaliero, allenamentoGiornaliero2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(allenamentoGiornaliero);
        expect(expectedResult).toContain(allenamentoGiornaliero2);
      });

      it('should accept null and undefined values', () => {
        const allenamentoGiornaliero: IAllenamentoGiornaliero = sampleWithRequiredData;
        expectedResult = service.addAllenamentoGiornalieroToCollectionIfMissing([], null, allenamentoGiornaliero, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(allenamentoGiornaliero);
      });

      it('should return initial array if no AllenamentoGiornaliero is added', () => {
        const allenamentoGiornalieroCollection: IAllenamentoGiornaliero[] = [sampleWithRequiredData];
        expectedResult = service.addAllenamentoGiornalieroToCollectionIfMissing(allenamentoGiornalieroCollection, undefined, null);
        expect(expectedResult).toEqual(allenamentoGiornalieroCollection);
      });
    });

    describe('compareAllenamentoGiornaliero', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAllenamentoGiornaliero(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAllenamentoGiornaliero(entity1, entity2);
        const compareResult2 = service.compareAllenamentoGiornaliero(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAllenamentoGiornaliero(entity1, entity2);
        const compareResult2 = service.compareAllenamentoGiornaliero(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAllenamentoGiornaliero(entity1, entity2);
        const compareResult2 = service.compareAllenamentoGiornaliero(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
