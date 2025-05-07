import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICirconferenza } from '../circonferenza.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../circonferenza.test-samples';

import { CirconferenzaService, RestCirconferenza } from './circonferenza.service';

const requireRestSample: RestCirconferenza = {
  ...sampleWithRequiredData,
  dataInserimento: sampleWithRequiredData.dataInserimento?.format(DATE_FORMAT),
};

describe('Circonferenza Service', () => {
  let service: CirconferenzaService;
  let httpMock: HttpTestingController;
  let expectedResult: ICirconferenza | ICirconferenza[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CirconferenzaService);
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

    it('should create a Circonferenza', () => {
      const circonferenza = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(circonferenza).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Circonferenza', () => {
      const circonferenza = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(circonferenza).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Circonferenza', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Circonferenza', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Circonferenza', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCirconferenzaToCollectionIfMissing', () => {
      it('should add a Circonferenza to an empty array', () => {
        const circonferenza: ICirconferenza = sampleWithRequiredData;
        expectedResult = service.addCirconferenzaToCollectionIfMissing([], circonferenza);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(circonferenza);
      });

      it('should not add a Circonferenza to an array that contains it', () => {
        const circonferenza: ICirconferenza = sampleWithRequiredData;
        const circonferenzaCollection: ICirconferenza[] = [
          {
            ...circonferenza,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCirconferenzaToCollectionIfMissing(circonferenzaCollection, circonferenza);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Circonferenza to an array that doesn't contain it", () => {
        const circonferenza: ICirconferenza = sampleWithRequiredData;
        const circonferenzaCollection: ICirconferenza[] = [sampleWithPartialData];
        expectedResult = service.addCirconferenzaToCollectionIfMissing(circonferenzaCollection, circonferenza);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(circonferenza);
      });

      it('should add only unique Circonferenza to an array', () => {
        const circonferenzaArray: ICirconferenza[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const circonferenzaCollection: ICirconferenza[] = [sampleWithRequiredData];
        expectedResult = service.addCirconferenzaToCollectionIfMissing(circonferenzaCollection, ...circonferenzaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const circonferenza: ICirconferenza = sampleWithRequiredData;
        const circonferenza2: ICirconferenza = sampleWithPartialData;
        expectedResult = service.addCirconferenzaToCollectionIfMissing([], circonferenza, circonferenza2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(circonferenza);
        expect(expectedResult).toContain(circonferenza2);
      });

      it('should accept null and undefined values', () => {
        const circonferenza: ICirconferenza = sampleWithRequiredData;
        expectedResult = service.addCirconferenzaToCollectionIfMissing([], null, circonferenza, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(circonferenza);
      });

      it('should return initial array if no Circonferenza is added', () => {
        const circonferenzaCollection: ICirconferenza[] = [sampleWithRequiredData];
        expectedResult = service.addCirconferenzaToCollectionIfMissing(circonferenzaCollection, undefined, null);
        expect(expectedResult).toEqual(circonferenzaCollection);
      });
    });

    describe('compareCirconferenza', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCirconferenza(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCirconferenza(entity1, entity2);
        const compareResult2 = service.compareCirconferenza(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCirconferenza(entity1, entity2);
        const compareResult2 = service.compareCirconferenza(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCirconferenza(entity1, entity2);
        const compareResult2 = service.compareCirconferenza(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
