import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMaitreOuvrage, MaitreOuvrage } from 'app/shared/model/maitre-ouvrage.model';
import { MaitreOuvrageService } from './maitre-ouvrage.service';
import { MaitreOuvrageComponent } from './maitre-ouvrage.component';
import { MaitreOuvrageDetailComponent } from './maitre-ouvrage-detail.component';
import { MaitreOuvrageUpdateComponent } from './maitre-ouvrage-update.component';

@Injectable({ providedIn: 'root' })
export class MaitreOuvrageResolve implements Resolve<IMaitreOuvrage> {
  constructor(private service: MaitreOuvrageService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMaitreOuvrage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((maitreOuvrage: HttpResponse<MaitreOuvrage>) => {
          if (maitreOuvrage.body) {
            return of(maitreOuvrage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MaitreOuvrage());
  }
}

export const maitreOuvrageRoute: Routes = [
  {
    path: '',
    component: MaitreOuvrageComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'btpprojectApp.maitreOuvrage.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MaitreOuvrageDetailComponent,
    resolve: {
      maitreOuvrage: MaitreOuvrageResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'btpprojectApp.maitreOuvrage.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MaitreOuvrageUpdateComponent,
    resolve: {
      maitreOuvrage: MaitreOuvrageResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'btpprojectApp.maitreOuvrage.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MaitreOuvrageUpdateComponent,
    resolve: {
      maitreOuvrage: MaitreOuvrageResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'btpprojectApp.maitreOuvrage.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
