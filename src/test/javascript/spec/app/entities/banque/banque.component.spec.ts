import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BtpprojectTestModule } from '../../../test.module';
import { BanqueComponent } from 'app/entities/banque/banque.component';
import { BanqueService } from 'app/entities/banque/banque.service';
import { Banque } from 'app/shared/model/banque.model';

describe('Component Tests', () => {
  describe('Banque Management Component', () => {
    let comp: BanqueComponent;
    let fixture: ComponentFixture<BanqueComponent>;
    let service: BanqueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BtpprojectTestModule],
        declarations: [BanqueComponent]
      })
        .overrideTemplate(BanqueComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BanqueComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BanqueService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Banque(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.banques && comp.banques[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
