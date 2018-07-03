/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TalentactorTestModule } from '../../../test.module';
import { CombatDetailComponent } from 'app/entities/combat/combat-detail.component';
import { Combat } from 'app/shared/model/combat.model';

describe('Component Tests', () => {
    describe('Combat Management Detail Component', () => {
        let comp: CombatDetailComponent;
        let fixture: ComponentFixture<CombatDetailComponent>;
        const route = ({ data: of({ combat: new Combat(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TalentactorTestModule],
                declarations: [CombatDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CombatDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CombatDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.combat).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
