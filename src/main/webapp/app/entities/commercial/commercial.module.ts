import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    CommercialComponent,
    CommercialDetailComponent,
    CommercialUpdateComponent,
    CommercialDeletePopupComponent,
    CommercialDeleteDialogComponent,
    commercialRoute,
    commercialPopupRoute
} from './';

const ENTITY_STATES = [...commercialRoute, ...commercialPopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CommercialComponent,
        CommercialDetailComponent,
        CommercialUpdateComponent,
        CommercialDeleteDialogComponent,
        CommercialDeletePopupComponent
    ],
    entryComponents: [CommercialComponent, CommercialUpdateComponent, CommercialDeleteDialogComponent, CommercialDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorCommercialModule {}
