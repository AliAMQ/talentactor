import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInstrument } from 'app/shared/model/instrument.model';
import { InstrumentService } from './instrument.service';

@Component({
    selector: 'jhi-instrument-delete-dialog',
    templateUrl: './instrument-delete-dialog.component.html'
})
export class InstrumentDeleteDialogComponent {
    instrument: IInstrument;

    constructor(private instrumentService: InstrumentService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.instrumentService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'instrumentListModification',
                content: 'Deleted an instrument'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-instrument-delete-popup',
    template: ''
})
export class InstrumentDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ instrument }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(InstrumentDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.instrument = instrument;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
