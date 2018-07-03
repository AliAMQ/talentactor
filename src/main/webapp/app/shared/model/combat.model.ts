import { IProfile } from 'app/shared/model//profile.model';

export interface ICombat {
    id?: number;
    title?: string;
    profiles?: IProfile[];
}

export class Combat implements ICombat {
    constructor(public id?: number, public title?: string, public profiles?: IProfile[]) {}
}
