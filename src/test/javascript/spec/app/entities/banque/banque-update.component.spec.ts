import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BtpprojectTestModule } from '../../../test.module';
import { BanqueUpdateComponent } from 'app/entities/banque/banque-update.component';
import { BanqueService } from 'app/entities/banque/banque.service';
import { Banque } from 'app/shared/model/banque.model';

describe('Component Tests', () => {
  describe('Banque Management Update Component', () => {
    let comp: BanqueUpdateComponent;
    let fixture: ComponentFixture<BanqueUpdateComponent>;
    let service: BanqueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BtpprojectTestModule],
        declarations: [BanqueUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BanqueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BanqueUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BanqueService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Banque(123);
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
        const entity = new Banque();
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
