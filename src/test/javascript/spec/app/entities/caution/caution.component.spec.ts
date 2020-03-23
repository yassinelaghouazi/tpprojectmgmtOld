import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BtpprojectTestModule } from '../../../test.module';
import { CautionComponent } from 'app/entities/caution/caution.component';
import { CautionService } from 'app/entities/caution/caution.service';
import { Caution } from 'app/shared/model/caution.model';

describe('Component Tests', () => {
  describe('Caution Management Component', () => {
    let comp: CautionComponent;
    let fixture: ComponentFixture<CautionComponent>;
    let service: CautionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BtpprojectTestModule],
        declarations: [CautionComponent]
      })
        .overrideTemplate(CautionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CautionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CautionService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Caution(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cautions && comp.cautions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
