/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { CyclingUpdateComponent } from 'app/entities/cycling/cycling-update.component';
import { CyclingService } from 'app/entities/cycling/cycling.service';
import { Cycling } from 'app/shared/model/cycling.model';

describe('Component Tests', () => {
    describe('Cycling Management Update Component', () => {
        let comp: CyclingUpdateComponent;
        let fixture: ComponentFixture<CyclingUpdateComponent>;
        let service: CyclingService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [CyclingUpdateComponent]
            })
                .overrideTemplate(CyclingUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CyclingUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CyclingService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Cycling(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.cycling = entity;
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
                    const entity = new Cycling();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.cycling = entity;
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
