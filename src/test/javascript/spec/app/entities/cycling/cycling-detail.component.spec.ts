/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { CyclingDetailComponent } from 'app/entities/cycling/cycling-detail.component';
import { Cycling } from 'app/shared/model/cycling.model';

describe('Component Tests', () => {
    describe('Cycling Management Detail Component', () => {
        let comp: CyclingDetailComponent;
        let fixture: ComponentFixture<CyclingDetailComponent>;
        const route = ({ data: of({ cycling: new Cycling(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [CyclingDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CyclingDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CyclingDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.cycling).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
