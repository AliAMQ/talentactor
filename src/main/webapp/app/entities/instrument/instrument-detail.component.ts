import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInstrument } from 'app/shared/model/instrument.model';

@Component({
    selector: 'jhi-instrument-detail',
    templateUrl: './instrument-detail.component.html'
})
export class InstrumentDetailComponent implements OnInit {
    instrument: IInstrument;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ instrument }) => {
            this.instrument = instrument;
        });
    }

    previousState() {
        window.history.back();
    }
}
