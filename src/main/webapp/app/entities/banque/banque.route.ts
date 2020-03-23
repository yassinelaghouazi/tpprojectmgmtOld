import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBanque, Banque } from 'app/shared/model/banque.model';
import { BanqueService } from './banque.service';
import { BanqueComponent } from './banque.component';
import { BanqueDetailComponent } from './banque-detail.component';
import { BanqueUpdateComponent } from './banque-update.component';

@Injectable({ providedIn: 'root' })
export class BanqueResolve implements Resolve<IBanque> {
  constructor(private service: BanqueService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBanque> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((banque: HttpResponse<Banque>) => {
          if (banque.body) {
            return of(banque.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Banque());
  }
}

export const banqueRoute: Routes = [
  {
    path: '',
    component: BanqueComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'btpprojectApp.banque.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BanqueDetailComponent,
    resolve: {
      banque: BanqueResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'btpprojectApp.banque.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BanqueUpdateComponent,
    resolve: {
      banque: BanqueResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'btpprojectApp.banque.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BanqueUpdateComponent,
    resolve: {
      banque: BanqueResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'btpprojectApp.banque.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
