import {ResultManagementComponent} from './result-management/result-management.component'
import {TeamManagementComponent} from './team-management/team-management.component'
import {GroupManagementComponent} from './group-management/group-management.component'
import {GameManagementComponent} from './game-management/game-management.component'
import {UserManagementComponent} from './user-management/user-management.component'
import {Routes} from '@angular/router'

const routes: Routes =[
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
    path: 'results/:groupId',
    component: ResultManagementComponent
  },
  {
    path: 'results/:groupId/:gameId',
    component: ResultManagementComponent
  },
  {
    path: 'teams',
    component: TeamManagementComponent
  },
  {
    path: 'groups',
    component: GroupManagementComponent
  },
  {
    path: 'games',
    component: GameManagementComponent
  },
  {
    path: 'users',
    component: UserManagementComponent
  }
]

export default routes
