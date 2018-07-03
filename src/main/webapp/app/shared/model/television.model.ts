export interface ITelevision {
    id?: number;
    title?: string;
    director?: string;
    cameraman?: string;
    link?: string;
    imageContentType?: string;
    image?: any;
    videoContentType?: string;
    video?: any;
    imagepath?: string;
    videopath?: string;
    profileId?: number;
}

export class Television implements ITelevision {
    constructor(
        public id?: number,
        public title?: string,
        public director?: string,
        public cameraman?: string,
        public link?: string,
        public imageContentType?: string,
        public image?: any,
        public videoContentType?: string,
        public video?: any,
        public imagepath?: string,
        public videopath?: string,
        public profileId?: number
    ) {}
}
