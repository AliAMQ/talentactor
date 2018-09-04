import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';

import { VERSION } from 'app/app.constants';
import { JhiLanguageHelper, Principal, LoginModalService, LoginService } from 'app/core';
import { JhiProfileService } from '../profiles/profile.service';
import { ProfileService } from '../../entities/profile/profile.service';
import { IProfile } from 'app/shared/model/profile.model';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['navbar.scss']
})
export class NavbarComponent implements OnInit {
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    profileid: number;

    constructor(
        private loginService: LoginService,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper,
        private principal: Principal,
        private loginModalService: LoginModalService,
        private jhiprofileService: JhiProfileService,
        private router: Router,
        private profileService: ProfileService
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
    }

    ngOnInit() {
        this.findProfileByUserId();
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });

        this.jhiprofileService.getProfileInfo().then(profileInfo => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });
    }

    changeLanguage(languageKey: string) {
        this.languageService.changeLanguage(languageKey);
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['']);
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }

    findProfileByUserId() {
        this.profileService.getProfileId.subscribe(value => (this.profileid = value));
        if (this.profileid === 0) {
            this.principal.identity().then(account => {
                if (account !== null) {
                    this.profileService
                        .query({
                            'username.equals': account.login
                        })
                        .subscribe((res: HttpResponse<IProfile[]>) => {
                            this.profileid = res.body[0].id;
                        });
                }
            });
        }
    }
}
