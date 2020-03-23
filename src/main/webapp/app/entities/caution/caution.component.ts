import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICaution } from 'app/shared/model/caution.model';
import { CautionService } from './caution.service';
import { CautionDeleteDialogComponent } from './caution-delete-dialog.component';

@Component({
  selector: 'jhi-caution',
  templateUrl: './caution.component.html'
})
export class CautionComponent implements OnInit, OnDestroy {
  cautions?: ICaution[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected cautionService: CautionService,
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
      this.cautionService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<ICaution[]>) => (this.cautions = res.body || []));
      return;
    }

    this.cautionService.query().subscribe((res: HttpResponse<ICaution[]>) => (this.cautions = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCautions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICaution): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCautions(): void {
    this.eventSubscriber = this.eventManager.subscribe('cautionListModification', () => this.loadAll());
  }

  delete(caution: ICaution): void {
    const modalRef = this.modalService.open(CautionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.caution = caution;
  }
}
