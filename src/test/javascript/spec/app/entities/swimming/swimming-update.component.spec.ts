/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { SwimmingUpdateComponent } from 'app/entities/swimming/swimming-update.component';
import { SwimmingService } from 'app/entities/swimming/swimming.service';
import { Swimming } from 'app/shared/model/swimming.model';

describe('Component Tests', () => {
    describe('Swimming Management Update Component', () => {
        let comp: SwimmingUpdateComponent;
        let fixture: ComponentFixture<SwimmingUpdateComponent>;
        let service: SwimmingService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [SwimmingUpdateComponent]
            })
                .overrideTemplate(SwimmingUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SwimmingUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SwimmingService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Swimming(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.swimming = entity;
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
                    const entity = new Swimming();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.swimming = entity;
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
