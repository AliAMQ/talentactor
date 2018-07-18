import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { IProfile } from 'app/shared/model/profile.model';
import { IUser, Principal } from 'app/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-profile-detail',
    templateUrl: './profile-detail.component.html'
})
export class ProfileDetailComponent implements OnInit {
    profile: IProfile;
    login;
    firstName;
    lastName;
    email: string;
    // user: IUser;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute, private principal: Principal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ profile }) => {
            this.profile = profile;
            this.login = this.principal.userIdentity.login;
            this.firstName = this.principal.userIdentity.firstName;
            this.lastName = this.principal.userIdentity.lastName;
            this.email = this.principal.userIdentity.email;
            // console.log(this.principal.userIdentity);
            /*this.profileService.findByUserId(this.principal.userIdentity.id).subscribe((res: HttpResponse<IProfile>) => {
            this.profileid = res.body.id;
            this.roleService
                .query({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    'profileId.equals': this.profileid
                })
                .subscribe(
                    (res1: HttpResponse<IRole[]>) => this.paginateRoles(res1.body, res1.headers),
                    (res1: HttpErrorResponse) => this.onError(res1.message)
                );
            });*/
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
