import { Component, OnInit, Input } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICaution, Caution } from 'app/shared/model/caution.model';
import { CautionService } from './caution.service';
import { IBanque } from 'app/shared/model/banque.model';
import { BanqueService } from 'app/entities/banque/banque.service';
import { IMaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';
import { MaitreOuvrageService } from 'app/entities/maitre-ouvrage/maitre-ouvrage.service';

type SelectableEntity = IBanque | IMaitreOuvrage;

@Component({
  selector: 'jhi-caution-update',
  templateUrl: './caution-update.component.html'
})
export class CautionUpdateComponent implements OnInit {
  @Input() caution: ICaution = new Caution();

  isSaving = false;
  banques: IBanque[] = [];
  maitreouvrages: IMaitreOuvrage[] = [];
  dateDemandeDp: any;
  dateRetraitDp: any;
  dateDepotDp: any;

  editForm = this.fb.group({
    id: [],
    numeroCaution: [],
    numeroAppelOffre: [],
    numeroMarche: [],
    objetCaution: [],
    typeCaution: [],
    montantCaution: [],
    statusCaution: [],
    dateDemande: [],
    dateRetrait: [],
    dateDepot: [],
    banque: [null, Validators.required],
    maitreOuvrage: [null, Validators.required]
  });

  constructor(
    protected cautionService: CautionService,
    protected banqueService: BanqueService,
    protected maitreOuvrageService: MaitreOuvrageService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caution }) => {
      this.updateForm(caution);

      this.banqueService.query().subscribe((res: HttpResponse<IBanque[]>) => (this.banques = res.body || []));

      this.maitreOuvrageService.query().subscribe((res: HttpResponse<IMaitreOuvrage[]>) => (this.maitreouvrages = res.body || []));
    });
  }

  updateForm(caution: ICaution): void {
    this.editForm.patchValue({
      id: caution.id,
      numeroCaution: caution.numeroCaution,
      numeroAppelOffre: caution.numeroAppelOffre,
      numeroMarche: caution.numeroMarche,
      objetCaution: caution.objetCaution,
      typeCaution: caution.typeCaution,
      montantCaution: caution.montantCaution,
      statusCaution: caution.statusCaution,
      dateDemande: caution.dateDemande,
      dateRetrait: caution.dateRetrait,
      dateDepot: caution.dateDepot,
      banque: caution.banque,
      maitreOuvrage: caution.maitreOuvrage
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const caution = this.createFromForm();
    if (caution.id !== undefined) {
      this.subscribeToSaveResponse(this.cautionService.update(caution));
    } else {
      this.subscribeToSaveResponse(this.cautionService.create(caution));
    }
  }

  private createFromForm(): ICaution {
    return {
      ...new Caution(),
      id: this.editForm.get(['id'])!.value,
      numeroCaution: this.editForm.get(['numeroCaution'])!.value,
      numeroAppelOffre: this.editForm.get(['numeroAppelOffre'])!.value,
      numeroMarche: this.editForm.get(['numeroMarche'])!.value,
      objetCaution: this.editForm.get(['objetCaution'])!.value,
      typeCaution: this.editForm.get(['typeCaution'])!.value,
      montantCaution: this.editForm.get(['montantCaution'])!.value,
      statusCaution: this.editForm.get(['statusCaution'])!.value,
      dateDemande: this.editForm.get(['dateDemande'])!.value,
      dateRetrait: this.editForm.get(['dateRetrait'])!.value,
      dateDepot: this.editForm.get(['dateDepot'])!.value,
      banque: this.editForm.get(['banque'])!.value,
      maitreOuvrage: this.editForm.get(['maitreOuvrage'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICaution>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
