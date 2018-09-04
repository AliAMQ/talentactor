import { Component, OnInit } from '@angular/core';
import { JhiProfileService } from './profile.service';
import { ProfileInfo } from './profile-info.model';

@Component({
    selector: 'jhi-page-ribbon',
    template: `<div class="ribbon" *ngIf="ribbonEnv"><a href="" jhiTranslate="global.ribbon.{{ribbonEnv}}">{{ribbonEnv}}</a></div>`,
    styleUrls: ['page-ribbon.scss']
})
export class PageRibbonComponent implements OnInit {
    profileInfo: ProfileInfo;
    ribbonEnv: string;

    constructor(private profileService: JhiProfileService) {}

    ngOnInit() {
        this.profileService.getProfileInfo().then(profileInfo => {
            this.profileInfo = profileInfo;
            this.ribbonEnv = profileInfo.ribbonEnv;
        });
    }
}
