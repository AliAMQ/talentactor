import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IFilm } from 'app/shared/model/film.model';
import { FileManagementService } from '../../shared/file/file-management.service';

@Component({
    selector: 'jhi-film-detail',
    templateUrl: './film-detail.component.html'
})
export class FilmDetailComponent implements OnInit {
    film: IFilm;

    constructor(
        private dataUtils: JhiDataUtils,
        private activatedRoute: ActivatedRoute,
        public fileManagementService: FileManagementService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ film }) => {
            this.film = film;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
