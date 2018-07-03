import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Internet } from 'app/shared/model/internet.model';
import { InternetService } from './internet.service';
import { InternetComponent } from './internet.component';
import { InternetDetailComponent } from './internet-detail.component';
import { InternetUpdateComponent } from './internet-update.component';
import { InternetDeletePopupComponent } from './internet-delete-dialog.component';
import { IInternet } from 'app/shared/model/internet.model';

@Injectable({ providedIn: 'root' })
export class InternetResolve implements Resolve<IInternet> {
    constructor(private service: InternetService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((internet: HttpResponse<Internet>) => internet.body);
        }
        return Observable.of(new Internet());
    }
}

export const internetRoute: Routes = [
    {
        path: 'internet',
        component: InternetComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.internet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'internet/:id/view',
        component: InternetDetailComponent,
        resolve: {
            internet: InternetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.internet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'internet/new',
        component: InternetUpdateComponent,
        resolve: {
            internet: InternetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.internet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'internet/:id/edit',
        component: InternetUpdateComponent,
        resolve: {
            internet: InternetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.internet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const internetPopupRoute: Routes = [
    {
        path: 'internet/:id/delete',
        component: InternetDeletePopupComponent,
        resolve: {
            internet: InternetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.internet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
