import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';
import { MaitreOuvrageService } from './maitre-ouvrage.service';
import { MaitreOuvrageDeleteDialogComponent } from './maitre-ouvrage-delete-dialog.component';

@Component({
  selector: 'jhi-maitre-ouvrage',
  templateUrl: './maitre-ouvrage.component.html'
})
export class MaitreOuvrageComponent implements OnInit, OnDestroy {
  maitreOuvrages?: IMaitreOuvrage[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected maitreOuvrageService: MaitreOuvrageService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.maitreOuvrageService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IMaitreOuvrage[]>) => (this.maitreOuvrages = res.body || []));
      return;
    }

    this.maitreOuvrageService.query().subscribe((res: HttpResponse<IMaitreOuvrage[]>) => (this.maitreOuvrages = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMaitreOuvrages();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMaitreOuvrage): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMaitreOuvrages(): void {
    this.eventSubscriber = this.eventManager.subscribe('maitreOuvrageListModification', () => this.loadAll());
  }

  delete(maitreOuvrage: IMaitreOuvrage): void {
    const modalRef = this.modalService.open(MaitreOuvrageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.maitreOuvrage = maitreOuvrage;
  }
}
