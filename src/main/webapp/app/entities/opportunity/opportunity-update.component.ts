import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars

import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IOpportunity, Opportunity } from 'app/shared/model/opportunity.model';
import { OpportunityService } from './opportunity.service';
import { IBanque } from 'app/shared/model/banque.model';
import { BanqueService } from 'app/entities/banque/banque.service';
import { IMaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';
import { MaitreOuvrageService } from 'app/entities/maitre-ouvrage/maitre-ouvrage.service';
import { ICaution, Caution } from 'app/shared/model/caution.model';
import { CautionService } from 'app/entities/caution/caution.service';

type SelectableEntity = IMaitreOuvrage | ICaution;

@Component({
  selector: 'jhi-opportunity-update',
  templateUrl: './opportunity-update.component.html'
})
export class OpportunityUpdateComponent implements OnInit {
  isSaving = false;
  maitreouvrages: IMaitreOuvrage[] = [];
  cautions: ICaution[] = [];
  cautionSaved: ICaution = new Caution();
  dateRemisePlisDp: any;
  // add caution part ;
  // begin;
  banquesCaution: IBanque[] = [];
  dateDemandeDpCaution: any;
  dateRetraitDpCaution: any;
  dateDepotDpCaution: any;
  // end

  editForm = this.fb.group({
    id: [],
    numeroAppelOffre: [null, [Validators.required]],
    dateRemisePlis: [],
    montantCaution: [],
    objetAffaire: [],
    estimationBudget: [],
    commentaires: [],
    maitreOuvrage: [null, Validators.required],
    // caution: [null, Validators.required],
    // add caution part ;
    // begin;
    idCaution: [],
    numeroCaution: [null, Validators.required],
    numeroAppelOffreCaution: [{ value: '', disabled: true }, []],
    objetCaution: [null, Validators.required],
    typeCaution: [{ value: '', disabled: true }, []],
    montantSubCaution: [{ value: '', disabled: true }, []],
    statusCaution: [null, Validators.required],
    dateDemande: [{ value: '', disabled: false }, []],
    dateRetrait: [],
    dateDepot: [],
    banqueCaution: [null, Validators.required],
    maitreOuvrageCaution: [{ value: '', disabled: true }, []]
    // end;
  });

  constructor(
    protected opportunityService: OpportunityService,
    protected maitreOuvrageService: MaitreOuvrageService,
    protected banqueService: BanqueService,
    protected cautionService: CautionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ opportunity }) => {
      this.updateForm(opportunity);

      this.maitreOuvrageService.query().subscribe((res: HttpResponse<IMaitreOuvrage[]>) => (this.maitreouvrages = res.body || []));

      this.banqueService.query().subscribe((res: HttpResponse<IBanque[]>) => (this.banquesCaution = res.body || []));
    });
  }

  updateForm(opportunity: IOpportunity): void {
    this.editForm.patchValue({
      id: opportunity.id,
      numeroAppelOffre: opportunity.numeroAppelOffre,
      dateRemisePlis: opportunity.dateRemisePlis,
      montantCaution: opportunity.montantCaution,
      objetAffaire: opportunity.objetAffaire,
      estimationBudget: opportunity.estimationBudget,
      commentaires: opportunity.commentaires,
      maitreOuvrage: opportunity.maitreOuvrage,
      caution: opportunity.caution,
      idCaution: opportunity.caution?.id,
      numeroCaution: opportunity.caution?.numeroCaution,
      numeroAppelOffreCaution: opportunity.caution?.numeroAppelOffre,
      objetCaution: opportunity.caution?.objetCaution,
      typeCaution: opportunity.caution?.typeCaution !== undefined ? opportunity.caution?.typeCaution : 'PROVISOIRE',
      montantSubCaution: opportunity.caution?.montantCaution,
      statusCaution: opportunity.caution?.statusCaution,
      dateDemande: opportunity.caution?.dateDemande,
      dateRetrait: opportunity.caution?.dateRetrait,
      dateDepot: opportunity.caution?.dateDepot,
      banqueCaution: opportunity.caution?.banque,
      maitreOuvrageCaution: opportunity.caution?.maitreOuvrage
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const opportunity = this.createFromForm();
    const caution = this.createCautionFromForm();
    if (opportunity.id === undefined) {
      opportunity.caution = caution;
      this.subscribeToSaveOpportunityResponse(this.opportunityService.create(opportunity));
    } else {
      let result: Observable<HttpResponse<ICaution>>;
      new Promise((resolve, reject) => {
        if (caution.id !== undefined) {
          result = this.cautionService.update(caution);
        } else {
          result = this.cautionService.create(caution);
        }
        result.subscribe(
          res => {
            const bodyRes = res.body;
            this.cautionSaved = bodyRes != null ? bodyRes : new Caution();
            /* eslint-disable no-console */
            console.log(result);
            console.log(res);
            console.log(bodyRes);
            console.log(typeof bodyRes);
            /* eslint-enable no-console */
            opportunity.caution = this.cautionSaved;
            resolve(this.cautionSaved);
          },
          () => {
            reject('Échec');
          }
        );
      }).then(() => {
        this.subscribeToSaveOpportunityResponse(this.opportunityService.update(opportunity));
      });
    }
  }
  private createFromForm(): IOpportunity {
    return {
      ...new Opportunity(),
      id: this.editForm.get(['id'])!.value,
      numeroAppelOffre: this.editForm.get(['numeroAppelOffre'])!.value,
      dateRemisePlis: this.editForm.get(['dateRemisePlis'])!.value,
      montantCaution: this.editForm.get(['montantCaution'])!.value,
      objetAffaire: this.editForm.get(['objetAffaire'])!.value,
      estimationBudget: this.editForm.get(['estimationBudget'])!.value,
      commentaires: this.editForm.get(['commentaires'])!.value,
      maitreOuvrage: this.editForm.get(['maitreOuvrage'])!.value
      // caution: this.editForm.get(['caution'])!.value
    };
  }

  private createCautionFromForm(): ICaution {
    return {
      ...new Caution(),
      id: this.editForm.get(['idCaution'])!.value,
      numeroCaution: this.editForm.get(['numeroCaution'])!.value,
      numeroAppelOffre: this.editForm.get(['numeroAppelOffreCaution'])!.value,
      objetCaution: this.editForm.get(['objetCaution'])!.value,
      typeCaution: this.editForm.get(['typeCaution'])!.value,
      montantCaution: this.editForm.get(['montantSubCaution'])!.value,
      statusCaution: this.editForm.get(['statusCaution'])!.value,
      dateDemande: this.editForm.get(['dateDemande'])!.value,
      dateRetrait: this.editForm.get(['dateRetrait'])!.value,
      dateDepot: this.editForm.get(['dateDepot'])!.value,
      banque: this.editForm.get(['banqueCaution'])!.value,
      maitreOuvrage: this.editForm.get(['maitreOuvrageCaution'])!.value
    };
  }

  protected subscribeToSaveOpportunityResponse(result: Observable<HttpResponse<IOpportunity>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected subscribeToSaveCautionResponse(result: Observable<HttpResponse<ICaution>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }
  onSaveCautionSuccess(): void {}

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

  onChangeMaitreOuvrage(): void {
    this.editForm.get(['maitreOuvrageCaution'])!.setValue(this.editForm.get(['maitreOuvrage'])!.value);
  }

  onChangeMontantCaution(value: string): void {
    this.editForm.get(['montantSubCaution'])!.setValue(value);
  }

  onChangeNumeroAO(value: string): void {
    this.editForm.get(['numeroAppelOffreCaution'])!.setValue(value);
  }
}
