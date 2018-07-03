import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    CyclingComponent,
    CyclingDetailComponent,
    CyclingUpdateComponent,
    CyclingDeletePopupComponent,
    CyclingDeleteDialogComponent,
    cyclingRoute,
    cyclingPopupRoute
} from './';

const ENTITY_STATES = [...cyclingRoute, ...cyclingPopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CyclingComponent,
        CyclingDetailComponent,
        CyclingUpdateComponent,
        CyclingDeleteDialogComponent,
        CyclingDeletePopupComponent
    ],
    entryComponents: [CyclingComponent, CyclingUpdateComponent, CyclingDeleteDialogComponent, CyclingDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorCyclingModule {}
