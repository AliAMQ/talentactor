import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    FilmComponent,
    FilmDetailComponent,
    FilmUpdateComponent,
    FilmDeletePopupComponent,
    FilmDeleteDialogComponent,
    filmRoute,
    filmPopupRoute
} from './';

const ENTITY_STATES = [...filmRoute, ...filmPopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    exports: [FilmComponent],
    declarations: [FilmComponent, FilmDetailComponent, FilmUpdateComponent, FilmDeleteDialogComponent, FilmDeletePopupComponent],
    entryComponents: [FilmComponent, FilmUpdateComponent, FilmDeleteDialogComponent, FilmDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorFilmModule {}
