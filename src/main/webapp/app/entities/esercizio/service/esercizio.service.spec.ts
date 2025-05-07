import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEsercizio } from '../esercizio.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../esercizio.test-samples';

import { EsercizioService } from './esercizio.service';

const requireRestSample: IEsercizio = {
  ...sampleWithRequiredData,
};

describe('Esercizio Service', () => {
  let service: EsercizioService;
  let httpMock: HttpTestingController;
  let expectedResult: IEsercizio | IEsercizio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EsercizioService);
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

    it('should create a Esercizio', () => {
      const esercizio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(esercizio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Esercizio', () => {
      const esercizio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(esercizio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Esercizio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Esercizio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Esercizio', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEsercizioToCollectionIfMissing', () => {
      it('should add a Esercizio to an empty array', () => {
        const esercizio: IEsercizio = sampleWithRequiredData;
        expectedResult = service.addEsercizioToCollectionIfMissing([], esercizio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(esercizio);
      });

      it('should not add a Esercizio to an array that contains it', () => {
        const esercizio: IEsercizio = sampleWithRequiredData;
        const esercizioCollection: IEsercizio[] = [
          {
            ...esercizio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEsercizioToCollectionIfMissing(esercizioCollection, esercizio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Esercizio to an array that doesn't contain it", () => {
        const esercizio: IEsercizio = sampleWithRequiredData;
        const esercizioCollection: IEsercizio[] = [sampleWithPartialData];
        expectedResult = service.addEsercizioToCollectionIfMissing(esercizioCollection, esercizio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(esercizio);
      });

      it('should add only unique Esercizio to an array', () => {
        const esercizioArray: IEsercizio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const esercizioCollection: IEsercizio[] = [sampleWithRequiredData];
        expectedResult = service.addEsercizioToCollectionIfMissing(esercizioCollection, ...esercizioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const esercizio: IEsercizio = sampleWithRequiredData;
        const esercizio2: IEsercizio = sampleWithPartialData;
        expectedResult = service.addEsercizioToCollectionIfMissing([], esercizio, esercizio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(esercizio);
        expect(expectedResult).toContain(esercizio2);
      });

      it('should accept null and undefined values', () => {
        const esercizio: IEsercizio = sampleWithRequiredData;
        expectedResult = service.addEsercizioToCollectionIfMissing([], null, esercizio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(esercizio);
      });

      it('should return initial array if no Esercizio is added', () => {
        const esercizioCollection: IEsercizio[] = [sampleWithRequiredData];
        expectedResult = service.addEsercizioToCollectionIfMissing(esercizioCollection, undefined, null);
        expect(expectedResult).toEqual(esercizioCollection);
      });
    });

    describe('compareEsercizio', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEsercizio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEsercizio(entity1, entity2);
        const compareResult2 = service.compareEsercizio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEsercizio(entity1, entity2);
        const compareResult2 = service.compareEsercizio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEsercizio(entity1, entity2);
        const compareResult2 = service.compareEsercizio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
