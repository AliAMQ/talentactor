export interface IRole {
    id?: number;
    title?: string;
    description?: string;
    profileId?: number;
    projectId?: number;
}

export class Role implements IRole {
    constructor(
        public id?: number,
        public title?: string,
        public description?: string,
        public profileId?: number,
        public projectId?: number
    ) {}
}
