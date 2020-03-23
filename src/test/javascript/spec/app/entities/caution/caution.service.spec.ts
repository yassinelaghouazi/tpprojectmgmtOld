import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { CautionService } from 'app/entities/caution/caution.service';
import { ICaution, Caution } from 'app/shared/model/caution.model';
import { TypeCaution } from 'app/shared/model/enumerations/type-caution.model';
import { StatusCaution } from 'app/shared/model/enumerations/status-caution.model';

describe('Service Tests', () => {
  describe('Caution Service', () => {
    let injector: TestBed;
    let service: CautionService;
    let httpMock: HttpTestingController;
    let elemDefault: ICaution;
    let expectedResult: ICaution | ICaution[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(CautionService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Caution(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        TypeCaution.PROVISOIRE,
        0,
        StatusCaution.DEMANDEE,
        currentDate,
        currentDate,
        currentDate
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateDemande: currentDate.format(DATE_FORMAT),
            dateRetrait: currentDate.format(DATE_FORMAT),
            dateDepot: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Caution', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateDemande: currentDate.format(DATE_FORMAT),
            dateRetrait: currentDate.format(DATE_FORMAT),
            dateDepot: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDemande: currentDate,
            dateRetrait: currentDate,
            dateDepot: currentDate
          },
          returnedFromService
        );

        service.create(new Caution()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Caution', () => {
        const returnedFromService = Object.assign(
          {
            numeroCaution: 'BBBBBB',
            numeroAppelOffre: 'BBBBBB',
            numeroMarche: 'BBBBBB',
            objetCaution: 'BBBBBB',
            typeCaution: 'BBBBBB',
            montantCaution: 1,
            statusCaution: 'BBBBBB',
            dateDemande: currentDate.format(DATE_FORMAT),
            dateRetrait: currentDate.format(DATE_FORMAT),
            dateDepot: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDemande: currentDate,
            dateRetrait: currentDate,
            dateDepot: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Caution', () => {
        const returnedFromService = Object.assign(
          {
            numeroCaution: 'BBBBBB',
            numeroAppelOffre: 'BBBBBB',
            numeroMarche: 'BBBBBB',
            objetCaution: 'BBBBBB',
            typeCaution: 'BBBBBB',
            montantCaution: 1,
            statusCaution: 'BBBBBB',
            dateDemande: currentDate.format(DATE_FORMAT),
            dateRetrait: currentDate.format(DATE_FORMAT),
            dateDepot: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDemande: currentDate,
            dateRetrait: currentDate,
            dateDepot: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Caution', () => {
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
