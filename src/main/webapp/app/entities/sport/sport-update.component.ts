import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ISport } from 'app/shared/model/sport.model';
import { SportService } from './sport.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

@Component({
    selector: 'jhi-sport-update',
    templateUrl: './sport-update.component.html'
})
export class SportUpdateComponent implements OnInit {
    private _sport: ISport;
    isSaving: boolean;

    profiles: IProfile[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private sportService: SportService,
        private profileService: ProfileService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sport }) => {
            this.sport = sport;
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
        if (this.sport.id !== undefined) {
            this.subscribeToSaveResponse(this.sportService.update(this.sport));
        } else {
            this.subscribeToSaveResponse(this.sportService.create(this.sport));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISport>>) {
        result.subscribe((res: HttpResponse<ISport>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get sport() {
        return this._sport;
    }

    set sport(sport: ISport) {
        this._sport = sport;
    }
}
