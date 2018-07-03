/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { SwimmingDetailComponent } from 'app/entities/swimming/swimming-detail.component';
import { Swimming } from 'app/shared/model/swimming.model';

describe('Component Tests', () => {
    describe('Swimming Management Detail Component', () => {
        let comp: SwimmingDetailComponent;
        let fixture: ComponentFixture<SwimmingDetailComponent>;
        const route = ({ data: of({ swimming: new Swimming(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [SwimmingDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SwimmingDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SwimmingDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.swimming).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
