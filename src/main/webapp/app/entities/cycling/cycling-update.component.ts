import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICycling } from 'app/shared/model/cycling.model';
import { CyclingService } from './cycling.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

@Component({
    selector: 'jhi-cycling-update',
    templateUrl: './cycling-update.component.html'
})
export class CyclingUpdateComponent implements OnInit {
    private _cycling: ICycling;
    isSaving: boolean;

    profiles: IProfile[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private cyclingService: CyclingService,
        private profileService: ProfileService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cycling }) => {
            this.cycling = cycling;
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
        if (this.cycling.id !== undefined) {
            this.subscribeToSaveResponse(this.cyclingService.update(this.cycling));
        } else {
            this.subscribeToSaveResponse(this.cyclingService.create(this.cycling));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICycling>>) {
        result.subscribe((res: HttpResponse<ICycling>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get cycling() {
        return this._cycling;
    }

    set cycling(cycling: ICycling) {
        this._cycling = cycling;
    }
}
