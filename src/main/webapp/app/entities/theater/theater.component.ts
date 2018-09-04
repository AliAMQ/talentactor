import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ITheater } from 'app/shared/model/theater.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { TheaterService } from './theater.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from '../profile/profile.service';
import { IPrint } from 'app/shared/model/print.model';

@Component({
    selector: 'jhi-theater',
    templateUrl: './theater.component.html'
})
export class TheaterComponent implements OnInit, OnDestroy {
    currentAccount: any;
    theaters: ITheater[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    profileid: number;

    constructor(
        private theaterService: TheaterService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager,
        private profileService: ProfileService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        if (typeof this.predicate === 'undefined') {
            this.predicate = 'id';
        }
        if (typeof this.page === 'undefined') {
            this.page = 1;
        }
        this.profileService.findByUserId(this.principal.getId()).subscribe((res: HttpResponse<IProfile>) => {
            this.profileid = res.body.id;
            this.theaterService
                .query({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    'profileId.equals': this.profileid
                })
                .subscribe(
                    (res1: HttpResponse<ITheater[]>) => this.paginateTheaters(res1.body, res1.headers),
                    (res1: HttpErrorResponse) => this.onError(res1.message)
                );
        });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        /*this.router.navigate(['/theater'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });*/
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/theater',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.findProfileByUserId();
        this.registerChangeInTheaters();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ITheater) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInTheaters() {
        this.eventSubscriber = this.eventManager.subscribe('theaterListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateTheaters(data: ITheater[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.theaters = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    findProfileByUserId() {
        this.profileService.getProfileId.subscribe(value => (this.profileid = value));
        if (this.profileid === 0) {
            this.principal.identity().then(account => {
                if (account !== null) {
                    this.profileService
                        .query({
                            'username.equals': account.login
                        })
                        .subscribe((res: HttpResponse<IProfile[]>) => {
                            this.profileid = res.body[0].id;
                            this.loadAll();
                        });
                }
            });
        } else {
            this.loadAll();
        }
    }
}
