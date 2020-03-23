import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BtpprojectTestModule } from '../../../test.module';
import { MaitreOuvrageDetailComponent } from 'app/entities/maitre-ouvrage/maitre-ouvrage-detail.component';
import { MaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';

describe('Component Tests', () => {
  describe('MaitreOuvrage Management Detail Component', () => {
    let comp: MaitreOuvrageDetailComponent;
    let fixture: ComponentFixture<MaitreOuvrageDetailComponent>;
    const route = ({ data: of({ maitreOuvrage: new MaitreOuvrage(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BtpprojectTestModule],
        declarations: [MaitreOuvrageDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MaitreOuvrageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MaitreOuvrageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load maitreOuvrage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.maitreOuvrage).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
