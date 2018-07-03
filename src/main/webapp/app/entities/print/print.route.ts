import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Print } from 'app/shared/model/print.model';
import { PrintService } from './print.service';
import { PrintComponent } from './print.component';
import { PrintDetailComponent } from './print-detail.component';
import { PrintUpdateComponent } from './print-update.component';
import { PrintDeletePopupComponent } from './print-delete-dialog.component';
import { IPrint } from 'app/shared/model/print.model';

@Injectable({ providedIn: 'root' })
export class PrintResolve implements Resolve<IPrint> {
    constructor(private service: PrintService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((print: HttpResponse<Print>) => print.body);
        }
        return Observable.of(new Print());
    }
}

export const printRoute: Routes = [
    {
        path: 'print',
        component: PrintComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.print.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'print/:id/view',
        component: PrintDetailComponent,
        resolve: {
            print: PrintResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.print.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'print/new',
        component: PrintUpdateComponent,
        resolve: {
            print: PrintResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.print.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'print/:id/edit',
        component: PrintUpdateComponent,
        resolve: {
            print: PrintResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.print.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const printPopupRoute: Routes = [
    {
        path: 'print/:id/delete',
        component: PrintDeletePopupComponent,
        resolve: {
            print: PrintResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.print.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
