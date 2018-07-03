/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { PrintDetailComponent } from 'app/entities/print/print-detail.component';
import { Print } from 'app/shared/model/print.model';

describe('Component Tests', () => {
    describe('Print Management Detail Component', () => {
        let comp: PrintDetailComponent;
        let fixture: ComponentFixture<PrintDetailComponent>;
        const route = ({ data: of({ print: new Print(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [PrintDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PrintDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PrintDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.print).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
