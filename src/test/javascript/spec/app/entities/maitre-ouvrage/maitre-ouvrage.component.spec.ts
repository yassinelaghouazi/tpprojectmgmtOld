import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BtpprojectTestModule } from '../../../test.module';
import { MaitreOuvrageComponent } from 'app/entities/maitre-ouvrage/maitre-ouvrage.component';
import { MaitreOuvrageService } from 'app/entities/maitre-ouvrage/maitre-ouvrage.service';
import { MaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';

describe('Component Tests', () => {
  describe('MaitreOuvrage Management Component', () => {
    let comp: MaitreOuvrageComponent;
    let fixture: ComponentFixture<MaitreOuvrageComponent>;
    let service: MaitreOuvrageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BtpprojectTestModule],
        declarations: [MaitreOuvrageComponent]
      })
        .overrideTemplate(MaitreOuvrageComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MaitreOuvrageComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MaitreOuvrageService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new MaitreOuvrage(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.maitreOuvrages && comp.maitreOuvrages[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
