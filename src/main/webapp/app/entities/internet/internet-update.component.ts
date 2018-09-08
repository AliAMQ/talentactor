import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IInternet } from 'app/shared/model/internet.model';
import { InternetService } from './internet.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

import { Principal } from 'app/core';
import { FileManagementService } from 'app/shared/file/file-management.service';

@Component({
    selector: 'jhi-internet-update',
    templateUrl: './internet-update.component.html'
})
export class InternetUpdateComponent implements OnInit {
    private _internet: IInternet;
    isSaving: boolean;

    profiles: IProfile[];
    profileId: number;
    selectedFiles: FileList;
    selectedFiles2: FileList;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private internetService: InternetService,
        private profileService: ProfileService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        public fileManagementService: FileManagementService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ internet }) => {
            this.internet = internet;
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
        this.dataUtils.clearInputImage(this.internet, this.elementRef, field, fieldContentType, idInput);
    }*/

    clearInputImage(image, button, input) {
        (document.getElementById(image) as HTMLImageElement).src = '';
        (document.getElementById(image) as HTMLImageElement).hidden = true;
        (document.getElementById(button) as HTMLButtonElement).hidden = true;
        (document.getElementById(input) as HTMLInputElement).value = null;
    }

    clearInputVideo(video, button, input) {
        (document.getElementById(video) as HTMLVideoElement).src = '';
        (document.getElementById(video) as HTMLVideoElement).hidden = true;
        (document.getElementById(button) as HTMLButtonElement).hidden = true;
        (document.getElementById(input) as HTMLInputElement).value = null;
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.profileService.findByUserId(this.principal.getId()).subscribe((res: HttpResponse<IProfile>) => {
            this.profileId = res.body.id;
            this.internet.profileId = this.profileId;
            this.isSaving = true;
            if (this.internet.id !== undefined) {
                if (this.selectedFiles !== undefined) {
                    if (this.internet.imagepath !== null) {
                        this.fileManagementService.deleteFile(this.internet.imagepath);
                    }
                    if ((document.getElementById('imagepath1') as HTMLImageElement).hidden !== true) {
                        this.internet.imagepath = this.upload('imagepath1');
                    } else {
                        this.internet.imagepath = null;
                    }
                } else {
                    if ((document.getElementById('imagepath1') as HTMLImageElement).hidden === true) {
                        if (this.internet.imagepath !== null) {
                            this.fileManagementService.deleteFile(this.internet.imagepath);
                            this.internet.imagepath = null;
                        }
                    }
                }
                if (this.selectedFiles2 !== undefined) {
                    if (this.internet.videopath !== null) {
                        this.fileManagementService.deleteFile(this.internet.videopath);
                    }
                    if ((document.getElementById('videopath1') as HTMLVideoElement).hidden !== true) {
                        this.internet.videopath = this.upload('videopath1');
                    } else {
                        this.internet.videopath = null;
                    }
                } else {
                    if ((document.getElementById('videopath1') as HTMLVideoElement).hidden === true) {
                        if (this.internet.videopath !== null) {
                            this.fileManagementService.deleteFile(this.internet.videopath);
                            this.internet.videopath = null;
                        }
                    }
                }
                this.subscribeToSaveResponse(this.internetService.update(this.internet));
            } else {
                if (this.selectedFiles !== undefined) {
                    if ((document.getElementById('imagepath1') as HTMLImageElement).hidden !== true) {
                        this.internet.imagepath = this.upload('imagepath1');
                    }
                }
                if (this.selectedFiles2 !== undefined) {
                    if ((document.getElementById('videopath1') as HTMLVideoElement).hidden !== true) {
                        this.internet.videopath = this.upload('videopath1');
                    }
                }
                this.subscribeToSaveResponse(this.internetService.create(this.internet));
            }
        });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IInternet>>) {
        result.subscribe((res: HttpResponse<IInternet>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get internet() {
        return this._internet;
    }

    set internet(internet: IInternet) {
        this._internet = internet;
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
        if (image === 'videopath1') {
            this.selectedFiles2 = event.target.files;
            (document.getElementById('close2') as HTMLButtonElement).hidden = false;
        }
    }

    selectVideoFile(event, video, input) {
        const reader = new FileReader();
        reader.addEventListener(
            'load',
            function() {
                (document.getElementById(video) as HTMLVideoElement).src = reader.result;
                (document.getElementById(video) as HTMLVideoElement).hidden = false;
            },
            false
        );
        reader.readAsDataURL((document.getElementById(input) as HTMLInputElement).files[0]);
        if (video === 'videopath1') {
            this.selectedFiles2 = event.target.files;
            (document.getElementById('close2') as HTMLButtonElement).hidden = false;
        }
    }

    upload(media): string {
        let file;
        if (media === 'imagepath1') {
            file = this.selectedFiles.item(0);
        }
        if (media === 'videopath1') {
            file = this.selectedFiles2.item(0);
        }
        return this.fileManagementService.uploadfile(file);
    }
}
