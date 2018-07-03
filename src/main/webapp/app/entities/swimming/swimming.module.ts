import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    SwimmingComponent,
    SwimmingDetailComponent,
    SwimmingUpdateComponent,
    SwimmingDeletePopupComponent,
    SwimmingDeleteDialogComponent,
    swimmingRoute,
    swimmingPopupRoute
} from './';

const ENTITY_STATES = [...swimmingRoute, ...swimmingPopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SwimmingComponent,
        SwimmingDetailComponent,
        SwimmingUpdateComponent,
        SwimmingDeleteDialogComponent,
        SwimmingDeletePopupComponent
    ],
    entryComponents: [SwimmingComponent, SwimmingUpdateComponent, SwimmingDeleteDialogComponent, SwimmingDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorSwimmingModule {}
