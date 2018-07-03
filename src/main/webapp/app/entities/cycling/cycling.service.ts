import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICycling } from 'app/shared/model/cycling.model';

type EntityResponseType = HttpResponse<ICycling>;
type EntityArrayResponseType = HttpResponse<ICycling[]>;

@Injectable({ providedIn: 'root' })
export class CyclingService {
    private resourceUrl = SERVER_API_URL + 'api/cyclings';

    constructor(private http: HttpClient) {}

    create(cycling: ICycling): Observable<EntityResponseType> {
        return this.http.post<ICycling>(this.resourceUrl, cycling, { observe: 'response' });
    }

    update(cycling: ICycling): Observable<EntityResponseType> {
        return this.http.put<ICycling>(this.resourceUrl, cycling, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICycling>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICycling[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
