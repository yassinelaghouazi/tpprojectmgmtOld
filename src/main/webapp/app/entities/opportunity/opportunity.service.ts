import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IOpportunity } from 'app/shared/model/opportunity.model';

type EntityResponseType = HttpResponse<IOpportunity>;
type EntityArrayResponseType = HttpResponse<IOpportunity[]>;

@Injectable({ providedIn: 'root' })
export class OpportunityService {
  public resourceUrl = SERVER_API_URL + 'api/opportunities';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/opportunities';

  constructor(protected http: HttpClient) {}

  create(opportunity: IOpportunity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(opportunity);
    return this.http
      .post<IOpportunity>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(opportunity: IOpportunity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(opportunity);
    return this.http
      .put<IOpportunity>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOpportunity>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOpportunity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOpportunity[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(opportunity: IOpportunity): IOpportunity {
    const copy: IOpportunity = Object.assign({}, opportunity, {
      dateRemisePlis:
        opportunity.dateRemisePlis && opportunity.dateRemisePlis.isValid() ? opportunity.dateRemisePlis.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateRemisePlis = res.body.dateRemisePlis ? moment(res.body.dateRemisePlis) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((opportunity: IOpportunity) => {
        opportunity.dateRemisePlis = opportunity.dateRemisePlis ? moment(opportunity.dateRemisePlis) : undefined;
      });
    }
    return res;
  }
}
