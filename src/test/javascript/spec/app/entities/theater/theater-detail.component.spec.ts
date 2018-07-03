/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { TheaterDetailComponent } from 'app/entities/theater/theater-detail.component';
import { Theater } from 'app/shared/model/theater.model';

describe('Component Tests', () => {
    describe('Theater Management Detail Component', () => {
        let comp: TheaterDetailComponent;
        let fixture: ComponentFixture<TheaterDetailComponent>;
        const route = ({ data: of({ theater: new Theater(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [TheaterDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TheaterDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TheaterDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.theater).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
