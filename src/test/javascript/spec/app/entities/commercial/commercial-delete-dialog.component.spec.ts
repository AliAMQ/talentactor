/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TalentactorTestModule } from '../../../test.module';
import { CommercialDeleteDialogComponent } from 'app/entities/commercial/commercial-delete-dialog.component';
import { CommercialService } from 'app/entities/commercial/commercial.service';

describe('Component Tests', () => {
    describe('Commercial Management Delete Component', () => {
        let comp: CommercialDeleteDialogComponent;
        let fixture: ComponentFixture<CommercialDeleteDialogComponent>;
        let service: CommercialService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [CommercialDeleteDialogComponent]
            })
                .overrideTemplate(CommercialDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CommercialDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CommercialService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
