import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BtpprojectSharedModule } from 'app/shared/shared.module';
import { MaitreOuvrageComponent } from './maitre-ouvrage.component';
import { MaitreOuvrageDetailComponent } from './maitre-ouvrage-detail.component';
import { MaitreOuvrageUpdateComponent } from './maitre-ouvrage-update.component';
import { MaitreOuvrageDeleteDialogComponent } from './maitre-ouvrage-delete-dialog.component';
import { maitreOuvrageRoute } from './maitre-ouvrage.route';

@NgModule({
  imports: [BtpprojectSharedModule, RouterModule.forChild(maitreOuvrageRoute)],
  declarations: [MaitreOuvrageComponent, MaitreOuvrageDetailComponent, MaitreOuvrageUpdateComponent, MaitreOuvrageDeleteDialogComponent],
  entryComponents: [MaitreOuvrageDeleteDialogComponent]
})
export class BtpprojectMaitreOuvrageModule {}
