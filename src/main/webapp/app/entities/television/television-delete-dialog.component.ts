import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITelevision } from 'app/shared/model/television.model';
import { TelevisionService } from './television.service';

@Component({
    selector: 'jhi-television-delete-dialog',
    templateUrl: './television-delete-dialog.component.html'
})
export class TelevisionDeleteDialogComponent {
    television: ITelevision;

    constructor(private televisionService: TelevisionService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.televisionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'televisionListModification',
                content: 'Deleted an television'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-television-delete-popup',
    template: ''
})
export class TelevisionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ television }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TelevisionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.television = television;
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
