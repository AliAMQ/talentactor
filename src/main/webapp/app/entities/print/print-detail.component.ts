import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IPrint } from 'app/shared/model/print.model';
import { FileManagementService } from 'app/shared/file/file-management.service';

@Component({
    selector: 'jhi-print-detail',
    templateUrl: './print-detail.component.html'
})
export class PrintDetailComponent implements OnInit {
    print: IPrint;

    constructor(
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        public fileManagementService: FileManagementService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ print }) => {
            this.print = print;
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
