import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICaution } from 'app/shared/model/caution.model';

@Component({
  selector: 'jhi-caution-detail',
  templateUrl: './caution-detail.component.html'
})
export class CautionDetailComponent implements OnInit {
  caution: ICaution | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caution }) => (this.caution = caution));
  }

  previousState(): void {
    window.history.back();
  }
}
