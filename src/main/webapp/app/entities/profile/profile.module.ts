import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import { TalentactorAdminModule } from 'app/admin/admin.module';
import {
    ProfileComponent,
    ProfileDetailComponent,
    ProfileUpdateComponent,
    ProfileDeletePopupComponent,
    ProfileDeleteDialogComponent,
    profileRoute,
    profilePopupRoute
} from './';
import { TalentactorFilmModule } from 'app/entities/film/film.module';

const ENTITY_STATES = [...profileRoute, ...profilePopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, TalentactorAdminModule, RouterModule.forChild(ENTITY_STATES), TalentactorFilmModule],
    declarations: [
        ProfileComponent,
        ProfileDetailComponent,
        ProfileUpdateComponent,
        ProfileDeleteDialogComponent,
        ProfileDeletePopupComponent
    ],
    entryComponents: [ProfileComponent, ProfileUpdateComponent, ProfileDeleteDialogComponent, ProfileDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorProfileModule {}
