import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Cycling } from 'app/shared/model/cycling.model';
import { CyclingService } from './cycling.service';
import { CyclingComponent } from './cycling.component';
import { CyclingDetailComponent } from './cycling-detail.component';
import { CyclingUpdateComponent } from './cycling-update.component';
import { CyclingDeletePopupComponent } from './cycling-delete-dialog.component';
import { ICycling } from 'app/shared/model/cycling.model';

@Injectable({ providedIn: 'root' })
export class CyclingResolve implements Resolve<ICycling> {
    constructor(private service: CyclingService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((cycling: HttpResponse<Cycling>) => cycling.body);
        }
        return Observable.of(new Cycling());
    }
}

export const cyclingRoute: Routes = [
    {
        path: 'cycling',
        component: CyclingComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.cycling.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cycling/:id/view',
        component: CyclingDetailComponent,
        resolve: {
            cycling: CyclingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.cycling.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cycling/new',
        component: CyclingUpdateComponent,
        resolve: {
            cycling: CyclingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.cycling.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cycling/:id/edit',
        component: CyclingUpdateComponent,
        resolve: {
            cycling: CyclingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.cycling.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cyclingPopupRoute: Routes = [
    {
        path: 'cycling/:id/delete',
        component: CyclingDeletePopupComponent,
        resolve: {
            cycling: CyclingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.cycling.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
