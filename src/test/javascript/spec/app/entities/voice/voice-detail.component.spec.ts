/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { VoiceDetailComponent } from 'app/entities/voice/voice-detail.component';
import { Voice } from 'app/shared/model/voice.model';

describe('Component Tests', () => {
    describe('Voice Management Detail Component', () => {
        let comp: VoiceDetailComponent;
        let fixture: ComponentFixture<VoiceDetailComponent>;
        const route = ({ data: of({ voice: new Voice(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [VoiceDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(VoiceDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(VoiceDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.voice).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
