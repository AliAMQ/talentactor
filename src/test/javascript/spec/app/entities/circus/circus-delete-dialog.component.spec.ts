/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TalentactorTestModule } from '../../../test.module';
import { CircusDeleteDialogComponent } from 'app/entities/circus/circus-delete-dialog.component';
import { CircusService } from 'app/entities/circus/circus.service';

describe('Component Tests', () => {
    describe('Circus Management Delete Component', () => {
        let comp: CircusDeleteDialogComponent;
        let fixture: ComponentFixture<CircusDeleteDialogComponent>;
        let service: CircusService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [CircusDeleteDialogComponent]
            })
                .overrideTemplate(CircusDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CircusDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CircusService);
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
