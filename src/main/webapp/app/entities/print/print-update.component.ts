import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IPrint } from 'app/shared/model/print.model';
import { PrintService } from './print.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

import { Principal } from 'app/core';

@Component({
    selector: 'jhi-print-update',
    templateUrl: './print-update.component.html'
})
export class PrintUpdateComponent implements OnInit {
    private _print: IPrint;
    isSaving: boolean;

    profiles: IProfile[];
    profileId: number;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private printService: PrintService,
        private profileService: ProfileService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ print }) => {
            this.print = print;
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
        this.dataUtils.clearInputImage(this.print, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.profileService.findByUserId(this.principal.userIdentity.id).subscribe((res: HttpResponse<IProfile>) => {
            this.profileId = res.body.id;
            this.print.profileId = this.profileId;
            this.isSaving = true;
            if (this.print.id !== undefined) {
                this.subscribeToSaveResponse(this.printService.update(this.print));
            } else {
                this.subscribeToSaveResponse(this.printService.create(this.print));
            }
        });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPrint>>) {
        result.subscribe((res: HttpResponse<IPrint>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get print() {
        return this._print;
    }

    set print(print: IPrint) {
        this._print = print;
    }
}
