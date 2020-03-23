import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BtpprojectTestModule } from '../../../test.module';
import { MaitreOuvrageUpdateComponent } from 'app/entities/maitre-ouvrage/maitre-ouvrage-update.component';
import { MaitreOuvrageService } from 'app/entities/maitre-ouvrage/maitre-ouvrage.service';
import { MaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';

describe('Component Tests', () => {
  describe('MaitreOuvrage Management Update Component', () => {
    let comp: MaitreOuvrageUpdateComponent;
    let fixture: ComponentFixture<MaitreOuvrageUpdateComponent>;
    let service: MaitreOuvrageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BtpprojectTestModule],
        declarations: [MaitreOuvrageUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MaitreOuvrageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MaitreOuvrageUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MaitreOuvrageService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MaitreOuvrage(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new MaitreOuvrage();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
