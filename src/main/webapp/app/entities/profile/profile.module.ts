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
import { TalentactorTelevisionModule } from 'app/entities/television/television.module';
import { TalentactorInternetModule } from 'app/entities/internet/internet.module';
import { TalentactorCommercialModule } from 'app/entities/commercial/commercial.module';
import { TalentactorPrintModule } from 'app/entities/print/print.module';
import { TalentactorTheaterModule } from 'app/entities/theater/theater.module';
import { TalentactorVoiceModule } from 'app/entities/voice/voice.module';

const ENTITY_STATES = [...profileRoute, ...profilePopupRoute];

@NgModule({
    imports: [
        TalentactorSharedModule,
        TalentactorAdminModule,
        RouterModule.forChild(ENTITY_STATES),
        TalentactorFilmModule,
        TalentactorTelevisionModule,
        TalentactorInternetModule,
        TalentactorCommercialModule,
        TalentactorPrintModule,
        TalentactorTheaterModule,
        TalentactorVoiceModule
    ],
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
