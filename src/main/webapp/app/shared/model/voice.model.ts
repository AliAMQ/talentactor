export interface IVoice {
    id?: number;
    title?: string;
    director?: string;
    link?: string;
    videoContentType?: string;
    video?: any;
    audioContentType?: string;
    audio?: any;
    videopath?: string;
    audiopath?: string;
    profileId?: number;
}

export class Voice implements IVoice {
    constructor(
        public id?: number,
        public title?: string,
        public director?: string,
        public link?: string,
        public videoContentType?: string,
        public video?: any,
        public audioContentType?: string,
        public audio?: any,
        public videopath?: string,
        public audiopath?: string,
        public profileId?: number
    ) {}
}
