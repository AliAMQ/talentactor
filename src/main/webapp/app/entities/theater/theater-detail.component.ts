import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ITheater } from 'app/shared/model/theater.model';
import { FileManagementService } from 'app/shared/file/file-management.service';

@Component({
    selector: 'jhi-theater-detail',
    templateUrl: './theater-detail.component.html'
})
export class TheaterDetailComponent implements OnInit {
    theater: ITheater;

    constructor(
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        public fileManagementService: FileManagementService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ theater }) => {
            this.theater = theater;
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
