import { IProfile } from 'app/shared/model//profile.model';

export interface ILanguage {
    id?: number;
    title?: string;
    profiles?: IProfile[];
}

export class Language implements ILanguage {
    constructor(public id?: number, public title?: string, public profiles?: IProfile[]) {}
}
