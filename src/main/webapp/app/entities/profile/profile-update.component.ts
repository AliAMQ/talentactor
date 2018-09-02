import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from './profile.service';
import { IUser, UserService, Principal, AccountService, JhiLanguageHelper } from 'app/core';
import { ISkill } from 'app/shared/model/skill.model';
import { SkillService } from 'app/entities/skill';
import { ISport } from 'app/shared/model/sport.model';
import { SportService } from 'app/entities/sport';
import { ISwimming } from 'app/shared/model/swimming.model';
import { SwimmingService } from 'app/entities/swimming';
import { ICombat } from 'app/shared/model/combat.model';
import { CombatService } from 'app/entities/combat';
import { ILanguage } from 'app/shared/model/language.model';
import { LanguageService } from 'app/entities/language';
import { IInstrument } from 'app/shared/model/instrument.model';
import { InstrumentService } from 'app/entities/instrument';
import { IWeapon } from 'app/shared/model/weapon.model';
import { WeaponService } from 'app/entities/weapon';
import { ICycling } from 'app/shared/model/cycling.model';
import { CyclingService } from 'app/entities/cycling';
import { ICircus } from 'app/shared/model/circus.model';
import { CircusService } from 'app/entities/circus';
import { IHorse } from 'app/shared/model/horse.model';
import { HorseService } from 'app/entities/horse';

import { FileManagementService } from '../../shared/file/file-management.service';
import { JhiLanguageService } from 'ng-jhipster';

@Component({
    selector: 'jhi-profile-update',
    templateUrl: './profile-update.component.html'
})
export class ProfileUpdateComponent implements OnInit {
    private _profile: IProfile;
    isSaving: boolean;

    users: IUser[];

    skills: ISkill[];

    sports: ISport[];

    swimmings: ISwimming[];

    combats: ICombat[];

    languages: ILanguage[];

    instruments: IInstrument[];

    weapons: IWeapon[];

    cyclings: ICycling[];

    circuses: ICircus[];

    horses: IHorse[];
    sinceDp: any;

