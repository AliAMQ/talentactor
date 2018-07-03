/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { HorseUpdateComponent } from 'app/entities/horse/horse-update.component';
import { HorseService } from 'app/entities/horse/horse.service';
import { Horse } from 'app/shared/model/horse.model';

describe('Component Tests', () => {
    describe('Horse Management Update Component', () => {
        let comp: HorseUpdateComponent;
        let fixture: ComponentFixture<HorseUpdateComponent>;
        let service: HorseService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [HorseUpdateComponent]
            })
                .overrideTemplate(HorseUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HorseUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HorseService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Horse(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.horse = entity;
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
                    const entity = new Horse();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.horse = entity;
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
