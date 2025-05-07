import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPesoCliente } from '../peso-cliente.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../peso-cliente.test-samples';

import { PesoClienteService, RestPesoCliente } from './peso-cliente.service';

const requireRestSample: RestPesoCliente = {
  ...sampleWithRequiredData,
  dataInserimento: sampleWithRequiredData.dataInserimento?.format(DATE_FORMAT),
};

describe('PesoCliente Service', () => {
  let service: PesoClienteService;
  let httpMock: HttpTestingController;
  let expectedResult: IPesoCliente | IPesoCliente[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PesoClienteService);
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

    it('should create a PesoCliente', () => {
      const pesoCliente = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pesoCliente).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PesoCliente', () => {
      const pesoCliente = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pesoCliente).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PesoCliente', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PesoCliente', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PesoCliente', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPesoClienteToCollectionIfMissing', () => {
      it('should add a PesoCliente to an empty array', () => {
        const pesoCliente: IPesoCliente = sampleWithRequiredData;
        expectedResult = service.addPesoClienteToCollectionIfMissing([], pesoCliente);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pesoCliente);
      });

      it('should not add a PesoCliente to an array that contains it', () => {
        const pesoCliente: IPesoCliente = sampleWithRequiredData;
        const pesoClienteCollection: IPesoCliente[] = [
          {
            ...pesoCliente,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPesoClienteToCollectionIfMissing(pesoClienteCollection, pesoCliente);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PesoCliente to an array that doesn't contain it", () => {
        const pesoCliente: IPesoCliente = sampleWithRequiredData;
        const pesoClienteCollection: IPesoCliente[] = [sampleWithPartialData];
        expectedResult = service.addPesoClienteToCollectionIfMissing(pesoClienteCollection, pesoCliente);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pesoCliente);
      });

      it('should add only unique PesoCliente to an array', () => {
        const pesoClienteArray: IPesoCliente[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pesoClienteCollection: IPesoCliente[] = [sampleWithRequiredData];
        expectedResult = service.addPesoClienteToCollectionIfMissing(pesoClienteCollection, ...pesoClienteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pesoCliente: IPesoCliente = sampleWithRequiredData;
        const pesoCliente2: IPesoCliente = sampleWithPartialData;
        expectedResult = service.addPesoClienteToCollectionIfMissing([], pesoCliente, pesoCliente2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pesoCliente);
        expect(expectedResult).toContain(pesoCliente2);
      });

      it('should accept null and undefined values', () => {
        const pesoCliente: IPesoCliente = sampleWithRequiredData;
        expectedResult = service.addPesoClienteToCollectionIfMissing([], null, pesoCliente, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pesoCliente);
      });

      it('should return initial array if no PesoCliente is added', () => {
        const pesoClienteCollection: IPesoCliente[] = [sampleWithRequiredData];
        expectedResult = service.addPesoClienteToCollectionIfMissing(pesoClienteCollection, undefined, null);
        expect(expectedResult).toEqual(pesoClienteCollection);
      });
    });

    describe('comparePesoCliente', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePesoCliente(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePesoCliente(entity1, entity2);
        const compareResult2 = service.comparePesoCliente(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePesoCliente(entity1, entity2);
        const compareResult2 = service.comparePesoCliente(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePesoCliente(entity1, entity2);
        const compareResult2 = service.comparePesoCliente(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
