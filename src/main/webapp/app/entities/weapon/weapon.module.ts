import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    WeaponComponent,
    WeaponDetailComponent,
    WeaponUpdateComponent,
    WeaponDeletePopupComponent,
    WeaponDeleteDialogComponent,
    weaponRoute,
    weaponPopupRoute
} from './';

const ENTITY_STATES = [...weaponRoute, ...weaponPopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [WeaponComponent, WeaponDetailComponent, WeaponUpdateComponent, WeaponDeleteDialogComponent, WeaponDeletePopupComponent],
    entryComponents: [WeaponComponent, WeaponUpdateComponent, WeaponDeleteDialogComponent, WeaponDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorWeaponModule {}
