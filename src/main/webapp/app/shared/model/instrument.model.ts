import { IProfile } from 'app/shared/model//profile.model';

export interface IInstrument {
    id?: number;
    title?: string;
    profiles?: IProfile[];
}

export class Instrument implements IInstrument {
    constructor(public id?: number, public title?: string, public profiles?: IProfile[]) {}
}
