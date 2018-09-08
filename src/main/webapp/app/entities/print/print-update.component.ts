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
import { FileManagementService } from 'app/shared/file/file-management.service';

@Component({
    selector: 'jhi-print-update',
    templateUrl: './print-update.component.html'
})
export class PrintUpdateComponent implements OnInit {
    private _print: IPrint;
    isSaving: boolean;

    profiles: IProfile[];
    profileId: number;
    selectedFiles: FileList;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private printService: PrintService,
        private profileService: ProfileService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        public fileManagementService: FileManagementService
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

    /*clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.print, this.elementRef, field, fieldContentType, idInput);
    }*/

    clearInputImage(image, button, input) {
        (document.getElementById(image) as HTMLImageElement).src = '';
        (document.getElementById(image) as HTMLImageElement).hidden = true;
        (document.getElementById(button) as HTMLButtonElement).hidden = true;
        (document.getElementById(input) as HTMLInputElement).value = null;
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.profileService.findByUserId(this.principal.getId()).subscribe((res: HttpResponse<IProfile>) => {
            this.profileId = res.body.id;
            this.print.profileId = this.profileId;
            this.isSaving = true;
            if (this.print.id !== undefined) {
                if (this.selectedFiles !== undefined) {
                    if (this.print.imagepath !== null) {
                        this.fileManagementService.deleteFile(this.print.imagepath);
                    }
                    if ((document.getElementById('imagepath1') as HTMLImageElement).hidden !== true) {
                        this.print.imagepath = this.upload('imagepath1');
                    } else {
                        this.print.imagepath = null;
                    }
                } else {
                    if ((document.getElementById('imagepath1') as HTMLImageElement).hidden === true) {
                        if (this.print.imagepath !== null) {
                            this.fileManagementService.deleteFile(this.print.imagepath);
                            this.print.imagepath = null;
                        }
                    }
                }
                this.subscribeToSaveResponse(this.printService.update(this.print));
            } else {
                if (this.selectedFiles !== undefined) {
                    if ((document.getElementById('imagepath1') as HTMLImageElement).hidden !== true) {
                        this.print.imagepath = this.upload('imagepath1');
                    }
                }
                this.subscribeToSaveResponse(this.printService.create(this.print));
            }
        });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPrint>>) {
        result.subscribe((res: HttpResponse<IPrint>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        global.setTimeout(this.previousState, 1000);
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

    selectFile(event, image, input) {
        const reader = new FileReader();
        reader.addEventListener(
            'load',
            function() {
                (document.getElementById(image) as HTMLImageElement).src = reader.result;
                (document.getElementById(image) as HTMLImageElement).hidden = false;
            },
            false
        );
        reader.readAsDataURL((document.getElementById(input) as HTMLInputElement).files[0]);
        if (image === 'imagepath1') {
            this.selectedFiles = event.target.files;
            (document.getElementById('close') as HTMLButtonElement).hidden = false;
        }
    }

    upload(media): string {
        let file;
        if (media === 'imagepath1') {
            file = this.selectedFiles.item(0);
        }
        return this.fileManagementService.uploadfile(file);
    }
}
