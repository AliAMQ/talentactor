import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from './project.service';
import { FileManagementService } from '../../shared/file/file-management.service';

@Component({
    selector: 'jhi-project-update',
    templateUrl: './project-update.component.html'
})
export class ProjectUpdateComponent implements OnInit {
    private _project: IProject;
    isSaving: boolean;
    dateDp: any;
    selectedFiles: FileList;
    selectedFiles2: FileList;

    constructor(
        private dataUtils: JhiDataUtils,
        private projectService: ProjectService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute,
        public fileManagementService: FileManagementService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ project }) => {
            this.project = project;
        });
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
        this.dataUtils.clearInputImage(this.project, this.elementRef, field, fieldContentType, idInput);
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
        this.isSaving = true;
        if (this.project.id !== undefined) {
            if (this.selectedFiles !== undefined) {
                if (this.project.imagepath !== null) {
                    this.fileManagementService.deleteFile(this.project.imagepath);
                }
                if ((document.getElementById('imagepath1') as HTMLImageElement).hidden !== true) {
                    this.project.imagepath = this.upload('imagepath1');
                } else {
                    this.project.imagepath = null;
                }
            } else {
                if ((document.getElementById('imagepath1') as HTMLImageElement).hidden === true) {
                    if (this.project.imagepath !== null) {
                        this.fileManagementService.deleteFile(this.project.imagepath);
                        this.project.imagepath = null;
                    }
                }
            }
            if (this.selectedFiles2 !== undefined) {
                if (this.project.videopath !== null) {
                    this.fileManagementService.deleteFile(this.project.videopath);
                }
                if ((document.getElementById('videopath1') as HTMLVideoElement).hidden !== true) {
                    this.project.videopath = this.upload('videopath1');
                } else {
                    this.project.videopath = null;
                }
            } else {
                if ((document.getElementById('videopath1') as HTMLVideoElement).hidden === true) {
                    if (this.project.videopath !== null) {
                        this.fileManagementService.deleteFile(this.project.videopath);
                        this.project.videopath = null;
                    }
                }
            }
            this.subscribeToSaveResponse(this.projectService.update(this.project));
        } else {
            if (this.selectedFiles !== undefined) {
                if ((document.getElementById('imagepath1') as HTMLImageElement).hidden !== true) {
                    this.project.imagepath = this.upload('imagepath1');
                }
            }
            if (this.selectedFiles2 !== undefined) {
                if ((document.getElementById('videopath1') as HTMLVideoElement).hidden !== true) {
                    this.project.videopath = this.upload('videopath1');
                }
            }
            this.subscribeToSaveResponse(this.projectService.create(this.project));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>) {
        result.subscribe((res: HttpResponse<IProject>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        global.setTimeout(this.previousState, 1000);
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get project() {
        return this._project;
    }

    set project(project: IProject) {
        this._project = project;
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
