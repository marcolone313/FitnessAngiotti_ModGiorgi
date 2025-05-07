import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICircuitoToEsercizio } from '../circuito-to-esercizio.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../circuito-to-esercizio.test-samples';

import { CircuitoToEsercizioService } from './circuito-to-esercizio.service';

const requireRestSample: ICircuitoToEsercizio = {
  ...sampleWithRequiredData,
};

describe('CircuitoToEsercizio Service', () => {
  let service: CircuitoToEsercizioService;
  let httpMock: HttpTestingController;
  let expectedResult: ICircuitoToEsercizio | ICircuitoToEsercizio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CircuitoToEsercizioService);
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

    it('should create a CircuitoToEsercizio', () => {
      const circuitoToEsercizio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(circuitoToEsercizio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CircuitoToEsercizio', () => {
      const circuitoToEsercizio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(circuitoToEsercizio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CircuitoToEsercizio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CircuitoToEsercizio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CircuitoToEsercizio', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCircuitoToEsercizioToCollectionIfMissing', () => {
      it('should add a CircuitoToEsercizio to an empty array', () => {
        const circuitoToEsercizio: ICircuitoToEsercizio = sampleWithRequiredData;
        expectedResult = service.addCircuitoToEsercizioToCollectionIfMissing([], circuitoToEsercizio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(circuitoToEsercizio);
      });

      it('should not add a CircuitoToEsercizio to an array that contains it', () => {
        const circuitoToEsercizio: ICircuitoToEsercizio = sampleWithRequiredData;
        const circuitoToEsercizioCollection: ICircuitoToEsercizio[] = [
          {
            ...circuitoToEsercizio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCircuitoToEsercizioToCollectionIfMissing(circuitoToEsercizioCollection, circuitoToEsercizio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CircuitoToEsercizio to an array that doesn't contain it", () => {
        const circuitoToEsercizio: ICircuitoToEsercizio = sampleWithRequiredData;
        const circuitoToEsercizioCollection: ICircuitoToEsercizio[] = [sampleWithPartialData];
        expectedResult = service.addCircuitoToEsercizioToCollectionIfMissing(circuitoToEsercizioCollection, circuitoToEsercizio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(circuitoToEsercizio);
      });

      it('should add only unique CircuitoToEsercizio to an array', () => {
        const circuitoToEsercizioArray: ICircuitoToEsercizio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const circuitoToEsercizioCollection: ICircuitoToEsercizio[] = [sampleWithRequiredData];
        expectedResult = service.addCircuitoToEsercizioToCollectionIfMissing(circuitoToEsercizioCollection, ...circuitoToEsercizioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const circuitoToEsercizio: ICircuitoToEsercizio = sampleWithRequiredData;
        const circuitoToEsercizio2: ICircuitoToEsercizio = sampleWithPartialData;
        expectedResult = service.addCircuitoToEsercizioToCollectionIfMissing([], circuitoToEsercizio, circuitoToEsercizio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(circuitoToEsercizio);
        expect(expectedResult).toContain(circuitoToEsercizio2);
      });

      it('should accept null and undefined values', () => {
        const circuitoToEsercizio: ICircuitoToEsercizio = sampleWithRequiredData;
        expectedResult = service.addCircuitoToEsercizioToCollectionIfMissing([], null, circuitoToEsercizio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(circuitoToEsercizio);
      });

      it('should return initial array if no CircuitoToEsercizio is added', () => {
        const circuitoToEsercizioCollection: ICircuitoToEsercizio[] = [sampleWithRequiredData];
        expectedResult = service.addCircuitoToEsercizioToCollectionIfMissing(circuitoToEsercizioCollection, undefined, null);
        expect(expectedResult).toEqual(circuitoToEsercizioCollection);
      });
    });

    describe('compareCircuitoToEsercizio', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCircuitoToEsercizio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCircuitoToEsercizio(entity1, entity2);
        const compareResult2 = service.compareCircuitoToEsercizio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCircuitoToEsercizio(entity1, entity2);
        const compareResult2 = service.compareCircuitoToEsercizio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCircuitoToEsercizio(entity1, entity2);
        const compareResult2 = service.compareCircuitoToEsercizio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
