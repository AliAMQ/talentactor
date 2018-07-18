import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { IProfile } from 'app/shared/model/profile.model';
import { Principal } from 'app/core';
import { HttpResponse } from '@angular/common/http';
import { IFilm } from 'app/shared/model/film.model';
import { ProfileService } from '../profile/profile.service';
import { FilmService } from 'app/entities/film/film.service';

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
    profileid: number;
    films: IFilm[];

    constructor(
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private profileService: ProfileService,
        private filmService: FilmService
    ) {}

    loadAllFilms() {
        this.profileService.findByUserId(this.principal.userIdentity.id).subscribe((res: HttpResponse<IProfile>) => {
            this.profileid = res.body.id;
            this.filmService
                .query({
                    'profileId.equals': this.profileid
                })
                .subscribe((res1: HttpResponse<IFilm[]>) => (this.films = res1.body));
        });
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ profile }) => {
            this.profile = profile;
            this.login = this.principal.userIdentity.login;
            this.firstName = this.principal.userIdentity.firstName;
            this.lastName = this.principal.userIdentity.lastName;
            this.email = this.principal.userIdentity.email;
        });
        this.loadAllFilms();
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
