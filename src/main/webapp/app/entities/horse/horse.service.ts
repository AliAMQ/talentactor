import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IHorse } from 'app/shared/model/horse.model';

type EntityResponseType = HttpResponse<IHorse>;
type EntityArrayResponseType = HttpResponse<IHorse[]>;

@Injectable({ providedIn: 'root' })
export class HorseService {
    private resourceUrl = SERVER_API_URL + 'api/horses';

    constructor(private http: HttpClient) {}

    create(horse: IHorse): Observable<EntityResponseType> {
        return this.http.post<IHorse>(this.resourceUrl, horse, { observe: 'response' });
    }

    update(horse: IHorse): Observable<EntityResponseType> {
        return this.http.put<IHorse>(this.resourceUrl, horse, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IHorse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHorse[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
