import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Weapon } from 'app/shared/model/weapon.model';
import { WeaponService } from './weapon.service';
import { WeaponComponent } from './weapon.component';
import { WeaponDetailComponent } from './weapon-detail.component';
import { WeaponUpdateComponent } from './weapon-update.component';
import { WeaponDeletePopupComponent } from './weapon-delete-dialog.component';
import { IWeapon } from 'app/shared/model/weapon.model';

@Injectable({ providedIn: 'root' })
export class WeaponResolve implements Resolve<IWeapon> {
    constructor(private service: WeaponService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((weapon: HttpResponse<Weapon>) => weapon.body);
        }
        return Observable.of(new Weapon());
    }
}

export const weaponRoute: Routes = [
    {
        path: 'weapon',
        component: WeaponComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.weapon.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'weapon/:id/view',
        component: WeaponDetailComponent,
        resolve: {
            weapon: WeaponResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.weapon.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'weapon/new',
        component: WeaponUpdateComponent,
        resolve: {
            weapon: WeaponResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.weapon.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'weapon/:id/edit',
        component: WeaponUpdateComponent,
        resolve: {
            weapon: WeaponResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.weapon.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const weaponPopupRoute: Routes = [
    {
        path: 'weapon/:id/delete',
        component: WeaponDeletePopupComponent,
        resolve: {
            weapon: WeaponResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.weapon.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
