import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BtpprojectTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { MaitreOuvrageDeleteDialogComponent } from 'app/entities/maitre-ouvrage/maitre-ouvrage-delete-dialog.component';
import { MaitreOuvrageService } from 'app/entities/maitre-ouvrage/maitre-ouvrage.service';

describe('Component Tests', () => {
  describe('MaitreOuvrage Management Delete Component', () => {
    let comp: MaitreOuvrageDeleteDialogComponent;
    let fixture: ComponentFixture<MaitreOuvrageDeleteDialogComponent>;
    let service: MaitreOuvrageService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BtpprojectTestModule],
        declarations: [MaitreOuvrageDeleteDialogComponent]
      })
        .overrideTemplate(MaitreOuvrageDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MaitreOuvrageDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MaitreOuvrageService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
