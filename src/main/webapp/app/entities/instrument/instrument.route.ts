import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Instrument } from 'app/shared/model/instrument.model';
import { InstrumentService } from './instrument.service';
import { InstrumentComponent } from './instrument.component';
import { InstrumentDetailComponent } from './instrument-detail.component';
import { InstrumentUpdateComponent } from './instrument-update.component';
import { InstrumentDeletePopupComponent } from './instrument-delete-dialog.component';
import { IInstrument } from 'app/shared/model/instrument.model';

@Injectable({ providedIn: 'root' })
export class InstrumentResolve implements Resolve<IInstrument> {
    constructor(private service: InstrumentService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((instrument: HttpResponse<Instrument>) => instrument.body);
        }
        return Observable.of(new Instrument());
    }
}

export const instrumentRoute: Routes = [
    {
        path: 'instrument',
        component: InstrumentComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.instrument.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'instrument/:id/view',
        component: InstrumentDetailComponent,
        resolve: {
            instrument: InstrumentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.instrument.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'instrument/new',
        component: InstrumentUpdateComponent,
        resolve: {
            instrument: InstrumentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.instrument.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'instrument/:id/edit',
        component: InstrumentUpdateComponent,
        resolve: {
            instrument: InstrumentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.instrument.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const instrumentPopupRoute: Routes = [
    {
        path: 'instrument/:id/delete',
        component: InstrumentDeletePopupComponent,
        resolve: {
            instrument: InstrumentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.instrument.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
