import {Routes} from '@angular/router'
import {SignupComponent} from './user-management/signup/signup.component'
import {LoginComponent} from './user-management/login/login.component'
import {isLoggedInGuard} from './guards/is-logged-in.guard'
import {isAdminGuard} from './guards/is-admin.guard'
import {LeaderboardComponent} from './leaderboard/leaderboard.component'
import {RootComponent} from './root/root.component'
import {isLoggedOutGuard} from './guards/is-logged-out.guard'

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: RootComponent
  },
  {
    path: 'signup',
    component: SignupComponent,
    canActivate: [isLoggedOutGuard]
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [isLoggedOutGuard]
  },
  {
    path: 'leaderboard',
    component: LeaderboardComponent
  },
  {
    path: 'bets',
    loadComponent: () => import('./bet-management/bet-management.component'),
    canActivate: [isLoggedInGuard]
  },
  {
    path: 'bets/:groupId',
    loadComponent: () => import('./bet-management/bet-management.component'),
    canActivate: [isLoggedInGuard]
  },
  {
    path: 'bets/:groupId/:gameId',
    loadComponent: () => import('./bet-management/bet-management.component'),
    canActivate: [isLoggedInGuard]
  },
  {
    path: 'admin',
    canActivate: [isAdminGuard],
    loadChildren: () => import('./admin/admin.routes')
  },
  {
    path: 'settings',
    canActivate: [isLoggedInGuard],
    loadComponent: () => import('./settings/settings.component'),
    loadChildren: () => import('./settings/settings.routes')
  },
  {
    path: 'verify-email',
    loadComponent: () => import('./verification/email-verification/email-verification.component')
  },
  {
    path: 'password-reset',
    loadComponent: () => import('./verification/password-reset/password-reset.component').then(m => m.PasswordResetComponent)
  },
  {
    path: 'forgot-password',
    loadComponent: () => import('./verification/request-password-reset/request-password-reset.component').then(m => m.RequestPasswordResetComponent)
  }
]
