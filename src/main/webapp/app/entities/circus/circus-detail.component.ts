import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICircus } from 'app/shared/model/circus.model';

@Component({
    selector: 'jhi-circus-detail',
    templateUrl: './circus-detail.component.html'
})
export class CircusDetailComponent implements OnInit {
    circus: ICircus;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ circus }) => {
            this.circus = circus;
        });
    }

    previousState() {
        window.history.back();
    }
}
