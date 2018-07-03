import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICircus } from 'app/shared/model/circus.model';

type EntityResponseType = HttpResponse<ICircus>;
type EntityArrayResponseType = HttpResponse<ICircus[]>;

@Injectable({ providedIn: 'root' })
export class CircusService {
    private resourceUrl = SERVER_API_URL + 'api/circuses';

    constructor(private http: HttpClient) {}

    create(circus: ICircus): Observable<EntityResponseType> {
        return this.http.post<ICircus>(this.resourceUrl, circus, { observe: 'response' });
    }

    update(circus: ICircus): Observable<EntityResponseType> {
        return this.http.put<ICircus>(this.resourceUrl, circus, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICircus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICircus[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
