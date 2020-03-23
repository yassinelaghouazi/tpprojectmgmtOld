import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BtpprojectSharedModule } from 'app/shared/shared.module';
import { CautionComponent } from './caution.component';
import { CautionDetailComponent } from './caution-detail.component';
import { CautionUpdateComponent } from './caution-update.component';
import { CautionDeleteDialogComponent } from './caution-delete-dialog.component';
import { cautionRoute } from './caution.route';

@NgModule({
  imports: [BtpprojectSharedModule, RouterModule.forChild(cautionRoute)],
  declarations: [CautionComponent, CautionDetailComponent, CautionUpdateComponent, CautionDeleteDialogComponent],
  entryComponents: [CautionDeleteDialogComponent]
})
export class BtpprojectCautionModule {}
