import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';

@Component({
  selector: 'jhi-maitre-ouvrage-detail',
  templateUrl: './maitre-ouvrage-detail.component.html'
})
export class MaitreOuvrageDetailComponent implements OnInit {
  maitreOuvrage: IMaitreOuvrage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maitreOuvrage }) => (this.maitreOuvrage = maitreOuvrage));
  }

  previousState(): void {
    window.history.back();
  }
}
