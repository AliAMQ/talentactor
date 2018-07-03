/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { CircusDetailComponent } from 'app/entities/circus/circus-detail.component';
import { Circus } from 'app/shared/model/circus.model';

describe('Component Tests', () => {
    describe('Circus Management Detail Component', () => {
        let comp: CircusDetailComponent;
        let fixture: ComponentFixture<CircusDetailComponent>;
        const route = ({ data: of({ circus: new Circus(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [CircusDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CircusDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CircusDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.circus).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
