import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ITheater } from 'app/shared/model/theater.model';
import { TheaterService } from './theater.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

import { Principal } from 'app/core';

@Component({
    selector: 'jhi-theater-update',
    templateUrl: './theater-update.component.html'
})
export class TheaterUpdateComponent implements OnInit {
    private _theater: ITheater;
    isSaving: boolean;

    profiles: IProfile[];
    profileId: number;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private theaterService: TheaterService,
        private profileService: ProfileService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ theater }) => {
            this.theater = theater;
        });
        this.profileService.query().subscribe(
            (res: HttpResponse<IProfile[]>) => {
                this.profiles = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.theater, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.profileService.findByUserId(this.principal.userIdentity.id).subscribe((res: HttpResponse<IProfile>) => {
            this.profileId = res.body.id;
            this.theater.profileId = this.profileId;
            this.isSaving = true;
            if (this.theater.id !== undefined) {
                this.subscribeToSaveResponse(this.theaterService.update(this.theater));
            } else {
                this.subscribeToSaveResponse(this.theaterService.create(this.theater));
            }
        });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITheater>>) {
        result.subscribe((res: HttpResponse<ITheater>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackProfileById(index: number, item: IProfile) {
        return item.id;
    }
    get theater() {
        return this._theater;
    }

    set theater(theater: ITheater) {
        this._theater = theater;
    }
}
