import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IHorse } from 'app/shared/model/horse.model';
import { HorseService } from './horse.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

@Component({
    selector: 'jhi-horse-update',
    templateUrl: './horse-update.component.html'
})
export class HorseUpdateComponent implements OnInit {
    private _horse: IHorse;
    isSaving: boolean;

    profiles: IProfile[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private horseService: HorseService,
        private profileService: ProfileService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ horse }) => {
            this.horse = horse;
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
        if (this.horse.id !== undefined) {
            this.subscribeToSaveResponse(this.horseService.update(this.horse));
        } else {
            this.subscribeToSaveResponse(this.horseService.create(this.horse));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IHorse>>) {
        result.subscribe((res: HttpResponse<IHorse>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get horse() {
        return this._horse;
    }

    set horse(horse: IHorse) {
        this._horse = horse;
    }
}
