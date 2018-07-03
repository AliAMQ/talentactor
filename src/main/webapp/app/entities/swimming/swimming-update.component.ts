import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ISwimming } from 'app/shared/model/swimming.model';
import { SwimmingService } from './swimming.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

@Component({
    selector: 'jhi-swimming-update',
    templateUrl: './swimming-update.component.html'
})
export class SwimmingUpdateComponent implements OnInit {
    private _swimming: ISwimming;
    isSaving: boolean;

    profiles: IProfile[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private swimmingService: SwimmingService,
        private profileService: ProfileService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ swimming }) => {
            this.swimming = swimming;
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
        if (this.swimming.id !== undefined) {
            this.subscribeToSaveResponse(this.swimmingService.update(this.swimming));
        } else {
            this.subscribeToSaveResponse(this.swimmingService.create(this.swimming));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISwimming>>) {
        result.subscribe((res: HttpResponse<ISwimming>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get swimming() {
        return this._swimming;
    }

    set swimming(swimming: ISwimming) {
        this._swimming = swimming;
    }
}
