import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ICommercial } from 'app/shared/model/commercial.model';

@Component({
    selector: 'jhi-commercial-detail',
    templateUrl: './commercial-detail.component.html'
})
export class CommercialDetailComponent implements OnInit {
    commercial: ICommercial;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ commercial }) => {
            this.commercial = commercial;
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
