import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TalentactorProfileModule } from './profile/profile.module';
import { TalentactorSkillModule } from './skill/skill.module';
import { TalentactorFilmModule } from './film/film.module';
import { TalentactorTelevisionModule } from './television/television.module';
import { TalentactorInternetModule } from './internet/internet.module';
import { TalentactorCommercialModule } from './commercial/commercial.module';
import { TalentactorPrintModule } from './print/print.module';
import { TalentactorTheaterModule } from './theater/theater.module';
import { TalentactorVoiceModule } from './voice/voice.module';
import { TalentactorProjectModule } from './project/project.module';
import { TalentactorRoleModule } from './role/role.module';
import { TalentactorSportModule } from './sport/sport.module';
import { TalentactorSwimmingModule } from './swimming/swimming.module';
import { TalentactorCombatModule } from './combat/combat.module';
import { TalentactorLanguageModule } from './language/language.module';
import { TalentactorInstrumentModule } from './instrument/instrument.module';
import { TalentactorWeaponModule } from './weapon/weapon.module';
import { TalentactorCyclingModule } from './cycling/cycling.module';
import { TalentactorCircusModule } from './circus/circus.module';
import { TalentactorHorseModule } from './horse/horse.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        TalentactorProfileModule,
        TalentactorSkillModule,
        TalentactorFilmModule,
        TalentactorTelevisionModule,
        TalentactorInternetModule,
        TalentactorCommercialModule,
        TalentactorPrintModule,
        TalentactorTheaterModule,
        TalentactorVoiceModule,
        TalentactorProjectModule,
        TalentactorRoleModule,
        TalentactorSportModule,
        TalentactorSwimmingModule,
        TalentactorCombatModule,
        TalentactorLanguageModule,
        TalentactorInstrumentModule,
        TalentactorWeaponModule,
        TalentactorCyclingModule,
        TalentactorCircusModule,
        TalentactorHorseModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TalentactorEntityModule {}
