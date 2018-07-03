import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Circus } from 'app/shared/model/circus.model';
import { CircusService } from './circus.service';
import { CircusComponent } from './circus.component';
import { CircusDetailComponent } from './circus-detail.component';
import { CircusUpdateComponent } from './circus-update.component';
import { CircusDeletePopupComponent } from './circus-delete-dialog.component';
import { ICircus } from 'app/shared/model/circus.model';

@Injectable({ providedIn: 'root' })
export class CircusResolve implements Resolve<ICircus> {
    constructor(private service: CircusService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((circus: HttpResponse<Circus>) => circus.body);
        }
        return Observable.of(new Circus());
    }
}

export const circusRoute: Routes = [
    {
        path: 'circus',
        component: CircusComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.circus.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'circus/:id/view',
        component: CircusDetailComponent,
        resolve: {
            circus: CircusResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.circus.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'circus/new',
        component: CircusUpdateComponent,
        resolve: {
            circus: CircusResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.circus.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'circus/:id/edit',
        component: CircusUpdateComponent,
        resolve: {
            circus: CircusResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.circus.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const circusPopupRoute: Routes = [
    {
        path: 'circus/:id/delete',
        component: CircusDeletePopupComponent,
        resolve: {
            circus: CircusResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.circus.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
