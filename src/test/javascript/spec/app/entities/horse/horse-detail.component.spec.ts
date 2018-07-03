/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { HorseDetailComponent } from 'app/entities/horse/horse-detail.component';
import { Horse } from 'app/shared/model/horse.model';

describe('Component Tests', () => {
    describe('Horse Management Detail Component', () => {
        let comp: HorseDetailComponent;
        let fixture: ComponentFixture<HorseDetailComponent>;
        const route = ({ data: of({ horse: new Horse(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [HorseDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(HorseDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HorseDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.horse).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