    selectedFiles: FileList;
    settingsAccount: any;
    error: string;
    success: string;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private profileService: ProfileService,
        private userService: UserService,
        private skillService: SkillService,
        private sportService: SportService,
        private swimmingService: SwimmingService,
        private combatService: CombatService,
        private languageService: LanguageService,
        private instrumentService: InstrumentService,
        private weaponService: WeaponService,
        private cyclingService: CyclingService,
        private circusService: CircusService,
        private horseService: HorseService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        public fileManagementService: FileManagementService,
        private account: AccountService,
        private jhilanguageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ profile }) => {
            this.profile = profile;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.skillService.query().subscribe(
            (res: HttpResponse<ISkill[]>) => {
                this.skills = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.sportService.query().subscribe(
            (res: HttpResponse<ISport[]>) => {
                this.sports = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.swimmingService.query().subscribe(
            (res: HttpResponse<ISwimming[]>) => {
                this.swimmings = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.combatService.query().subscribe(
            (res: HttpResponse<ICombat[]>) => {
                this.combats = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.languageService.query().subscribe(
            (res: HttpResponse<ILanguage[]>) => {
                this.languages = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.instrumentService.query().subscribe(
            (res: HttpResponse<IInstrument[]>) => {
                this.instruments = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.weaponService.query().subscribe(
            (res: HttpResponse<IWeapon[]>) => {
                this.weapons = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.cyclingService.query().subscribe(
            (res: HttpResponse<ICycling[]>) => {
                this.cyclings = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.circusService.query().subscribe(
            (res: HttpResponse<ICircus[]>) => {
                this.circuses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.horseService.query().subscribe(
            (res: HttpResponse<IHorse[]>) => {
                this.horses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.principal.identity().then(account => {
            this.settingsAccount = this.copyAccount(account);
        });
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    /*clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.profile, this.elementRef, field, fieldContentType, idInput);
    }*/

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.profile.id !== undefined) {
            if (this.selectedFiles !== undefined) {
                if (this.profile.imagepath !== null) {
                    this.fileManagementService.deleteFile(this.profile.imagepath);
                }
                if ((document.getElementById('imagepath1') as HTMLImageElement).hidden !== true) {
                    this.profile.imagepath = this.upload('imagepath1');
                } else {
                    this.profile.imagepath = null;
                }
            } else {
                if ((document.getElementById('imagepath1') as HTMLImageElement).hidden === true) {
                    if (this.profile.imagepath !== null) {
                        this.fileManagementService.deleteFile(this.profile.imagepath);
                        this.profile.imagepath = null;
                    }
                }
            }
            this.subscribeToSaveResponse(this.profileService.update(this.profile));
        } else {
            if (this.selectedFiles !== undefined) {
                if ((document.getElementById('imagepath1') as HTMLImageElement).hidden !== true) {
                    this.profile.imagepath = this.upload('imagepath1');
                }
            }
            this.subscribeToSaveResponse(this.profileService.create(this.profile));
        }
        this.saveAccount();
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProfile>>) {
        result.subscribe((res: HttpResponse<IProfile>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        global.setTimeout(this.previousState, 1000);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackSkillById(index: number, item: ISkill) {
        return item.id;
    }

    trackSportById(index: number, item: ISport) {
        return item.id;
    }

    trackSwimmingById(index: number, item: ISwimming) {
        return item.id;
    }

    trackCombatById(index: number, item: ICombat) {
        return item.id;
    }

    trackLanguageById(index: number, item: ILanguage) {
        return item.id;
    }

    trackInstrumentById(index: number, item: IInstrument) {
        return item.id;
    }

    trackWeaponById(index: number, item: IWeapon) {
        return item.id;
    }

    trackCyclingById(index: number, item: ICycling) {
        return item.id;
    }

    trackCircusById(index: number, item: ICircus) {
        return item.id;
    }

    trackHorseById(index: number, item: IHorse) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get profile() {
        return this._profile;
    }

    set profile(profile: IProfile) {
        this._profile = profile;
    }

    copyAccount(account) {
        return {
            activated: account.activated,
            email: account.email,
            firstName: account.firstName,
            langKey: account.langKey,
            lastName: account.lastName,
            login: account.login,
            imageUrl: account.imageUrl
        };
    }

    selectFile(event, image, input) {
        const reader = new FileReader();
        reader.addEventListener(
            'load',
            function() {
                (document.getElementById(image) as HTMLImageElement).src = reader.result;
                (document.getElementById(image) as HTMLImageElement).hidden = false;
            },
            false
        );
        reader.readAsDataURL((document.getElementById(input) as HTMLInputElement).files[0]);
        if (image === 'imagepath1') {
            this.selectedFiles = event.target.files;
            (document.getElementById('close') as HTMLButtonElement).hidden = false;
        }
    }

    upload(image): string {
        let file;
        if (image === 'imagepath1') {
            file = this.selectedFiles.item(0);
        }
        return this.fileManagementService.uploadfile(file);
    }

    clearInputImage(image, button, input) {
        (document.getElementById(image) as HTMLImageElement).src = '';
        (document.getElementById(image) as HTMLImageElement).hidden = true;
        (document.getElementById(button) as HTMLButtonElement).hidden = true;
        (document.getElementById(input) as HTMLInputElement).value = null;
    }

    saveAccount() {
        this.account.save(this.settingsAccount).subscribe(
            () => {
                this.error = null;
                this.success = 'OK';
                this.principal.identity(true).then(account => {
                    this.settingsAccount = this.copyAccount(account);
                });
                this.jhilanguageService.getCurrent().then(current => {
                    if (this.settingsAccount.langKey !== current) {
                        this.jhilanguageService.changeLanguage(this.settingsAccount.langKey);
                    }
                });
            },
            () => {
                this.success = null;
                this.error = 'ERROR';
            }
        );
    }
}
