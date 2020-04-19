import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { OpportunityService } from 'app/entities/opportunity/opportunity.service';
import { IOpportunity, Opportunity } from 'app/shared/model/opportunity.model';
import { Caution } from 'app/shared/model/caution.model';
import { TypeCaution } from 'app/shared/model/enumerations/type-caution.model';
import { StatusCaution } from 'app/shared/model/enumerations/status-caution.model';

describe('Service Tests', () => {
  describe('Opportunity Service', () => {
    let injector: TestBed;
    let service: OpportunityService;
    let httpMock: HttpTestingController;
    let elemDefault: IOpportunity;
    let expectedResult: IOpportunity | IOpportunity[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(OpportunityService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Opportunity(0, 'AAAAAAA', currentDate, 0, 'AAAAAAA', 0, 'AAAAAAA');
      const caution: Caution = new Caution(
        0,
        '123456789',
        '123456',
        '1234',
        '123',
        TypeCaution.DEFINITIVE,
        1000000,
        StatusCaution.DEMANDEE,
        currentDate,
        currentDate,
        currentDate,
        undefined,
        undefined
      );
      elemDefault.caution = caution;
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateRemisePlis: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Opportunity', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateRemisePlis: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateRemisePlis: currentDate
          },
          returnedFromService
        );

        service.create(new Opportunity()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Opportunity', () => {
        const returnedFromService = Object.assign(
          {
            numeroAppelOffre: 'BBBBBB',
            dateRemisePlis: currentDate.format(DATE_FORMAT),
            montantCaution: 1,
            objetAffaire: 'BBBBBB',
            estimationBudget: 1,
            commentaires: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateRemisePlis: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Opportunity', () => {
        const returnedFromService = Object.assign(
          {
            numeroAppelOffre: 'BBBBBB',
            dateRemisePlis: currentDate.format(DATE_FORMAT),
            montantCaution: 1,
            objetAffaire: 'BBBBBB',
            estimationBudget: 1,
            commentaires: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateRemisePlis: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Opportunity', () => {
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
