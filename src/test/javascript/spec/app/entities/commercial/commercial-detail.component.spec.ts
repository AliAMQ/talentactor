/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { CommercialDetailComponent } from 'app/entities/commercial/commercial-detail.component';
import { Commercial } from 'app/shared/model/commercial.model';

describe('Component Tests', () => {
    describe('Commercial Management Detail Component', () => {
        let comp: CommercialDetailComponent;
        let fixture: ComponentFixture<CommercialDetailComponent>;
        const route = ({ data: of({ commercial: new Commercial(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [CommercialDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CommercialDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CommercialDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.commercial).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
