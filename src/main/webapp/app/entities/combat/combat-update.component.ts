import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICombat } from 'app/shared/model/combat.model';
import { CombatService } from './combat.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';

@Component({
    selector: 'jhi-combat-update',
    templateUrl: './combat-update.component.html'
})
export class CombatUpdateComponent implements OnInit {
    private _combat: ICombat;
    isSaving: boolean;

    profiles: IProfile[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private combatService: CombatService,
        private profileService: ProfileService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ combat }) => {
            this.combat = combat;
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
        if (this.combat.id !== undefined) {
            this.subscribeToSaveResponse(this.combatService.update(this.combat));
        } else {
            this.subscribeToSaveResponse(this.combatService.create(this.combat));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICombat>>) {
        result.subscribe((res: HttpResponse<ICombat>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get combat() {
        return this._combat;
    }

    set combat(combat: ICombat) {
        this._combat = combat;
    }
}
