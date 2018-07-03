import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISwimming } from 'app/shared/model/swimming.model';
import { SwimmingService } from './swimming.service';

@Component({
    selector: 'jhi-swimming-delete-dialog',
    templateUrl: './swimming-delete-dialog.component.html'
})
export class SwimmingDeleteDialogComponent {
    swimming: ISwimming;

    constructor(private swimmingService: SwimmingService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.swimmingService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'swimmingListModification',
                content: 'Deleted an swimming'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-swimming-delete-popup',
    template: ''
})
export class SwimmingDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ swimming }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SwimmingDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.swimming = swimming;
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
