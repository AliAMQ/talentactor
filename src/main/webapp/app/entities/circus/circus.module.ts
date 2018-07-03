import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    CircusComponent,
    CircusDetailComponent,
    CircusUpdateComponent,
    CircusDeletePopupComponent,
    CircusDeleteDialogComponent,
    circusRoute,
    circusPopupRoute
} from './';

const ENTITY_STATES = [...circusRoute, ...circusPopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [CircusComponent, CircusDetailComponent, CircusUpdateComponent, CircusDeleteDialogComponent, CircusDeletePopupComponent],
    entryComponents: [CircusComponent, CircusUpdateComponent, CircusDeleteDialogComponent, CircusDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorCircusModule {}
