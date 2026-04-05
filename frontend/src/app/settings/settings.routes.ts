import {Routes} from '@angular/router'

const routes: Routes = [
  {
    path: '',
    redirectTo: 'profile',
    pathMatch: 'full'
  },
  {
    path: 'profile',
    loadComponent: () => import('./account-settings/account-settings.component')
  },
  {
    path: 'notifications',
    loadComponent: () => import('./notification-settings/notification-settings.component')
  }
]

export default routes
