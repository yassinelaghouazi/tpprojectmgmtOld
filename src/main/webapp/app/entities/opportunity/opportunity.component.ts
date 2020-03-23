import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOpportunity } from 'app/shared/model/opportunity.model';
import { OpportunityService } from './opportunity.service';
import { OpportunityDeleteDialogComponent } from './opportunity-delete-dialog.component';

@Component({
  selector: 'jhi-opportunity',
  templateUrl: './opportunity.component.html'
})
export class OpportunityComponent implements OnInit, OnDestroy {
  opportunities?: IOpportunity[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected opportunityService: OpportunityService,
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
      this.opportunityService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IOpportunity[]>) => (this.opportunities = res.body || []));
      return;
    }

    this.opportunityService.query().subscribe((res: HttpResponse<IOpportunity[]>) => (this.opportunities = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInOpportunities();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOpportunity): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInOpportunities(): void {
    this.eventSubscriber = this.eventManager.subscribe('opportunityListModification', () => this.loadAll());
  }

  delete(opportunity: IOpportunity): void {
    const modalRef = this.modalService.open(OpportunityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.opportunity = opportunity;
  }
}
