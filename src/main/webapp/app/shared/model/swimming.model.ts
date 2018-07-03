import { IProfile } from 'app/shared/model//profile.model';

export interface ISwimming {
    id?: number;
    title?: string;
    profiles?: IProfile[];
}

export class Swimming implements ISwimming {
    constructor(public id?: number, public title?: string, public profiles?: IProfile[]) {}
}
