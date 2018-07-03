/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { CircusUpdateComponent } from 'app/entities/circus/circus-update.component';
import { CircusService } from 'app/entities/circus/circus.service';
import { Circus } from 'app/shared/model/circus.model';

describe('Component Tests', () => {
    describe('Circus Management Update Component', () => {
        let comp: CircusUpdateComponent;
        let fixture: ComponentFixture<CircusUpdateComponent>;
        let service: CircusService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [CircusUpdateComponent]
            })
                .overrideTemplate(CircusUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CircusUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CircusService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Circus(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.circus = entity;
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
                    const entity = new Circus();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.circus = entity;
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
