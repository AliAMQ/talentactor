import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    InstrumentComponent,
    InstrumentDetailComponent,
    InstrumentUpdateComponent,
    InstrumentDeletePopupComponent,
    InstrumentDeleteDialogComponent,
    instrumentRoute,
    instrumentPopupRoute
} from './';

const ENTITY_STATES = [...instrumentRoute, ...instrumentPopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        InstrumentComponent,
        InstrumentDetailComponent,
        InstrumentUpdateComponent,
        InstrumentDeleteDialogComponent,
        InstrumentDeletePopupComponent
    ],
    entryComponents: [InstrumentComponent, InstrumentUpdateComponent, InstrumentDeleteDialogComponent, InstrumentDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorInstrumentModule {}
