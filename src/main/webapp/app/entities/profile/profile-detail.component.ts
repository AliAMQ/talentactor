import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { IProfile } from 'app/shared/model/profile.model';
import { Principal } from 'app/core';
import { HttpResponse } from '@angular/common/http';
import { ProfileService } from '../profile/profile.service';
import { IFilm } from 'app/shared/model/film.model';
import { FilmService } from 'app/entities/film/film.service';
import { ITelevision } from 'app/shared/model/television.model';
import { TelevisionService } from 'app/entities/television/television.service';

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
    televisions: ITelevision[];

    constructor(
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private profileService: ProfileService,
        private filmService: FilmService,
        private televisionService: TelevisionService
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

    loadAllTelevisions() {
        this.profileService.findByUserId(this.principal.userIdentity.id).subscribe((res: HttpResponse<IProfile>) => {
            this.profileid = res.body.id;
            this.televisionService
                .query({
                    'profileId.equals': this.profileid
                })
                .subscribe((res1: HttpResponse<ITelevision[]>) => (this.televisions = res1.body));
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
        this.loadAllTelevisions();
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
