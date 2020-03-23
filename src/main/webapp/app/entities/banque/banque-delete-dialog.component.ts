import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBanque } from 'app/shared/model/banque.model';
import { BanqueService } from './banque.service';

@Component({
  templateUrl: './banque-delete-dialog.component.html'
})
export class BanqueDeleteDialogComponent {
  banque?: IBanque;

  constructor(protected banqueService: BanqueService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.banqueService.delete(id).subscribe(() => {
      this.eventManager.broadcast('banqueListModification');
      this.activeModal.close();
    });
  }
}
