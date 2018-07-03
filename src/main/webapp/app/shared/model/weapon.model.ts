import { IProfile } from 'app/shared/model//profile.model';

export interface IWeapon {
    id?: number;
    title?: string;
    profiles?: IProfile[];
}

export class Weapon implements IWeapon {
    constructor(public id?: number, public title?: string, public profiles?: IProfile[]) {}
}
