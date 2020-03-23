import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BtpprojectTestModule } from '../../../test.module';
import { CautionUpdateComponent } from 'app/entities/caution/caution-update.component';
import { CautionService } from 'app/entities/caution/caution.service';
import { Caution } from 'app/shared/model/caution.model';

describe('Component Tests', () => {
  describe('Caution Management Update Component', () => {
    let comp: CautionUpdateComponent;
    let fixture: ComponentFixture<CautionUpdateComponent>;
    let service: CautionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BtpprojectTestModule],
        declarations: [CautionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CautionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CautionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CautionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Caution(123);
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
        const entity = new Caution();
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
