import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPlicometria } from '../plicometria.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../plicometria.test-samples';

import { PlicometriaService, RestPlicometria } from './plicometria.service';

const requireRestSample: RestPlicometria = {
  ...sampleWithRequiredData,
  dataInserimento: sampleWithRequiredData.dataInserimento?.format(DATE_FORMAT),
};

describe('Plicometria Service', () => {
  let service: PlicometriaService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlicometria | IPlicometria[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PlicometriaService);
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

    it('should create a Plicometria', () => {
      const plicometria = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(plicometria).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Plicometria', () => {
      const plicometria = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(plicometria).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Plicometria', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Plicometria', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Plicometria', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPlicometriaToCollectionIfMissing', () => {
      it('should add a Plicometria to an empty array', () => {
        const plicometria: IPlicometria = sampleWithRequiredData;
        expectedResult = service.addPlicometriaToCollectionIfMissing([], plicometria);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(plicometria);
      });

      it('should not add a Plicometria to an array that contains it', () => {
        const plicometria: IPlicometria = sampleWithRequiredData;
        const plicometriaCollection: IPlicometria[] = [
          {
            ...plicometria,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlicometriaToCollectionIfMissing(plicometriaCollection, plicometria);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Plicometria to an array that doesn't contain it", () => {
        const plicometria: IPlicometria = sampleWithRequiredData;
        const plicometriaCollection: IPlicometria[] = [sampleWithPartialData];
        expectedResult = service.addPlicometriaToCollectionIfMissing(plicometriaCollection, plicometria);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(plicometria);
      });

      it('should add only unique Plicometria to an array', () => {
        const plicometriaArray: IPlicometria[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const plicometriaCollection: IPlicometria[] = [sampleWithRequiredData];
        expectedResult = service.addPlicometriaToCollectionIfMissing(plicometriaCollection, ...plicometriaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const plicometria: IPlicometria = sampleWithRequiredData;
        const plicometria2: IPlicometria = sampleWithPartialData;
        expectedResult = service.addPlicometriaToCollectionIfMissing([], plicometria, plicometria2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(plicometria);
        expect(expectedResult).toContain(plicometria2);
      });

      it('should accept null and undefined values', () => {
        const plicometria: IPlicometria = sampleWithRequiredData;
        expectedResult = service.addPlicometriaToCollectionIfMissing([], null, plicometria, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(plicometria);
      });

      it('should return initial array if no Plicometria is added', () => {
        const plicometriaCollection: IPlicometria[] = [sampleWithRequiredData];
        expectedResult = service.addPlicometriaToCollectionIfMissing(plicometriaCollection, undefined, null);
        expect(expectedResult).toEqual(plicometriaCollection);
      });
    });

    describe('comparePlicometria', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlicometria(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePlicometria(entity1, entity2);
        const compareResult2 = service.comparePlicometria(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePlicometria(entity1, entity2);
        const compareResult2 = service.comparePlicometria(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePlicometria(entity1, entity2);
        const compareResult2 = service.comparePlicometria(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
