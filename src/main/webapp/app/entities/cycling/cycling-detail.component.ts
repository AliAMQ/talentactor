import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICycling } from 'app/shared/model/cycling.model';

@Component({
    selector: 'jhi-cycling-detail',
    templateUrl: './cycling-detail.component.html'
})
export class CyclingDetailComponent implements OnInit {
    cycling: ICycling;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cycling }) => {
            this.cycling = cycling;
        });
    }

    previousState() {
        window.history.back();
    }
}
