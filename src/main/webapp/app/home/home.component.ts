import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { LoginModalService, Principal, Account } from 'app/core';

import { HttpResponse } from '@angular/common/http';
import { ProjectService } from 'app/entities/project/project.service';
import { IProject } from 'app/shared/model/project.model';
import { FileManagementService } from '../shared/file/file-management.service';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    project: IProject;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private projectService: ProjectService,
        private dataUtils: JhiDataUtils,
        public fileManagementService: FileManagementService
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.projectService.find(1).subscribe((res: HttpResponse<IProject>) => (this.project = res.body));
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
}
