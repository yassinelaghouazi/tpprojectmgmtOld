import { Moment } from 'moment';
import { IBanque } from 'app/shared/model/banque.model';
import { IMaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';
import { TypeCaution } from 'app/shared/model/enumerations/type-caution.model';
import { StatusCaution } from 'app/shared/model/enumerations/status-caution.model';

export interface ICaution {
  id?: number;
  numeroCaution?: string;
  numeroAppelOffre?: string;
  numeroMarche?: string;
  objetCaution?: string;
  typeCaution?: TypeCaution;
  montantCaution?: number;
  statusCaution?: StatusCaution;
  dateDemande?: Moment;
  dateRetrait?: Moment;
  dateDepot?: Moment;
  banque?: IBanque;
  maitreOuvrage?: IMaitreOuvrage;
}

export class Caution implements ICaution {
  constructor(
    public id?: number,
    public numeroCaution?: string,
    public numeroAppelOffre?: string,
    public numeroMarche?: string,
    public objetCaution?: string,
    public typeCaution?: TypeCaution,
    public montantCaution?: number,
    public statusCaution?: StatusCaution,
    public dateDemande?: Moment,
    public dateRetrait?: Moment,
    public dateDepot?: Moment,
    public banque?: IBanque,
    public maitreOuvrage?: IMaitreOuvrage
  ) {}
}
