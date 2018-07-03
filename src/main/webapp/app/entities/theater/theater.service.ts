import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITheater } from 'app/shared/model/theater.model';

type EntityResponseType = HttpResponse<ITheater>;
type EntityArrayResponseType = HttpResponse<ITheater[]>;

@Injectable({ providedIn: 'root' })
export class TheaterService {
    private resourceUrl = SERVER_API_URL + 'api/theaters';

    constructor(private http: HttpClient) {}

    create(theater: ITheater): Observable<EntityResponseType> {
        return this.http.post<ITheater>(this.resourceUrl, theater, { observe: 'response' });
    }

    update(theater: ITheater): Observable<EntityResponseType> {
        return this.http.put<ITheater>(this.resourceUrl, theater, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITheater>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITheater[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
