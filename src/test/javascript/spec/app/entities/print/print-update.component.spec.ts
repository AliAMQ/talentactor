/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { PrintUpdateComponent } from 'app/entities/print/print-update.component';
import { PrintService } from 'app/entities/print/print.service';
import { Print } from 'app/shared/model/print.model';

describe('Component Tests', () => {
    describe('Print Management Update Component', () => {
        let comp: PrintUpdateComponent;
        let fixture: ComponentFixture<PrintUpdateComponent>;
        let service: PrintService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [PrintUpdateComponent]
            })
                .overrideTemplate(PrintUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PrintUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PrintService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Print(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.print = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Print();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.print = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
