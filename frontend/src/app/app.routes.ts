import {Routes} from '@angular/router'
import {SignupComponent} from './user-management/signup/signup.component'
import {LoginComponent} from './user-management/login/login.component'
import {BetManagementComponent} from './bet-management/bet-management.component'
import {isLoggedInGuard} from './guards/is-logged-in.guard'
import {isAdminGuard} from './guards/is-admin.guard'
import {ResultManagementComponent} from './admin/result-management/result-management.component'
import {UserManagementComponent} from './admin/user-management/user-management.component'
import {LeaderboardComponent} from './leaderboard/leaderboard.component'
import {RootComponent} from './root/root.component'

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: RootComponent
  },
  {
    path: 'signup',
    component: SignupComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'leaderboard',
    component: LeaderboardComponent
  },
  {
    path: 'bets',
    component: BetManagementComponent,
    canActivate: [isLoggedInGuard]
  },
  {
    path: 'admin',
    canActivate: [isAdminGuard],
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'results',
      },
      {
        path: 'results',
        component: ResultManagementComponent
      },
      {
        path: 'users',
        component: UserManagementComponent
      }
    ]
  }
]
