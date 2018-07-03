/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { InternetDetailComponent } from 'app/entities/internet/internet-detail.component';
import { Internet } from 'app/shared/model/internet.model';

describe('Component Tests', () => {
    describe('Internet Management Detail Component', () => {
        let comp: InternetDetailComponent;
        let fixture: ComponentFixture<InternetDetailComponent>;
        const route = ({ data: of({ internet: new Internet(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [InternetDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(InternetDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(InternetDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.internet).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
