import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    InternetComponent,
    InternetDetailComponent,
    InternetUpdateComponent,
    InternetDeletePopupComponent,
    InternetDeleteDialogComponent,
    internetRoute,
    internetPopupRoute
} from './';

const ENTITY_STATES = [...internetRoute, ...internetPopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    exports: [InternetComponent],
    declarations: [
        InternetComponent,
        InternetDetailComponent,
        InternetUpdateComponent,
        InternetDeleteDialogComponent,
        InternetDeletePopupComponent
    ],
    entryComponents: [InternetComponent, InternetUpdateComponent, InternetDeleteDialogComponent, InternetDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorInternetModule {}
