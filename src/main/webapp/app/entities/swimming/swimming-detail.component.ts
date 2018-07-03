import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISwimming } from 'app/shared/model/swimming.model';

@Component({
    selector: 'jhi-swimming-detail',
    templateUrl: './swimming-detail.component.html'
})
export class SwimmingDetailComponent implements OnInit {
    swimming: ISwimming;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ swimming }) => {
            this.swimming = swimming;
        });
    }

    previousState() {
        window.history.back();
    }
}
