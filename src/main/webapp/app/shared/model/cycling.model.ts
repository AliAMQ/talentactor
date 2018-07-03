import { IProfile } from 'app/shared/model//profile.model';

export interface ICycling {
    id?: number;
    title?: string;
    profiles?: IProfile[];
}

export class Cycling implements ICycling {
    constructor(public id?: number, public title?: string, public profiles?: IProfile[]) {}
}
