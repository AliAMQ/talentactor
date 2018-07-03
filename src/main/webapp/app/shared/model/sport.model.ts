import { IProfile } from 'app/shared/model//profile.model';

export interface ISport {
    id?: number;
    title?: string;
    profiles?: IProfile[];
}

export class Sport implements ISport {
    constructor(public id?: number, public title?: string, public profiles?: IProfile[]) {}
}
