import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';
import { MaitreOuvrageService } from './maitre-ouvrage.service';

@Component({
  templateUrl: './maitre-ouvrage-delete-dialog.component.html'
})
export class MaitreOuvrageDeleteDialogComponent {
  maitreOuvrage?: IMaitreOuvrage;

  constructor(
    protected maitreOuvrageService: MaitreOuvrageService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.maitreOuvrageService.delete(id).subscribe(() => {
      this.eventManager.broadcast('maitreOuvrageListModification');
      this.activeModal.close();
    });
  }
}
