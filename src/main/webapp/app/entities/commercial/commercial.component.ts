import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ICommercial } from 'app/shared/model/commercial.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { CommercialService } from './commercial.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from '../profile/profile.service';
import { IInternet } from 'app/shared/model/internet.model';
import { FileManagementService } from 'app/shared/file/file-management.service';

@Component({
    selector: 'jhi-commercial',
    templateUrl: './commercial.component.html'
})
export class CommercialComponent implements OnInit, OnDestroy {
    currentAccount: any;
    commercials: ICommercial[];
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
        private commercialService: CommercialService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager,
        private profileService: ProfileService,
        public fileManagementService: FileManagementService
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
        /*this.profileService.findByUserId(this.principal.getId()).subscribe((res: HttpResponse<IProfile>) => {
            this.profileid = res.body.id;*/
        this.commercialService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                'profileId.equals': this.profileid
            })
            .subscribe(
                (res1: HttpResponse<ICommercial[]>) => this.paginateCommercials(res1.body, res1.headers),
                (res1: HttpErrorResponse) => this.onError(res1.message)
            );
        // });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        /*this.router.navigate(['/commercial'], {
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
            '/commercial',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        /*this.loadAll();*/
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.findProfileByUserId();
        this.registerChangeInCommercials();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICommercial) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInCommercials() {
        this.eventSubscriber = this.eventManager.subscribe('commercialListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateCommercials(data: ICommercial[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.commercials = data;
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
