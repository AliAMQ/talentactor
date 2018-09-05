import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IFilm } from 'app/shared/model/film.model';
import { FilmService } from './film.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

import { Principal } from 'app/core';

import { FileManagementService } from '../../shared/file/file-management.service';

@Component({
    selector: 'jhi-film-update',
    templateUrl: './film-update.component.html'
})
export class FilmUpdateComponent implements OnInit {
    private _film: IFilm;
    isSaving: boolean;
    profiles: IProfile[];
    profileId: number;
    selectedFiles: FileList;
    selectedFiles2: FileList;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private filmService: FilmService,
        private profileService: ProfileService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        public fileManagementService: FileManagementService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ film }) => {
            this.film = film;
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
        this.dataUtils.clearInputImage(this.film, this.elementRef, field, fieldContentType, idInput);
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
            this.film.profileId = this.profileId;
            this.isSaving = true;
            if (this.film.id !== undefined) {
                if (this.selectedFiles !== undefined) {
                    console.log('-------aaaaaa--------' + this.selectedFiles);
                    if (this.film.imagepath !== null) {
                        console.log('-------bbbbbb--------' + this.film.imagepath);
                        this.fileManagementService.deleteFile(this.film.imagepath);
                    }
                    if ((document.getElementById('imagepath1') as HTMLImageElement).hidden !== true) {
                        this.film.imagepath = this.upload('imagepath1');
                        console.log('-------cccccc--------' + this.film.imagepath);
                    } else {
                        this.film.imagepath = null;
                    }
                } else {
                    if ((document.getElementById('imagepath1') as HTMLImageElement).hidden === true) {
                        if (this.film.imagepath !== null) {
                            console.log('-------dddddddd--------' + this.film.imagepath);
                            this.fileManagementService.deleteFile(this.film.imagepath);
                            this.film.imagepath = null;
                        }
                    }
                }
                if (this.selectedFiles2 !== undefined) {
                    console.log('-------11111--------' + this.film.videopath);
                    if (this.film.videopath !== null) {
                        this.fileManagementService.deleteFile(this.film.videopath);
                        console.log('-------22222--------' + this.film.videopath);
                    }
                    if ((document.getElementById('videopath1') as HTMLVideoElement).hidden !== true) {
                        this.film.videopath = this.upload('videopath1');
                        console.log('-------333333--------' + this.film.videopath);
                    } else {
                        console.log('---------4444444------' + this.film.videopath);
                        this.film.videopath = null;
                    }
                } else {
                    if ((document.getElementById('videopath1') as HTMLVideoElement).hidden === true) {
                        if (this.film.videopath !== null) {
                            console.log('-------5555--------' + this.film.videopath);
                            this.fileManagementService.deleteFile(this.film.videopath);
                            this.film.videopath = null;
                        }
                    }
                }
                this.subscribeToSaveResponse(this.filmService.update(this.film));
            } else {
                if (this.selectedFiles !== undefined) {
                    if ((document.getElementById('imagepath1') as HTMLImageElement).hidden !== true) {
                        this.film.imagepath = this.upload('imagepath1');
                    }
                }
                if (this.selectedFiles2 !== undefined) {
                    if ((document.getElementById('videopath1') as HTMLVideoElement).hidden !== true) {
                        this.film.videopath = this.upload('videopath1');
                    }
                }
                this.subscribeToSaveResponse(this.filmService.create(this.film));
            }
        });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFilm>>) {
        result.subscribe((res: HttpResponse<IFilm>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get film() {
        return this._film;
    }

    set film(film: IFilm) {
        this._film = film;
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
