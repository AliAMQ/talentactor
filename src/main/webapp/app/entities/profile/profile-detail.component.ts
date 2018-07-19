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
import { IInternet } from 'app/shared/model/internet.model';
import { InternetService } from 'app/entities/internet/internet.service';
import { ICommercial } from 'app/shared/model/commercial.model';
import { CommercialService } from 'app/entities/commercial/commercial.service';
import { IPrint } from 'app/shared/model/print.model';
import { PrintService } from 'app/entities/print/print.service';
import { ITheater } from 'app/shared/model/theater.model';
import { TheaterService } from 'app/entities/theater/theater.service';
import { IVoice } from 'app/shared/model/voice.model';
import { VoiceService } from 'app/entities/voice/voice.service';

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
    films: IFilm[];
    televisions: ITelevision[];
    internets: IInternet[];
    commercials: ICommercial[];
    prints: IPrint[];
    theaters: ITheater[];
    voices: IVoice[];

    constructor(
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private profileService: ProfileService,
        private filmService: FilmService,
        private televisionService: TelevisionService,
        private internetService: InternetService,
        private commercialService: CommercialService,
        private printService: PrintService,
        private theaterService: TheaterService,
        private voiceService: VoiceService
    ) {}

    loadAllFilms() {
        this.filmService
            .query({
                'profileId.equals': this.profile.id
            })
            .subscribe((res1: HttpResponse<IFilm[]>) => (this.films = res1.body));
    }

    loadAllTelevisions() {
        this.televisionService
            .query({
                'profileId.equals': this.profile.id
            })
            .subscribe((res1: HttpResponse<ITelevision[]>) => (this.televisions = res1.body));
    }

    loadAllInternets() {
        this.internetService
            .query({
                'profileId.equals': this.profile.id
            })
            .subscribe((res1: HttpResponse<IInternet[]>) => (this.internets = res1.body));
    }

    loadAllCommercials() {
        this.commercialService
            .query({
                'profileId.equals': this.profile.id
            })
            .subscribe((res1: HttpResponse<ICommercial[]>) => (this.commercials = res1.body));
    }

    loadAllPrints() {
        this.printService
            .query({
                'profileId.equals': this.profile.id
            })
            .subscribe((res1: HttpResponse<IPrint[]>) => (this.prints = res1.body));
    }

    loadAllTheaters() {
        this.theaterService
            .query({
                'profileId.equals': this.profile.id
            })
            .subscribe((res1: HttpResponse<ITheater[]>) => (this.theaters = res1.body));
    }

    loadAllVoices() {
        this.voiceService
            .query({
                'profileId.equals': this.profile.id
            })
            .subscribe((res1: HttpResponse<IVoice[]>) => (this.voices = res1.body));
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
        this.loadAllInternets();
        this.loadAllCommercials();
        this.loadAllPrints();
        this.loadAllTheaters();
        this.loadAllVoices();
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
