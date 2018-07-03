import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    TelevisionComponent,
    TelevisionDetailComponent,
    TelevisionUpdateComponent,
    TelevisionDeletePopupComponent,
    TelevisionDeleteDialogComponent,
    televisionRoute,
    televisionPopupRoute
} from './';

const ENTITY_STATES = [...televisionRoute, ...televisionPopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TelevisionComponent,
        TelevisionDetailComponent,
        TelevisionUpdateComponent,
        TelevisionDeleteDialogComponent,
        TelevisionDeletePopupComponent
    ],
    entryComponents: [TelevisionComponent, TelevisionUpdateComponent, TelevisionDeleteDialogComponent, TelevisionDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorTelevisionModule {}
