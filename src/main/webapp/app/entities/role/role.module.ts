import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    RoleComponent,
    RoleDetailComponent,
    RoleUpdateComponent,
    RoleDeletePopupComponent,
    RoleDeleteDialogComponent,
    roleRoute,
    rolePopupRoute
} from './';
import { RoleMyComponent } from './role-my/role-my.component';

const ENTITY_STATES = [...roleRoute, ...rolePopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    exports: [RoleMyComponent],
    declarations: [
        RoleComponent,
        RoleDetailComponent,
        RoleUpdateComponent,
        RoleDeleteDialogComponent,
        RoleDeletePopupComponent,
        RoleMyComponent
    ],
    entryComponents: [RoleComponent, RoleUpdateComponent, RoleDeleteDialogComponent, RoleDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorRoleModule {}
