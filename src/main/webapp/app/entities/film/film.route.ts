import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Film } from 'app/shared/model/film.model';
import { FilmService } from './film.service';
import { FilmComponent } from './film.component';
import { FilmDetailComponent } from './film-detail.component';
import { FilmUpdateComponent } from './film-update.component';
import { FilmDeletePopupComponent } from './film-delete-dialog.component';
import { IFilm } from 'app/shared/model/film.model';

@Injectable({ providedIn: 'root' })
export class FilmResolve implements Resolve<IFilm> {
    constructor(private service: FilmService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((film: HttpResponse<Film>) => film.body);
        }
        return Observable.of(new Film());
    }
}

export const filmRoute: Routes = [
    {
        path: 'film',
        component: FilmComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.film.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'film/:id/view',
        component: FilmDetailComponent,
        resolve: {
            film: FilmResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.film.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'film/new',
        component: FilmUpdateComponent,
        resolve: {
            film: FilmResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.film.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'film/:id/edit',
        component: FilmUpdateComponent,
        resolve: {
            film: FilmResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.film.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const filmPopupRoute: Routes = [
    {
        path: 'film/:id/delete',
        component: FilmDeletePopupComponent,
        resolve: {
            film: FilmResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.film.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
