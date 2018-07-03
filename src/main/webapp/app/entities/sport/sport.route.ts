import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Sport } from 'app/shared/model/sport.model';
import { SportService } from './sport.service';
import { SportComponent } from './sport.component';
import { SportDetailComponent } from './sport-detail.component';
import { SportUpdateComponent } from './sport-update.component';
import { SportDeletePopupComponent } from './sport-delete-dialog.component';
import { ISport } from 'app/shared/model/sport.model';

@Injectable({ providedIn: 'root' })
export class SportResolve implements Resolve<ISport> {
    constructor(private service: SportService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((sport: HttpResponse<Sport>) => sport.body);
        }
        return Observable.of(new Sport());
    }
}

export const sportRoute: Routes = [
    {
        path: 'sport',
        component: SportComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.sport.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sport/:id/view',
        component: SportDetailComponent,
        resolve: {
            sport: SportResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.sport.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sport/new',
        component: SportUpdateComponent,
        resolve: {
            sport: SportResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.sport.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sport/:id/edit',
        component: SportUpdateComponent,
        resolve: {
            sport: SportResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.sport.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sportPopupRoute: Routes = [
    {
        path: 'sport/:id/delete',
        component: SportDeletePopupComponent,
        resolve: {
            sport: SportResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.sport.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
