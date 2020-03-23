import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMaitreOuvrage, MaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';
import { MaitreOuvrageService } from './maitre-ouvrage.service';

@Component({
  selector: 'jhi-maitre-ouvrage-update',
  templateUrl: './maitre-ouvrage-update.component.html'
})
export class MaitreOuvrageUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [],
    email: [],
    tel: [],
    contactPersonne: []
  });

  constructor(protected maitreOuvrageService: MaitreOuvrageService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maitreOuvrage }) => {
      this.updateForm(maitreOuvrage);
    });
  }

  updateForm(maitreOuvrage: IMaitreOuvrage): void {
    this.editForm.patchValue({
      id: maitreOuvrage.id,
      nom: maitreOuvrage.nom,
      email: maitreOuvrage.email,
      tel: maitreOuvrage.tel,
      contactPersonne: maitreOuvrage.contactPersonne
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const maitreOuvrage = this.createFromForm();
    if (maitreOuvrage.id !== undefined) {
      this.subscribeToSaveResponse(this.maitreOuvrageService.update(maitreOuvrage));
    } else {
      this.subscribeToSaveResponse(this.maitreOuvrageService.create(maitreOuvrage));
    }
  }

  private createFromForm(): IMaitreOuvrage {
    return {
      ...new MaitreOuvrage(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      email: this.editForm.get(['email'])!.value,
      tel: this.editForm.get(['tel'])!.value,
      contactPersonne: this.editForm.get(['contactPersonne'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaitreOuvrage>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
