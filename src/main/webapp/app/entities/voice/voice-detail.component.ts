import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IVoice } from 'app/shared/model/voice.model';

@Component({
    selector: 'jhi-voice-detail',
    templateUrl: './voice-detail.component.html'
})
export class VoiceDetailComponent implements OnInit {
    voice: IVoice;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ voice }) => {
            this.voice = voice;
        });
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
