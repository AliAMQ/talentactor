import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInternet } from 'app/shared/model/internet.model';

type EntityResponseType = HttpResponse<IInternet>;
type EntityArrayResponseType = HttpResponse<IInternet[]>;

@Injectable({ providedIn: 'root' })
export class InternetService {
    private resourceUrl = SERVER_API_URL + 'api/internets';

    constructor(private http: HttpClient) {}

    create(internet: IInternet): Observable<EntityResponseType> {
        return this.http.post<IInternet>(this.resourceUrl, internet, { observe: 'response' });
    }

    update(internet: IInternet): Observable<EntityResponseType> {
        return this.http.put<IInternet>(this.resourceUrl, internet, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IInternet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInternet[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
