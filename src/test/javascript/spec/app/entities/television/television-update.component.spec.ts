/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { TelevisionUpdateComponent } from 'app/entities/television/television-update.component';
import { TelevisionService } from 'app/entities/television/television.service';
import { Television } from 'app/shared/model/television.model';

describe('Component Tests', () => {
    describe('Television Management Update Component', () => {
        let comp: TelevisionUpdateComponent;
        let fixture: ComponentFixture<TelevisionUpdateComponent>;
        let service: TelevisionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [TelevisionUpdateComponent]
            })
                .overrideTemplate(TelevisionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TelevisionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TelevisionService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Television(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.television = entity;
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
                    const entity = new Television();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.television = entity;
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
