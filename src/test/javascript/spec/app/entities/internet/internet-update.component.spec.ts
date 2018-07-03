/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { InternetUpdateComponent } from 'app/entities/internet/internet-update.component';
import { InternetService } from 'app/entities/internet/internet.service';
import { Internet } from 'app/shared/model/internet.model';

describe('Component Tests', () => {
    describe('Internet Management Update Component', () => {
        let comp: InternetUpdateComponent;
        let fixture: ComponentFixture<InternetUpdateComponent>;
        let service: InternetService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [InternetUpdateComponent]
            })
                .overrideTemplate(InternetUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InternetUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InternetService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Internet(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.internet = entity;
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
                    const entity = new Internet();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.internet = entity;
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
