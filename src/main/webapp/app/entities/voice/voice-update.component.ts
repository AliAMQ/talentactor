import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IVoice } from 'app/shared/model/voice.model';
import { VoiceService } from './voice.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

import { Principal } from 'app/core';
import { FileManagementService } from 'app/shared/file/file-management.service';

@Component({
    selector: 'jhi-voice-update',
    templateUrl: './voice-update.component.html'
})
export class VoiceUpdateComponent implements OnInit {
    private _voice: IVoice;
    isSaving: boolean;

    profiles: IProfile[];
    profileId: number;
    selectedFiles: FileList;
    selectedFiles2: FileList;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private voiceService: VoiceService,
        private profileService: ProfileService,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        public fileManagementService: FileManagementService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ voice }) => {
            this.voice = voice;
        });
        this.profileService.query().subscribe(
            (res: HttpResponse<IProfile[]>) => {
                this.profiles = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    clearInputVideo(video, button, input) {
        (document.getElementById(video) as HTMLVideoElement).src = '';
        (document.getElementById(video) as HTMLVideoElement).hidden = true;
        (document.getElementById(button) as HTMLButtonElement).hidden = true;
        (document.getElementById(input) as HTMLInputElement).value = null;
    }

    clearInputAudio(audio, button, input) {
        (document.getElementById(audio) as HTMLAudioElement).src = '';
        (document.getElementById(audio) as HTMLAudioElement).hidden = true;
        (document.getElementById(button) as HTMLButtonElement).hidden = true;
        (document.getElementById(input) as HTMLInputElement).value = null;
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

    previousState() {
        window.history.back();
    }

    save() {
        this.profileService.findByUserId(this.principal.getId()).subscribe((res: HttpResponse<IProfile>) => {
            this.profileId = res.body.id;
            this.voice.profileId = this.profileId;
            this.isSaving = true;
            if (this.voice.id !== undefined) {
                if (this.selectedFiles !== undefined) {
                    if (this.voice.videopath !== null) {
                        this.fileManagementService.deleteFile(this.voice.videopath);
                    }
                    if ((document.getElementById('videopath1') as HTMLVideoElement).hidden !== true) {
                        this.voice.videopath = this.upload('videopath1');
                    } else {
                        this.voice.videopath = null;
                    }
                } else {
                    if ((document.getElementById('videopath1') as HTMLVideoElement).hidden === true) {
                        if (this.voice.videopath !== null) {
                            this.fileManagementService.deleteFile(this.voice.videopath);
                            this.voice.videopath = null;
                        }
                    }
                }
                if (this.selectedFiles2 !== undefined) {
                    if (this.voice.audiopath !== null) {
                        this.fileManagementService.deleteFile(this.voice.audiopath);
                    }
                    if ((document.getElementById('audiopath1') as HTMLAudioElement).hidden !== true) {
                        this.voice.audiopath = this.upload('audiopath1');
                    } else {
                        this.voice.audiopath = null;
                    }
                } else {
                    if ((document.getElementById('audiopath1') as HTMLAudioElement).hidden === true) {
                        if (this.voice.audiopath !== null) {
                            this.fileManagementService.deleteFile(this.voice.audiopath);
                            this.voice.audiopath = null;
                        }
                    }
                }
                this.subscribeToSaveResponse(this.voiceService.update(this.voice));
            } else {
                if (this.selectedFiles !== undefined) {
                    if ((document.getElementById('videopath1') as HTMLVideoElement).hidden !== true) {
                        this.voice.videopath = this.upload('videopath1');
                    }
                }
                if (this.selectedFiles2 !== undefined) {
                    if ((document.getElementById('audiopath1') as HTMLAudioElement).hidden !== true) {
                        this.voice.audiopath = this.upload('audiopath1');
                    }
                }
                this.subscribeToSaveResponse(this.voiceService.create(this.voice));
            }
        });
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IVoice>>) {
        result.subscribe((res: HttpResponse<IVoice>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get voice() {
        return this._voice;
    }

    set voice(voice: IVoice) {
        this._voice = voice;
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
            this.selectedFiles = event.target.files;
            (document.getElementById('close') as HTMLButtonElement).hidden = false;
        }
    }

    selectAudioFile(event, audio, input) {
        const reader = new FileReader();
        reader.addEventListener(
            'load',
            function() {
                (document.getElementById(audio) as HTMLAudioElement).src = reader.result;
                (document.getElementById(audio) as HTMLAudioElement).hidden = false;
            },
            false
        );
        reader.readAsDataURL((document.getElementById(input) as HTMLInputElement).files[0]);
        if (audio === 'audiopath1') {
            this.selectedFiles2 = event.target.files;
            (document.getElementById('close2') as HTMLButtonElement).hidden = false;
        }
    }

    upload(media): string {
        let file;
        if (media === 'videopath1') {
            file = this.selectedFiles.item(0);
        }
        if (media === 'audiopath1') {
            file = this.selectedFiles2.item(0);
        }
        return this.fileManagementService.uploadfile(file);
    }
}
