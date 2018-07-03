/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { TheaterUpdateComponent } from 'app/entities/theater/theater-update.component';
import { TheaterService } from 'app/entities/theater/theater.service';
import { Theater } from 'app/shared/model/theater.model';

describe('Component Tests', () => {
    describe('Theater Management Update Component', () => {
        let comp: TheaterUpdateComponent;
        let fixture: ComponentFixture<TheaterUpdateComponent>;
        let service: TheaterService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [TheaterUpdateComponent]
            })
                .overrideTemplate(TheaterUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TheaterUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TheaterService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Theater(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.theater = entity;
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
                    const entity = new Theater();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.theater = entity;
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
