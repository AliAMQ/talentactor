import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPrint } from 'app/shared/model/print.model';

type EntityResponseType = HttpResponse<IPrint>;
type EntityArrayResponseType = HttpResponse<IPrint[]>;

@Injectable({ providedIn: 'root' })
export class PrintService {
    private resourceUrl = SERVER_API_URL + 'api/prints';

    constructor(private http: HttpClient) {}

    create(print: IPrint): Observable<EntityResponseType> {
        return this.http.post<IPrint>(this.resourceUrl, print, { observe: 'response' });
    }

    update(print: IPrint): Observable<EntityResponseType> {
        return this.http.put<IPrint>(this.resourceUrl, print, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPrint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPrint[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
