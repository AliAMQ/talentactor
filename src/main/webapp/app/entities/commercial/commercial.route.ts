import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Commercial } from 'app/shared/model/commercial.model';
import { CommercialService } from './commercial.service';
import { CommercialComponent } from './commercial.component';
import { CommercialDetailComponent } from './commercial-detail.component';
import { CommercialUpdateComponent } from './commercial-update.component';
import { CommercialDeletePopupComponent } from './commercial-delete-dialog.component';
import { ICommercial } from 'app/shared/model/commercial.model';

@Injectable({ providedIn: 'root' })
export class CommercialResolve implements Resolve<ICommercial> {
    constructor(private service: CommercialService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((commercial: HttpResponse<Commercial>) => commercial.body);
        }
        return Observable.of(new Commercial());
    }
}

export const commercialRoute: Routes = [
    {
        path: 'commercial',
        component: CommercialComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.commercial.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'commercial/:id/view',
        component: CommercialDetailComponent,
        resolve: {
            commercial: CommercialResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.commercial.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'commercial/new',
        component: CommercialUpdateComponent,
        resolve: {
            commercial: CommercialResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.commercial.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'commercial/:id/edit',
        component: CommercialUpdateComponent,
        resolve: {
            commercial: CommercialResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.commercial.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const commercialPopupRoute: Routes = [
    {
        path: 'commercial/:id/delete',
        component: CommercialDeletePopupComponent,
        resolve: {
            commercial: CommercialResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.commercial.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
