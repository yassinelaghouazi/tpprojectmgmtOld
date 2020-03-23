import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IOpportunity, Opportunity } from 'app/shared/model/opportunity.model';
import { OpportunityService } from './opportunity.service';
import { IMaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';
import { MaitreOuvrageService } from 'app/entities/maitre-ouvrage/maitre-ouvrage.service';
import { ICaution } from 'app/shared/model/caution.model';
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
  dateRemisePlisDp: any;

  editForm = this.fb.group({
    id: [],
    numeroAppelOffre: [null, [Validators.required]],
    dateRemisePlis: [],
    montantCaution: [],
    objetAffaire: [],
    estimationBudget: [],
    commentaires: [],
    maitreOuvrage: [null, Validators.required],
    caution: [null, Validators.required]
  });

  constructor(
    protected opportunityService: OpportunityService,
    protected maitreOuvrageService: MaitreOuvrageService,
    protected cautionService: CautionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ opportunity }) => {
      this.updateForm(opportunity);

      this.maitreOuvrageService.query().subscribe((res: HttpResponse<IMaitreOuvrage[]>) => (this.maitreouvrages = res.body || []));

      this.cautionService
        .query({ filter: 'opportunity-is-null' })
        .pipe(
          map((res: HttpResponse<ICaution[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICaution[]) => {
          if (!opportunity.caution || !opportunity.caution.id) {
            this.cautions = resBody;
          } else {
            this.cautionService
              .find(opportunity.caution.id)
              .pipe(
                map((subRes: HttpResponse<ICaution>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICaution[]) => (this.cautions = concatRes));
          }
        });
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
      caution: opportunity.caution
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const opportunity = this.createFromForm();
    if (opportunity.id !== undefined) {
      this.subscribeToSaveResponse(this.opportunityService.update(opportunity));
    } else {
      this.subscribeToSaveResponse(this.opportunityService.create(opportunity));
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
      maitreOuvrage: this.editForm.get(['maitreOuvrage'])!.value,
      caution: this.editForm.get(['caution'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOpportunity>>): void {
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
