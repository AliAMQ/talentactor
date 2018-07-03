import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IWeapon } from 'app/shared/model/weapon.model';
import { WeaponService } from './weapon.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

@Component({
    selector: 'jhi-weapon-update',
    templateUrl: './weapon-update.component.html'
})
export class WeaponUpdateComponent implements OnInit {
    private _weapon: IWeapon;
    isSaving: boolean;

    profiles: IProfile[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private weaponService: WeaponService,
        private profileService: ProfileService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ weapon }) => {
            this.weapon = weapon;
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
        if (this.weapon.id !== undefined) {
            this.subscribeToSaveResponse(this.weaponService.update(this.weapon));
        } else {
            this.subscribeToSaveResponse(this.weaponService.create(this.weapon));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWeapon>>) {
        result.subscribe((res: HttpResponse<IWeapon>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get weapon() {
        return this._weapon;
    }

    set weapon(weapon: IWeapon) {
        this._weapon = weapon;
    }
}
