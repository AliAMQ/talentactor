import { Moment } from 'moment';
import { IRole } from 'app/shared/model//role.model';
import { IFilm } from 'app/shared/model//film.model';
import { ITelevision } from 'app/shared/model//television.model';
import { IInternet } from 'app/shared/model//internet.model';
import { ICommercial } from 'app/shared/model//commercial.model';
import { IPrint } from 'app/shared/model//print.model';
import { ITheater } from 'app/shared/model//theater.model';
import { IVoice } from 'app/shared/model//voice.model';
import { ISkill } from 'app/shared/model//skill.model';
import { ISport } from 'app/shared/model//sport.model';
import { ISwimming } from 'app/shared/model//swimming.model';
import { ICombat } from 'app/shared/model//combat.model';
import { ILanguage } from 'app/shared/model//language.model';
import { IInstrument } from 'app/shared/model//instrument.model';
import { IWeapon } from 'app/shared/model//weapon.model';
import { ICycling } from 'app/shared/model//cycling.model';
import { ICircus } from 'app/shared/model//circus.model';
import { IHorse } from 'app/shared/model//horse.model';

export const enum State {
    AK = 'AK',
    AL = 'AL',
    AZ = 'AZ',
    AR = 'AR',
    CA = 'CA',
    CO = 'CO',
    CT = 'CT',
    DE = 'DE',
    FL = 'FL',
    GA = 'GA',
    HI = 'HI',
    ID = 'ID',
    IL = 'IL',
    IN = 'IN',
    IA = 'IA',
    KS = 'KS',
    KY = 'KY',
    LA = 'LA',
    ME = 'ME',
    MD = 'MD',
    MA = 'MA',
    MI = 'MI',
    MN = 'MN',
    MS = 'MS',
    MO = 'MO',
    MT = 'MT',
    NE = 'NE',
    NV = 'NV',
    NH = 'NH',
    NJ = 'NJ',
    NM = 'NM',
    NY = 'NY',
    NC = 'NC',
    ND = 'ND',
    OH = 'OH',
    OK = 'OK',
    OR = 'OR',
    PA = 'PA',
    RI = 'RI',
    SC = 'SC',
    SD = 'SD',
    TN = 'TN',
    TX = 'TX',
    UT = 'UT',
    VT = 'VT',
    VA = 'VA',
    WA = 'WA',
    WV = 'WV',
    WI = 'WI',
    WY = 'WY'
}

export interface IProfile {
    id?: number;
    state?: State;
    city?: string;
    address?: string;
    phone?: string;
    since?: Moment;
    imageContentType?: string;
    image?: any;
    videoContentType?: string;
    video?: any;
    audioContentType?: string;
    audio?: any;
    imagepath?: string;
    videopath?: string;
    audiopath?: string;
    firstname?: string;
    lastname?: string;
    username?: string;
    userId?: number;
    roles?: IRole[];
    films?: IFilm[];
    televisions?: ITelevision[];
    internets?: IInternet[];
    commercials?: ICommercial[];
    prints?: IPrint[];
    theaters?: ITheater[];
    voices?: IVoice[];
    skills?: ISkill[];
    sports?: ISport[];
    swimmings?: ISwimming[];
    combats?: ICombat[];
    languages?: ILanguage[];
    instruments?: IInstrument[];
    weapons?: IWeapon[];
    cyclings?: ICycling[];
    circuses?: ICircus[];
    horses?: IHorse[];
}

export class Profile implements IProfile {
    constructor(
        public id?: number,
        public state?: State,
        public city?: string,
        public address?: string,
        public phone?: string,
        public since?: Moment,
        public imageContentType?: string,
        public image?: any,
        public videoContentType?: string,
        public video?: any,
        public audioContentType?: string,
        public audio?: any,
        public imagepath?: string,
        public videopath?: string,
        public audiopath?: string,
        public firstname?: string,
        public lastname?: string,
        public username?: string,
        public userId?: number,
        public roles?: IRole[],
        public films?: IFilm[],
        public televisions?: ITelevision[],
        public internets?: IInternet[],
        public commercials?: ICommercial[],
        public prints?: IPrint[],
        public theaters?: ITheater[],
        public voices?: IVoice[],
        public skills?: ISkill[],
        public sports?: ISport[],
        public swimmings?: ISwimming[],
        public combats?: ICombat[],
        public languages?: ILanguage[],
        public instruments?: IInstrument[],
        public weapons?: IWeapon[],
        public cyclings?: ICycling[],
        public circuses?: ICircus[],
        public horses?: IHorse[]
    ) {}
}
