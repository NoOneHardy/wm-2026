import {Component, computed, inject} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectUser} from '../user-management/store/user.feature'
import {DashboardComponent} from '../dashboard/dashboard.component'
import {HomeComponent} from '../home/home.component'

@Component({
  selector: 'wm-root',
  imports: [
    DashboardComponent,
    HomeComponent
  ],
  templateUrl: './root.component.html',
  styleUrl: './root.component.css'
})
export class RootComponent {
  private store = inject(Store)

  user = this.store.selectSignal(selectUser)
  isLoggedIn = computed(() => !!this.user())
}
