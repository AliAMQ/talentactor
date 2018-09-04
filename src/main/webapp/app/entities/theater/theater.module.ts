import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    TheaterComponent,
    TheaterDetailComponent,
    TheaterUpdateComponent,
    TheaterDeletePopupComponent,
    TheaterDeleteDialogComponent,
    theaterRoute,
    theaterPopupRoute
} from './';

const ENTITY_STATES = [...theaterRoute, ...theaterPopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    exports: [TheaterComponent],
    declarations: [
        TheaterComponent,
        TheaterDetailComponent,
        TheaterUpdateComponent,
        TheaterDeleteDialogComponent,
        TheaterDeletePopupComponent
    ],
    entryComponents: [TheaterComponent, TheaterUpdateComponent, TheaterDeleteDialogComponent, TheaterDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorTheaterModule {}
