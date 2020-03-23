import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICaution, Caution } from 'app/shared/model/caution.model';
import { CautionService } from './caution.service';
import { CautionComponent } from './caution.component';
import { CautionDetailComponent } from './caution-detail.component';
import { CautionUpdateComponent } from './caution-update.component';

@Injectable({ providedIn: 'root' })
export class CautionResolve implements Resolve<ICaution> {
  constructor(private service: CautionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICaution> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((caution: HttpResponse<Caution>) => {
          if (caution.body) {
            return of(caution.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Caution());
  }
}

export const cautionRoute: Routes = [
  {
    path: '',
    component: CautionComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'btpprojectApp.caution.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CautionDetailComponent,
    resolve: {
      caution: CautionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'btpprojectApp.caution.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CautionUpdateComponent,
    resolve: {
      caution: CautionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'btpprojectApp.caution.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CautionUpdateComponent,
    resolve: {
      caution: CautionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'btpprojectApp.caution.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
