import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITelevision } from 'app/shared/model/television.model';

type EntityResponseType = HttpResponse<ITelevision>;
type EntityArrayResponseType = HttpResponse<ITelevision[]>;

@Injectable({ providedIn: 'root' })
export class TelevisionService {
    private resourceUrl = SERVER_API_URL + 'api/televisions';

    constructor(private http: HttpClient) {}

    create(television: ITelevision): Observable<EntityResponseType> {
        return this.http.post<ITelevision>(this.resourceUrl, television, { observe: 'response' });
    }

    update(television: ITelevision): Observable<EntityResponseType> {
        return this.http.put<ITelevision>(this.resourceUrl, television, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITelevision>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITelevision[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
