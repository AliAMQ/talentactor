import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ITelevision } from 'app/shared/model/television.model';

@Component({
    selector: 'jhi-television-detail',
    templateUrl: './television-detail.component.html'
})
export class TelevisionDetailComponent implements OnInit {
    television: ITelevision;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ television }) => {
            this.television = television;
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
