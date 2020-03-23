import { Moment } from 'moment';
import { IMaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';
import { ICaution } from 'app/shared/model/caution.model';

export interface IOpportunity {
  id?: number;
  numeroAppelOffre?: string;
  dateRemisePlis?: Moment;
  montantCaution?: number;
  objetAffaire?: string;
  estimationBudget?: number;
  commentaires?: string;
  maitreOuvrage?: IMaitreOuvrage;
  caution?: ICaution;
}

export class Opportunity implements IOpportunity {
  constructor(
    public id?: number,
    public numeroAppelOffre?: string,
    public dateRemisePlis?: Moment,
    public montantCaution?: number,
    public objetAffaire?: string,
    public estimationBudget?: number,
    public commentaires?: string,
    public maitreOuvrage?: IMaitreOuvrage,
    public caution?: ICaution
  ) {}
}
