import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IVoice } from 'app/shared/model/voice.model';

type EntityResponseType = HttpResponse<IVoice>;
type EntityArrayResponseType = HttpResponse<IVoice[]>;

@Injectable({ providedIn: 'root' })
export class VoiceService {
    private resourceUrl = SERVER_API_URL + 'api/voices';

    constructor(private http: HttpClient) {}

    create(voice: IVoice): Observable<EntityResponseType> {
        return this.http.post<IVoice>(this.resourceUrl, voice, { observe: 'response' });
    }

    update(voice: IVoice): Observable<EntityResponseType> {
        return this.http.put<IVoice>(this.resourceUrl, voice, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IVoice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IVoice[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
