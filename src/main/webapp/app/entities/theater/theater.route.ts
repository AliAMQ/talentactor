import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Theater } from 'app/shared/model/theater.model';
import { TheaterService } from './theater.service';
import { TheaterComponent } from './theater.component';
import { TheaterDetailComponent } from './theater-detail.component';
import { TheaterUpdateComponent } from './theater-update.component';
import { TheaterDeletePopupComponent } from './theater-delete-dialog.component';
import { ITheater } from 'app/shared/model/theater.model';

@Injectable({ providedIn: 'root' })
export class TheaterResolve implements Resolve<ITheater> {
    constructor(private service: TheaterService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((theater: HttpResponse<Theater>) => theater.body);
        }
        return Observable.of(new Theater());
    }
}

export const theaterRoute: Routes = [
    {
        path: 'theater',
        component: TheaterComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.theater.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'theater/:id/view',
        component: TheaterDetailComponent,
        resolve: {
            theater: TheaterResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.theater.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'theater/new',
        component: TheaterUpdateComponent,
        resolve: {
            theater: TheaterResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.theater.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'theater/:id/edit',
        component: TheaterUpdateComponent,
        resolve: {
            theater: TheaterResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.theater.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const theaterPopupRoute: Routes = [
    {
        path: 'theater/:id/delete',
        component: TheaterDeletePopupComponent,
        resolve: {
            theater: TheaterResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.theater.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
