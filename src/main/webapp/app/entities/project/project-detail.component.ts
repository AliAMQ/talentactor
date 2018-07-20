import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IProject } from 'app/shared/model/project.model';

import { IRole } from 'app/shared/model/role.model';
import { RoleService } from 'app/entities/role/role.service';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-project-detail',
    templateUrl: './project-detail.component.html'
})
export class ProjectDetailComponent implements OnInit {
    project: IProject;
    roles: IRole[];

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute, private roleService: RoleService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ project }) => {
            this.project = project;
        });
        this.loadAllRoles();
    }

    loadAllRoles() {
        this.roleService
            .query({
                'projectId.equals': this.project.id
            })
            .subscribe((res1: HttpResponse<IRole[]>) => (this.roles = res1.body));
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
