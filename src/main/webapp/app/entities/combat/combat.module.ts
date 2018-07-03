import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    CombatComponent,
    CombatDetailComponent,
    CombatUpdateComponent,
    CombatDeletePopupComponent,
    CombatDeleteDialogComponent,
    combatRoute,
    combatPopupRoute
} from './';

const ENTITY_STATES = [...combatRoute, ...combatPopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [CombatComponent, CombatDetailComponent, CombatUpdateComponent, CombatDeleteDialogComponent, CombatDeletePopupComponent],
    entryComponents: [CombatComponent, CombatUpdateComponent, CombatDeleteDialogComponent, CombatDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorCombatModule {}
