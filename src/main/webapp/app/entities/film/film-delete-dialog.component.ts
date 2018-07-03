import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFilm } from 'app/shared/model/film.model';
import { FilmService } from './film.service';

@Component({
    selector: 'jhi-film-delete-dialog',
    templateUrl: './film-delete-dialog.component.html'
})
export class FilmDeleteDialogComponent {
    film: IFilm;

    constructor(private filmService: FilmService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.filmService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'filmListModification',
                content: 'Deleted an film'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-film-delete-popup',
    template: ''
})
export class FilmDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ film }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FilmDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.film = film;
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
