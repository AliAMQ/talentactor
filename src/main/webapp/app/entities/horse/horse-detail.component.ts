import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHorse } from 'app/shared/model/horse.model';

@Component({
    selector: 'jhi-horse-detail',
    templateUrl: './horse-detail.component.html'
})
export class HorseDetailComponent implements OnInit {
    horse: IHorse;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ horse }) => {
            this.horse = horse;
        });
    }

    previousState() {
        window.history.back();
    }
}
