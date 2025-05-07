import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICorsa } from '../corsa.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../corsa.test-samples';

import { CorsaService } from './corsa.service';

const requireRestSample: ICorsa = {
  ...sampleWithRequiredData,
};

describe('Corsa Service', () => {
  let service: CorsaService;
  let httpMock: HttpTestingController;
  let expectedResult: ICorsa | ICorsa[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CorsaService);
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

    it('should create a Corsa', () => {
      const corsa = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(corsa).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Corsa', () => {
      const corsa = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(corsa).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Corsa', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Corsa', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Corsa', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCorsaToCollectionIfMissing', () => {
      it('should add a Corsa to an empty array', () => {
        const corsa: ICorsa = sampleWithRequiredData;
        expectedResult = service.addCorsaToCollectionIfMissing([], corsa);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(corsa);
      });

      it('should not add a Corsa to an array that contains it', () => {
        const corsa: ICorsa = sampleWithRequiredData;
        const corsaCollection: ICorsa[] = [
          {
            ...corsa,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCorsaToCollectionIfMissing(corsaCollection, corsa);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Corsa to an array that doesn't contain it", () => {
        const corsa: ICorsa = sampleWithRequiredData;
        const corsaCollection: ICorsa[] = [sampleWithPartialData];
        expectedResult = service.addCorsaToCollectionIfMissing(corsaCollection, corsa);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(corsa);
      });

      it('should add only unique Corsa to an array', () => {
        const corsaArray: ICorsa[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const corsaCollection: ICorsa[] = [sampleWithRequiredData];
        expectedResult = service.addCorsaToCollectionIfMissing(corsaCollection, ...corsaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const corsa: ICorsa = sampleWithRequiredData;
        const corsa2: ICorsa = sampleWithPartialData;
        expectedResult = service.addCorsaToCollectionIfMissing([], corsa, corsa2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(corsa);
        expect(expectedResult).toContain(corsa2);
      });

      it('should accept null and undefined values', () => {
        const corsa: ICorsa = sampleWithRequiredData;
        expectedResult = service.addCorsaToCollectionIfMissing([], null, corsa, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(corsa);
      });

      it('should return initial array if no Corsa is added', () => {
        const corsaCollection: ICorsa[] = [sampleWithRequiredData];
        expectedResult = service.addCorsaToCollectionIfMissing(corsaCollection, undefined, null);
        expect(expectedResult).toEqual(corsaCollection);
      });
    });

    describe('compareCorsa', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCorsa(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCorsa(entity1, entity2);
        const compareResult2 = service.compareCorsa(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCorsa(entity1, entity2);
        const compareResult2 = service.compareCorsa(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCorsa(entity1, entity2);
        const compareResult2 = service.compareCorsa(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
