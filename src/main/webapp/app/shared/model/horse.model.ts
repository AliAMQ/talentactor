import { IProfile } from 'app/shared/model//profile.model';

export interface IHorse {
    id?: number;
    title?: string;
    profiles?: IProfile[];
}

export class Horse implements IHorse {
    constructor(public id?: number, public title?: string, public profiles?: IProfile[]) {}
}
