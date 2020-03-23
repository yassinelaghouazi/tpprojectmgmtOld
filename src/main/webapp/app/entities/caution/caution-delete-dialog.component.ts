import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICaution } from 'app/shared/model/caution.model';
import { CautionService } from './caution.service';

@Component({
  templateUrl: './caution-delete-dialog.component.html'
})
export class CautionDeleteDialogComponent {
  caution?: ICaution;

  constructor(protected cautionService: CautionService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cautionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('cautionListModification');
      this.activeModal.close();
    });
  }
}
