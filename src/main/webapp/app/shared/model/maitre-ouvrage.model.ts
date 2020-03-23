export interface IMaitreOuvrage {
  id?: number;
  nom?: string;
  email?: string;
  tel?: string;
  contactPersonne?: string;
}

export class MaitreOuvrage implements IMaitreOuvrage {
  constructor(public id?: number, public nom?: string, public email?: string, public tel?: string, public contactPersonne?: string) {}
}
