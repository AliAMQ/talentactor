import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IProject } from 'app/shared/model/project.model';

import { Principal } from 'app/core';
import { ProfileService } from 'app/entities/profile/profile.service';
import { IProfile } from 'app/shared/model/profile.model';
import { IRole } from 'app/shared/model/role.model';
import { RoleService } from 'app/entities/role/role.service';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-project-detail',
    templateUrl: './project-detail.component.html'
})
export class ProjectDetailComponent implements OnInit {
    project: IProject;
    profileId: number;
    roles: IRole[];

    constructor(
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private roleService: RoleService,
        private profileService: ProfileService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ project }) => {
            this.project = project;
        });
        this.loadAllRoles();
    }

    loadAllRoles() {
        this.profileService.findByUserId(this.principal.userIdentity.id).subscribe((res: HttpResponse<IProfile>) => {
            this.profileId = res.body.id;
            this.roleService
                .query({
                    'profileId.equals': this.profileId
                })
                .subscribe((res1: HttpResponse<IRole[]>) => (this.roles = res1.body));
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
