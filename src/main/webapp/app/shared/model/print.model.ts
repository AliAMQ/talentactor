export interface IPrint {
    id?: number;
    title?: string;
    photographer?: string;
    hair?: string;
    link?: string;
    imageContentType?: string;
    image?: any;
    imagepath?: string;
    profileId?: number;
}

export class Print implements IPrint {
    constructor(
        public id?: number,
        public title?: string,
        public photographer?: string,
        public hair?: string,
        public link?: string,
        public imageContentType?: string,
        public image?: any,
        public imagepath?: string,
        public profileId?: number
    ) {}
}
