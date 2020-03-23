import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'opportunity',
        loadChildren: () => import('./opportunity/opportunity.module').then(m => m.BtpprojectOpportunityModule)
      },
      {
        path: 'maitre-ouvrage',
        loadChildren: () => import('./maitre-ouvrage/maitre-ouvrage.module').then(m => m.BtpprojectMaitreOuvrageModule)
      },
      {
        path: 'caution',
        loadChildren: () => import('./caution/caution.module').then(m => m.BtpprojectCautionModule)
      },
      {
        path: 'banque',
        loadChildren: () => import('./banque/banque.module').then(m => m.BtpprojectBanqueModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class BtpprojectEntityModule {}
