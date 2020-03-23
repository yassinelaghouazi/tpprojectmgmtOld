import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { MaitreOuvrageService } from 'app/entities/maitre-ouvrage/maitre-ouvrage.service';
import { IMaitreOuvrage, MaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';

describe('Service Tests', () => {
  describe('MaitreOuvrage Service', () => {
    let injector: TestBed;
    let service: MaitreOuvrageService;
    let httpMock: HttpTestingController;
    let elemDefault: IMaitreOuvrage;
    let expectedResult: IMaitreOuvrage | IMaitreOuvrage[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(MaitreOuvrageService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new MaitreOuvrage(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a MaitreOuvrage', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new MaitreOuvrage()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MaitreOuvrage', () => {
        const returnedFromService = Object.assign(
          {
            nom: 'BBBBBB',
            email: 'BBBBBB',
            tel: 'BBBBBB',
            contactPersonne: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MaitreOuvrage', () => {
        const returnedFromService = Object.assign(
          {
            nom: 'BBBBBB',
            email: 'BBBBBB',
            tel: 'BBBBBB',
            contactPersonne: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a MaitreOuvrage', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
