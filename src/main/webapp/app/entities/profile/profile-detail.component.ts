import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { IProfile } from 'app/shared/model/profile.model';
import { Principal } from 'app/core';
import { FileManagementService } from '../../shared/file/file-management.service';
import { ProfileService } from './profile.service';

@Component({
    selector: 'jhi-profile-detail',
    templateUrl: './profile-detail.component.html'
})
export class ProfileDetailComponent implements OnInit {
    profile: IProfile;
    login;
    email: string;

    constructor(
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        public fileManagementService: FileManagementService,
        private profileService: ProfileService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ profile }) => {
            this.profile = profile;
            this.login = this.principal.getLogin();
            this.email = this.principal.getEmail();
        });
        this.profileService.setProfileId(this.profile.id);
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
