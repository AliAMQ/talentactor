import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Combat } from 'app/shared/model/combat.model';
import { CombatService } from './combat.service';
import { CombatComponent } from './combat.component';
import { CombatDetailComponent } from './combat-detail.component';
import { CombatUpdateComponent } from './combat-update.component';
import { CombatDeletePopupComponent } from './combat-delete-dialog.component';
import { ICombat } from 'app/shared/model/combat.model';

@Injectable({ providedIn: 'root' })
export class CombatResolve implements Resolve<ICombat> {
    constructor(private service: CombatService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((combat: HttpResponse<Combat>) => combat.body);
        }
        return Observable.of(new Combat());
    }
}

export const combatRoute: Routes = [
    {
        path: 'combat',
        component: CombatComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.combat.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'combat/:id/view',
        component: CombatDetailComponent,
        resolve: {
            combat: CombatResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.combat.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'combat/new',
        component: CombatUpdateComponent,
        resolve: {
            combat: CombatResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.combat.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'combat/:id/edit',
        component: CombatUpdateComponent,
        resolve: {
            combat: CombatResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.combat.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const combatPopupRoute: Routes = [
    {
        path: 'combat/:id/delete',
        component: CombatDeletePopupComponent,
        resolve: {
            combat: CombatResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.combat.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
