import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Television } from 'app/shared/model/television.model';
import { TelevisionService } from './television.service';
import { TelevisionComponent } from './television.component';
import { TelevisionDetailComponent } from './television-detail.component';
import { TelevisionUpdateComponent } from './television-update.component';
import { TelevisionDeletePopupComponent } from './television-delete-dialog.component';
import { ITelevision } from 'app/shared/model/television.model';

@Injectable({ providedIn: 'root' })
export class TelevisionResolve implements Resolve<ITelevision> {
    constructor(private service: TelevisionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((television: HttpResponse<Television>) => television.body);
        }
        return Observable.of(new Television());
    }
}

export const televisionRoute: Routes = [
    {
        path: 'television',
        component: TelevisionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.television.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'television/:id/view',
        component: TelevisionDetailComponent,
        resolve: {
            television: TelevisionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.television.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'television/new',
        component: TelevisionUpdateComponent,
        resolve: {
            television: TelevisionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.television.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'television/:id/edit',
        component: TelevisionUpdateComponent,
        resolve: {
            television: TelevisionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.television.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const televisionPopupRoute: Routes = [
    {
        path: 'television/:id/delete',
        component: TelevisionDeletePopupComponent,
        resolve: {
            television: TelevisionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.television.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
