import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPrint } from 'app/shared/model/print.model';
import { PrintService } from './print.service';

@Component({
    selector: 'jhi-print-delete-dialog',
    templateUrl: './print-delete-dialog.component.html'
})
export class PrintDeleteDialogComponent {
    print: IPrint;

    constructor(private printService: PrintService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.printService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'printListModification',
                content: 'Deleted an print'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-print-delete-popup',
    template: ''
})
export class PrintDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ print }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PrintDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.print = print;
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
