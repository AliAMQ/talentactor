import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Voice } from 'app/shared/model/voice.model';
import { VoiceService } from './voice.service';
import { VoiceComponent } from './voice.component';
import { VoiceDetailComponent } from './voice-detail.component';
import { VoiceUpdateComponent } from './voice-update.component';
import { VoiceDeletePopupComponent } from './voice-delete-dialog.component';
import { IVoice } from 'app/shared/model/voice.model';

@Injectable({ providedIn: 'root' })
export class VoiceResolve implements Resolve<IVoice> {
    constructor(private service: VoiceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((voice: HttpResponse<Voice>) => voice.body);
        }
        return Observable.of(new Voice());
    }
}

export const voiceRoute: Routes = [
    {
        path: 'voice',
        component: VoiceComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.voice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'voice/:id/view',
        component: VoiceDetailComponent,
        resolve: {
            voice: VoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.voice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'voice/new',
        component: VoiceUpdateComponent,
        resolve: {
            voice: VoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.voice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'voice/:id/edit',
        component: VoiceUpdateComponent,
        resolve: {
            voice: VoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.voice.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const voicePopupRoute: Routes = [
    {
        path: 'voice/:id/delete',
        component: VoiceDeletePopupComponent,
        resolve: {
            voice: VoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.voice.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
