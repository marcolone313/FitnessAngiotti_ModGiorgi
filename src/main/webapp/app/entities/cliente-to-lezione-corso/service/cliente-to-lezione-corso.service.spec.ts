import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IClienteToLezioneCorso } from '../cliente-to-lezione-corso.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../cliente-to-lezione-corso.test-samples';

import { ClienteToLezioneCorsoService, RestClienteToLezioneCorso } from './cliente-to-lezione-corso.service';

const requireRestSample: RestClienteToLezioneCorso = {
  ...sampleWithRequiredData,
  dataCompletamento: sampleWithRequiredData.dataCompletamento?.format(DATE_FORMAT),
};

describe('ClienteToLezioneCorso Service', () => {
  let service: ClienteToLezioneCorsoService;
  let httpMock: HttpTestingController;
  let expectedResult: IClienteToLezioneCorso | IClienteToLezioneCorso[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ClienteToLezioneCorsoService);
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

    it('should create a ClienteToLezioneCorso', () => {
      const clienteToLezioneCorso = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(clienteToLezioneCorso).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ClienteToLezioneCorso', () => {
      const clienteToLezioneCorso = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(clienteToLezioneCorso).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ClienteToLezioneCorso', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ClienteToLezioneCorso', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ClienteToLezioneCorso', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addClienteToLezioneCorsoToCollectionIfMissing', () => {
      it('should add a ClienteToLezioneCorso to an empty array', () => {
        const clienteToLezioneCorso: IClienteToLezioneCorso = sampleWithRequiredData;
        expectedResult = service.addClienteToLezioneCorsoToCollectionIfMissing([], clienteToLezioneCorso);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(clienteToLezioneCorso);
      });

      it('should not add a ClienteToLezioneCorso to an array that contains it', () => {
        const clienteToLezioneCorso: IClienteToLezioneCorso = sampleWithRequiredData;
        const clienteToLezioneCorsoCollection: IClienteToLezioneCorso[] = [
          {
            ...clienteToLezioneCorso,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addClienteToLezioneCorsoToCollectionIfMissing(clienteToLezioneCorsoCollection, clienteToLezioneCorso);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ClienteToLezioneCorso to an array that doesn't contain it", () => {
        const clienteToLezioneCorso: IClienteToLezioneCorso = sampleWithRequiredData;
        const clienteToLezioneCorsoCollection: IClienteToLezioneCorso[] = [sampleWithPartialData];
        expectedResult = service.addClienteToLezioneCorsoToCollectionIfMissing(clienteToLezioneCorsoCollection, clienteToLezioneCorso);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(clienteToLezioneCorso);
      });

      it('should add only unique ClienteToLezioneCorso to an array', () => {
        const clienteToLezioneCorsoArray: IClienteToLezioneCorso[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const clienteToLezioneCorsoCollection: IClienteToLezioneCorso[] = [sampleWithRequiredData];
        expectedResult = service.addClienteToLezioneCorsoToCollectionIfMissing(
          clienteToLezioneCorsoCollection,
          ...clienteToLezioneCorsoArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const clienteToLezioneCorso: IClienteToLezioneCorso = sampleWithRequiredData;
        const clienteToLezioneCorso2: IClienteToLezioneCorso = sampleWithPartialData;
        expectedResult = service.addClienteToLezioneCorsoToCollectionIfMissing([], clienteToLezioneCorso, clienteToLezioneCorso2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(clienteToLezioneCorso);
        expect(expectedResult).toContain(clienteToLezioneCorso2);
      });

      it('should accept null and undefined values', () => {
        const clienteToLezioneCorso: IClienteToLezioneCorso = sampleWithRequiredData;
        expectedResult = service.addClienteToLezioneCorsoToCollectionIfMissing([], null, clienteToLezioneCorso, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(clienteToLezioneCorso);
      });

      it('should return initial array if no ClienteToLezioneCorso is added', () => {
        const clienteToLezioneCorsoCollection: IClienteToLezioneCorso[] = [sampleWithRequiredData];
        expectedResult = service.addClienteToLezioneCorsoToCollectionIfMissing(clienteToLezioneCorsoCollection, undefined, null);
        expect(expectedResult).toEqual(clienteToLezioneCorsoCollection);
      });
    });

    describe('compareClienteToLezioneCorso', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareClienteToLezioneCorso(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareClienteToLezioneCorso(entity1, entity2);
        const compareResult2 = service.compareClienteToLezioneCorso(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareClienteToLezioneCorso(entity1, entity2);
        const compareResult2 = service.compareClienteToLezioneCorso(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareClienteToLezioneCorso(entity1, entity2);
        const compareResult2 = service.compareClienteToLezioneCorso(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
