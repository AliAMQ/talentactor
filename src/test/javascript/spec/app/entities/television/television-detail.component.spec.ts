/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { TelevisionDetailComponent } from 'app/entities/television/television-detail.component';
import { Television } from 'app/shared/model/television.model';

describe('Component Tests', () => {
    describe('Television Management Detail Component', () => {
        let comp: TelevisionDetailComponent;
        let fixture: ComponentFixture<TelevisionDetailComponent>;
        const route = ({ data: of({ television: new Television(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [TelevisionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TelevisionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TelevisionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.television).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
