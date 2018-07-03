import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    HorseComponent,
    HorseDetailComponent,
    HorseUpdateComponent,
    HorseDeletePopupComponent,
    HorseDeleteDialogComponent,
    horseRoute,
    horsePopupRoute
} from './';

const ENTITY_STATES = [...horseRoute, ...horsePopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [HorseComponent, HorseDetailComponent, HorseUpdateComponent, HorseDeleteDialogComponent, HorseDeletePopupComponent],
    entryComponents: [HorseComponent, HorseUpdateComponent, HorseDeleteDialogComponent, HorseDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorHorseModule {}
