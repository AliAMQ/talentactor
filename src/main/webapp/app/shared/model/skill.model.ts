import { IProfile } from 'app/shared/model//profile.model';

export interface ISkill {
    id?: number;
    title?: string;
    profiles?: IProfile[];
}

export class Skill implements ISkill {
    constructor(public id?: number, public title?: string, public profiles?: IProfile[]) {}
}
