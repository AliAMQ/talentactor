import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Swimming } from 'app/shared/model/swimming.model';
import { SwimmingService } from './swimming.service';
import { SwimmingComponent } from './swimming.component';
import { SwimmingDetailComponent } from './swimming-detail.component';
import { SwimmingUpdateComponent } from './swimming-update.component';
import { SwimmingDeletePopupComponent } from './swimming-delete-dialog.component';
import { ISwimming } from 'app/shared/model/swimming.model';

@Injectable({ providedIn: 'root' })
export class SwimmingResolve implements Resolve<ISwimming> {
    constructor(private service: SwimmingService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((swimming: HttpResponse<Swimming>) => swimming.body);
        }
        return Observable.of(new Swimming());
    }
}

export const swimmingRoute: Routes = [
    {
        path: 'swimming',
        component: SwimmingComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.swimming.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'swimming/:id/view',
        component: SwimmingDetailComponent,
        resolve: {
            swimming: SwimmingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.swimming.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'swimming/new',
        component: SwimmingUpdateComponent,
        resolve: {
            swimming: SwimmingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.swimming.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'swimming/:id/edit',
        component: SwimmingUpdateComponent,
        resolve: {
            swimming: SwimmingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.swimming.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const swimmingPopupRoute: Routes = [
    {
        path: 'swimming/:id/delete',
        component: SwimmingDeletePopupComponent,
        resolve: {
            swimming: SwimmingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.swimming.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
