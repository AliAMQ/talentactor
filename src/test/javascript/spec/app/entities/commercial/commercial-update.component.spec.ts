/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { CommercialUpdateComponent } from 'app/entities/commercial/commercial-update.component';
import { CommercialService } from 'app/entities/commercial/commercial.service';
import { Commercial } from 'app/shared/model/commercial.model';

describe('Component Tests', () => {
    describe('Commercial Management Update Component', () => {
        let comp: CommercialUpdateComponent;
        let fixture: ComponentFixture<CommercialUpdateComponent>;
        let service: CommercialService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [CommercialUpdateComponent]
            })
                .overrideTemplate(CommercialUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CommercialUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CommercialService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Commercial(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.commercial = entity;
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
                    const entity = new Commercial();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.commercial = entity;
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
