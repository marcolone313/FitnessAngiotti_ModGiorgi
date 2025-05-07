import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPassaggioEsercizio } from '../passaggio-esercizio.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../passaggio-esercizio.test-samples';

import { PassaggioEsercizioService } from './passaggio-esercizio.service';

const requireRestSample: IPassaggioEsercizio = {
  ...sampleWithRequiredData,
};

describe('PassaggioEsercizio Service', () => {
  let service: PassaggioEsercizioService;
  let httpMock: HttpTestingController;
  let expectedResult: IPassaggioEsercizio | IPassaggioEsercizio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PassaggioEsercizioService);
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

    it('should create a PassaggioEsercizio', () => {
      const passaggioEsercizio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(passaggioEsercizio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PassaggioEsercizio', () => {
      const passaggioEsercizio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(passaggioEsercizio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PassaggioEsercizio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PassaggioEsercizio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PassaggioEsercizio', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPassaggioEsercizioToCollectionIfMissing', () => {
      it('should add a PassaggioEsercizio to an empty array', () => {
        const passaggioEsercizio: IPassaggioEsercizio = sampleWithRequiredData;
        expectedResult = service.addPassaggioEsercizioToCollectionIfMissing([], passaggioEsercizio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(passaggioEsercizio);
      });

      it('should not add a PassaggioEsercizio to an array that contains it', () => {
        const passaggioEsercizio: IPassaggioEsercizio = sampleWithRequiredData;
        const passaggioEsercizioCollection: IPassaggioEsercizio[] = [
          {
            ...passaggioEsercizio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPassaggioEsercizioToCollectionIfMissing(passaggioEsercizioCollection, passaggioEsercizio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PassaggioEsercizio to an array that doesn't contain it", () => {
        const passaggioEsercizio: IPassaggioEsercizio = sampleWithRequiredData;
        const passaggioEsercizioCollection: IPassaggioEsercizio[] = [sampleWithPartialData];
        expectedResult = service.addPassaggioEsercizioToCollectionIfMissing(passaggioEsercizioCollection, passaggioEsercizio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(passaggioEsercizio);
      });

      it('should add only unique PassaggioEsercizio to an array', () => {
        const passaggioEsercizioArray: IPassaggioEsercizio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const passaggioEsercizioCollection: IPassaggioEsercizio[] = [sampleWithRequiredData];
        expectedResult = service.addPassaggioEsercizioToCollectionIfMissing(passaggioEsercizioCollection, ...passaggioEsercizioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const passaggioEsercizio: IPassaggioEsercizio = sampleWithRequiredData;
        const passaggioEsercizio2: IPassaggioEsercizio = sampleWithPartialData;
        expectedResult = service.addPassaggioEsercizioToCollectionIfMissing([], passaggioEsercizio, passaggioEsercizio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(passaggioEsercizio);
        expect(expectedResult).toContain(passaggioEsercizio2);
      });

      it('should accept null and undefined values', () => {
        const passaggioEsercizio: IPassaggioEsercizio = sampleWithRequiredData;
        expectedResult = service.addPassaggioEsercizioToCollectionIfMissing([], null, passaggioEsercizio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(passaggioEsercizio);
      });

      it('should return initial array if no PassaggioEsercizio is added', () => {
        const passaggioEsercizioCollection: IPassaggioEsercizio[] = [sampleWithRequiredData];
        expectedResult = service.addPassaggioEsercizioToCollectionIfMissing(passaggioEsercizioCollection, undefined, null);
        expect(expectedResult).toEqual(passaggioEsercizioCollection);
      });
    });

    describe('comparePassaggioEsercizio', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePassaggioEsercizio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePassaggioEsercizio(entity1, entity2);
        const compareResult2 = service.comparePassaggioEsercizio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePassaggioEsercizio(entity1, entity2);
        const compareResult2 = service.comparePassaggioEsercizio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePassaggioEsercizio(entity1, entity2);
        const compareResult2 = service.comparePassaggioEsercizio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
