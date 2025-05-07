import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ILezioneCorso } from '../lezione-corso.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../lezione-corso.test-samples';

import { LezioneCorsoService } from './lezione-corso.service';

const requireRestSample: ILezioneCorso = {
  ...sampleWithRequiredData,
};

describe('LezioneCorso Service', () => {
  let service: LezioneCorsoService;
  let httpMock: HttpTestingController;
  let expectedResult: ILezioneCorso | ILezioneCorso[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LezioneCorsoService);
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

    it('should create a LezioneCorso', () => {
      const lezioneCorso = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(lezioneCorso).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LezioneCorso', () => {
      const lezioneCorso = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(lezioneCorso).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LezioneCorso', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LezioneCorso', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LezioneCorso', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLezioneCorsoToCollectionIfMissing', () => {
      it('should add a LezioneCorso to an empty array', () => {
        const lezioneCorso: ILezioneCorso = sampleWithRequiredData;
        expectedResult = service.addLezioneCorsoToCollectionIfMissing([], lezioneCorso);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lezioneCorso);
      });

      it('should not add a LezioneCorso to an array that contains it', () => {
        const lezioneCorso: ILezioneCorso = sampleWithRequiredData;
        const lezioneCorsoCollection: ILezioneCorso[] = [
          {
            ...lezioneCorso,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLezioneCorsoToCollectionIfMissing(lezioneCorsoCollection, lezioneCorso);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LezioneCorso to an array that doesn't contain it", () => {
        const lezioneCorso: ILezioneCorso = sampleWithRequiredData;
        const lezioneCorsoCollection: ILezioneCorso[] = [sampleWithPartialData];
        expectedResult = service.addLezioneCorsoToCollectionIfMissing(lezioneCorsoCollection, lezioneCorso);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lezioneCorso);
      });

      it('should add only unique LezioneCorso to an array', () => {
        const lezioneCorsoArray: ILezioneCorso[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const lezioneCorsoCollection: ILezioneCorso[] = [sampleWithRequiredData];
        expectedResult = service.addLezioneCorsoToCollectionIfMissing(lezioneCorsoCollection, ...lezioneCorsoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lezioneCorso: ILezioneCorso = sampleWithRequiredData;
        const lezioneCorso2: ILezioneCorso = sampleWithPartialData;
        expectedResult = service.addLezioneCorsoToCollectionIfMissing([], lezioneCorso, lezioneCorso2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lezioneCorso);
        expect(expectedResult).toContain(lezioneCorso2);
      });

      it('should accept null and undefined values', () => {
        const lezioneCorso: ILezioneCorso = sampleWithRequiredData;
        expectedResult = service.addLezioneCorsoToCollectionIfMissing([], null, lezioneCorso, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lezioneCorso);
      });

      it('should return initial array if no LezioneCorso is added', () => {
        const lezioneCorsoCollection: ILezioneCorso[] = [sampleWithRequiredData];
        expectedResult = service.addLezioneCorsoToCollectionIfMissing(lezioneCorsoCollection, undefined, null);
        expect(expectedResult).toEqual(lezioneCorsoCollection);
      });
    });

    describe('compareLezioneCorso', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLezioneCorso(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLezioneCorso(entity1, entity2);
        const compareResult2 = service.compareLezioneCorso(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLezioneCorso(entity1, entity2);
        const compareResult2 = service.compareLezioneCorso(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLezioneCorso(entity1, entity2);
        const compareResult2 = service.compareLezioneCorso(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
