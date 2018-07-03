import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TalentactorSharedModule } from 'app/shared';
import {
    VoiceComponent,
    VoiceDetailComponent,
    VoiceUpdateComponent,
    VoiceDeletePopupComponent,
    VoiceDeleteDialogComponent,
    voiceRoute,
    voicePopupRoute
} from './';

const ENTITY_STATES = [...voiceRoute, ...voicePopupRoute];

@NgModule({
    imports: [TalentactorSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [VoiceComponent, VoiceDetailComponent, VoiceUpdateComponent, VoiceDeleteDialogComponent, VoiceDeletePopupComponent],
    entryComponents: [VoiceComponent, VoiceUpdateComponent, VoiceDeleteDialogComponent, VoiceDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorVoiceModule {}
