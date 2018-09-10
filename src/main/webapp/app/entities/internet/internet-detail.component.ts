import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IInternet } from 'app/shared/model/internet.model';
import { FileManagementService } from 'app/shared/file/file-management.service';

@Component({
    selector: 'jhi-internet-detail',
    templateUrl: './internet-detail.component.html'
})
export class InternetDetailComponent implements OnInit {
    internet: IInternet;

    constructor(
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        public fileManagementService: FileManagementService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ internet }) => {
            this.internet = internet;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
