import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IBanque } from 'app/shared/model/banque.model';

type EntityResponseType = HttpResponse<IBanque>;
type EntityArrayResponseType = HttpResponse<IBanque[]>;

@Injectable({ providedIn: 'root' })
export class BanqueService {
  public resourceUrl = SERVER_API_URL + 'api/banques';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/banques';

  constructor(protected http: HttpClient) {}

  create(banque: IBanque): Observable<EntityResponseType> {
    return this.http.post<IBanque>(this.resourceUrl, banque, { observe: 'response' });
  }

  update(banque: IBanque): Observable<EntityResponseType> {
    return this.http.put<IBanque>(this.resourceUrl, banque, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBanque>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBanque[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBanque[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
