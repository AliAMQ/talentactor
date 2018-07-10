import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProfile } from 'app/shared/model/profile.model';

type EntityResponseType = HttpResponse<IProfile>;
type EntityArrayResponseType = HttpResponse<IProfile[]>;

@Injectable({ providedIn: 'root' })
export class ProfileService {
    private resourceUrl = SERVER_API_URL + 'api/profiles';
    private resourceUrl2 = SERVER_API_URL + '/api/user-profiles-by-userid';

    constructor(private http: HttpClient) {}

    create(profile: IProfile): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(profile);
        return this.http
            .post<IProfile>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    update(profile: IProfile): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(profile);
        return this.http
            .put<IProfile>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    findByUserId(userId: number): Observable<EntityResponseType> {
        return this.http
            .get<IProfile>(`${this.resourceUrl2}/${userId}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertDateFromServer(res));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IProfile[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(profile: IProfile): IProfile {
        const copy: IProfile = Object.assign({}, profile, {
            since: profile.since != null && profile.since.isValid() ? profile.since.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.since = res.body.since != null ? moment(res.body.since) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((profile: IProfile) => {
            profile.since = profile.since != null ? moment(profile.since) : null;
        });
        return res;
    }
}
