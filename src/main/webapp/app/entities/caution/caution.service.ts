import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { ICaution } from 'app/shared/model/caution.model';

type EntityResponseType = HttpResponse<ICaution>;
type EntityArrayResponseType = HttpResponse<ICaution[]>;

@Injectable({ providedIn: 'root' })
export class CautionService {
  public resourceUrl = SERVER_API_URL + 'api/cautions';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/cautions';

  constructor(protected http: HttpClient) {}

  create(caution: ICaution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(caution);
    return this.http
      .post<ICaution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(caution: ICaution): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(caution);
    return this.http
      .put<ICaution>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICaution>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICaution[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICaution[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(caution: ICaution): ICaution {
    /* eslint-disable no-console */
    console.log(caution.dateDemande);
    /* eslint-enable no-console */
    const copy: ICaution = Object.assign({}, caution, {
      dateDemande: caution.dateDemande && caution.dateDemande.isValid() ? caution.dateDemande.format(DATE_FORMAT) : undefined,
      dateRetrait: caution.dateRetrait && caution.dateRetrait.isValid() ? caution.dateRetrait.format(DATE_FORMAT) : undefined,
      dateDepot: caution.dateDepot && caution.dateDepot.isValid() ? caution.dateDepot.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      /* eslint-disable no-console */
      console.log('res.body.dateDemande : ' + res.body.dateDemande);
      /* eslint-enable no-console */
      res.body.dateDemande = res.body.dateDemande ? moment(res.body.dateDemande) : undefined;
      res.body.dateRetrait = res.body.dateRetrait ? moment(res.body.dateRetrait) : undefined;
      res.body.dateDepot = res.body.dateDepot ? moment(res.body.dateDepot) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((caution: ICaution) => {
        /* eslint-disable no-console */
        console.log('convertDateArrayFromServer caution.dateDemande : ' + caution.dateDemande);
        /* eslint-enable no-console */
        caution.dateDemande = caution.dateDemande ? moment(caution.dateDemande) : undefined;
        caution.dateRetrait = caution.dateRetrait ? moment(caution.dateRetrait) : undefined;
        caution.dateDepot = caution.dateDepot ? moment(caution.dateDepot) : undefined;
      });
    }
    return res;
  }
}
