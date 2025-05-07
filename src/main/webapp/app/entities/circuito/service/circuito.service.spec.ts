import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICircuito } from '../circuito.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../circuito.test-samples';

import { CircuitoService } from './circuito.service';

const requireRestSample: ICircuito = {
  ...sampleWithRequiredData,
};

describe('Circuito Service', () => {
  let service: CircuitoService;
  let httpMock: HttpTestingController;
  let expectedResult: ICircuito | ICircuito[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CircuitoService);
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

    it('should create a Circuito', () => {
      const circuito = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(circuito).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Circuito', () => {
      const circuito = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(circuito).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Circuito', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Circuito', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Circuito', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCircuitoToCollectionIfMissing', () => {
      it('should add a Circuito to an empty array', () => {
        const circuito: ICircuito = sampleWithRequiredData;
        expectedResult = service.addCircuitoToCollectionIfMissing([], circuito);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(circuito);
      });

      it('should not add a Circuito to an array that contains it', () => {
        const circuito: ICircuito = sampleWithRequiredData;
        const circuitoCollection: ICircuito[] = [
          {
            ...circuito,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCircuitoToCollectionIfMissing(circuitoCollection, circuito);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Circuito to an array that doesn't contain it", () => {
        const circuito: ICircuito = sampleWithRequiredData;
        const circuitoCollection: ICircuito[] = [sampleWithPartialData];
        expectedResult = service.addCircuitoToCollectionIfMissing(circuitoCollection, circuito);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(circuito);
      });

      it('should add only unique Circuito to an array', () => {
        const circuitoArray: ICircuito[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const circuitoCollection: ICircuito[] = [sampleWithRequiredData];
        expectedResult = service.addCircuitoToCollectionIfMissing(circuitoCollection, ...circuitoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const circuito: ICircuito = sampleWithRequiredData;
        const circuito2: ICircuito = sampleWithPartialData;
        expectedResult = service.addCircuitoToCollectionIfMissing([], circuito, circuito2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(circuito);
        expect(expectedResult).toContain(circuito2);
      });

      it('should accept null and undefined values', () => {
        const circuito: ICircuito = sampleWithRequiredData;
        expectedResult = service.addCircuitoToCollectionIfMissing([], null, circuito, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(circuito);
      });

      it('should return initial array if no Circuito is added', () => {
        const circuitoCollection: ICircuito[] = [sampleWithRequiredData];
        expectedResult = service.addCircuitoToCollectionIfMissing(circuitoCollection, undefined, null);
        expect(expectedResult).toEqual(circuitoCollection);
      });
    });

    describe('compareCircuito', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCircuito(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCircuito(entity1, entity2);
        const compareResult2 = service.compareCircuito(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCircuito(entity1, entity2);
        const compareResult2 = service.compareCircuito(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCircuito(entity1, entity2);
        const compareResult2 = service.compareCircuito(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
