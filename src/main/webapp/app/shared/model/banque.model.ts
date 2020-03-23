export interface IBanque {
  id?: number;
  banque?: string;
  contactEmail?: string;
  contactTel?: string;
  adresseAgence?: string;
}

export class Banque implements IBanque {
  constructor(
    public id?: number,
    public banque?: string,
    public contactEmail?: string,
    public contactTel?: string,
    public adresseAgence?: string
  ) {}
}
