import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICombat } from 'app/shared/model/combat.model';

@Component({
    selector: 'jhi-combat-detail',
    templateUrl: './combat-detail.component.html'
})
export class CombatDetailComponent implements OnInit {
    combat: ICombat;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ combat }) => {
            this.combat = combat;
        });
    }

    previousState() {
        window.history.back();
    }
}
