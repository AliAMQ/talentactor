import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Horse } from 'app/shared/model/horse.model';
import { HorseService } from './horse.service';
import { HorseComponent } from './horse.component';
import { HorseDetailComponent } from './horse-detail.component';
import { HorseUpdateComponent } from './horse-update.component';
import { HorseDeletePopupComponent } from './horse-delete-dialog.component';
import { IHorse } from 'app/shared/model/horse.model';

@Injectable({ providedIn: 'root' })
export class HorseResolve implements Resolve<IHorse> {
    constructor(private service: HorseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((horse: HttpResponse<Horse>) => horse.body);
        }
        return Observable.of(new Horse());
    }
}

export const horseRoute: Routes = [
    {
        path: 'horse',
        component: HorseComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.horse.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'horse/:id/view',
        component: HorseDetailComponent,
        resolve: {
            horse: HorseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.horse.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'horse/new',
        component: HorseUpdateComponent,
        resolve: {
            horse: HorseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.horse.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'horse/:id/edit',
        component: HorseUpdateComponent,
        resolve: {
            horse: HorseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.horse.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const horsePopupRoute: Routes = [
    {
        path: 'horse/:id/delete',
        component: HorseDeletePopupComponent,
        resolve: {
            horse: HorseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.horse.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
