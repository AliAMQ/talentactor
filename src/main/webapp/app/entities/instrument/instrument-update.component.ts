import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IInstrument } from 'app/shared/model/instrument.model';
import { InstrumentService } from './instrument.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

@Component({
    selector: 'jhi-instrument-update',
    templateUrl: './instrument-update.component.html'
})
export class InstrumentUpdateComponent implements OnInit {
    private _instrument: IInstrument;
    isSaving: boolean;

    profiles: IProfile[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private instrumentService: InstrumentService,
        private profileService: ProfileService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ instrument }) => {
            this.instrument = instrument;
        });
        this.profileService.query().subscribe(
            (res: HttpResponse<IProfile[]>) => {
                this.profiles = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.instrument.id !== undefined) {
            this.subscribeToSaveResponse(this.instrumentService.update(this.instrument));
        } else {
            this.subscribeToSaveResponse(this.instrumentService.create(this.instrument));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IInstrument>>) {
        result.subscribe((res: HttpResponse<IInstrument>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get instrument() {
        return this._instrument;
    }

    set instrument(instrument: IInstrument) {
        this._instrument = instrument;
    }
}
