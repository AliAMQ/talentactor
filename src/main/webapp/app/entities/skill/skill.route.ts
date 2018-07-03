import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Skill } from 'app/shared/model/skill.model';
import { SkillService } from './skill.service';
import { SkillComponent } from './skill.component';
import { SkillDetailComponent } from './skill-detail.component';
import { SkillUpdateComponent } from './skill-update.component';
import { SkillDeletePopupComponent } from './skill-delete-dialog.component';
import { ISkill } from 'app/shared/model/skill.model';

@Injectable({ providedIn: 'root' })
export class SkillResolve implements Resolve<ISkill> {
    constructor(private service: SkillService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((skill: HttpResponse<Skill>) => skill.body);
        }
        return Observable.of(new Skill());
    }
}

export const skillRoute: Routes = [
    {
        path: 'skill',
        component: SkillComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'talentactorApp.skill.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'skill/:id/view',
        component: SkillDetailComponent,
        resolve: {
            skill: SkillResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.skill.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'skill/new',
        component: SkillUpdateComponent,
        resolve: {
            skill: SkillResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.skill.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'skill/:id/edit',
        component: SkillUpdateComponent,
        resolve: {
            skill: SkillResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.skill.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const skillPopupRoute: Routes = [
    {
        path: 'skill/:id/delete',
        component: SkillDeletePopupComponent,
        resolve: {
            skill: SkillResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'talentactorApp.skill.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
