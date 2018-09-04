import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    PrintComponent,
    PrintDetailComponent,
    PrintUpdateComponent,
    PrintDeletePopupComponent,
    PrintDeleteDialogComponent,
    printRoute,
    printPopupRoute
} from './';

const ENTITY_STATES = [...printRoute, ...printPopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    exports: [PrintComponent],
    declarations: [PrintComponent, PrintDetailComponent, PrintUpdateComponent, PrintDeleteDialogComponent, PrintDeletePopupComponent],
    entryComponents: [PrintComponent, PrintUpdateComponent, PrintDeleteDialogComponent, PrintDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorPrintModule {}
