import {Routes} from '@angular/router'
import {SignupComponent} from './user-management/signup/signup.component'
import {LoginComponent} from './user-management/login/login.component'
import {BetManagementComponent} from './bet-management/bet-management.component'
import {isLoggedInGuard} from './guards/is-logged-in.guard'

export const routes: Routes = [
  {
    path: 'signup',
    component: SignupComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'bets',
    component: BetManagementComponent,
    canActivate: [isLoggedInGuard]
  }
]
