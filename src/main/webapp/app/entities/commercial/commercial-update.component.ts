import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ICommercial } from 'app/shared/model/commercial.model';
import { CommercialService } from './commercial.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

import { Principal } from 'app/core';

@Component({
    selector: 'jhi-commercial-update',
    templateUrl: './commercial-update.component.html'
})
export class CommercialUpdateComponent implements OnInit {
    private _commercial: ICommercial;
    isSaving: boolean;

    profiles: IProfile[];
    profileId: number;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private commercialService: CommercialService,
        private profileService: ProfileService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ commercial }) => {
            this.commercial = commercial;
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
        this.dataUtils.clearInputImage(this.commercial, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.profileService.findByUserId(this.principal.getId()).subscribe((res: HttpResponse<IProfile>) => {
            this.profileId = res.body.id;
            this.commercial.profileId = this.profileId;
            this.isSaving = true;
            if (this.commercial.id !== undefined) {
                this.subscribeToSaveResponse(this.commercialService.update(this.commercial));
            } else {
                this.subscribeToSaveResponse(this.commercialService.create(this.commercial));
            }
        });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICommercial>>) {
        result.subscribe((res: HttpResponse<ICommercial>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get commercial() {
        return this._commercial;
    }

    set commercial(commercial: ICommercial) {
        this._commercial = commercial;
    }
}
