import { Moment } from 'moment';
import { IRole } from 'app/shared/model//role.model';

export interface IProject {
    id?: number;
    title?: string;
    code?: string;
    date?: Moment;
    imageContentType?: string;
    image?: any;
    videoContentType?: string;
    video?: any;
    imagepath?: string;
    videopath?: string;
    roles?: IRole[];
}

export class Project implements IProject {
    constructor(
        public id?: number,
        public title?: string,
        public code?: string,
        public date?: Moment,
        public imageContentType?: string,
        public image?: any,
        public videoContentType?: string,
        public video?: any,
        public imagepath?: string,
        public videopath?: string,
        public roles?: IRole[]
    ) {}
}
