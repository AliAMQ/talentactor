import { IProfile } from 'app/shared/model//profile.model';

export interface ICircus {
    id?: number;
    title?: string;
    profiles?: IProfile[];
}

export class Circus implements ICircus {
    constructor(public id?: number, public title?: string, public profiles?: IProfile[]) {}
}
