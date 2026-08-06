import {Component, computed, inject, ChangeDetectionStrategy} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectUser} from '../user-management/store/user.feature'
import {DashboardComponent} from '../dashboard/dashboard.component'
import {HomeComponent} from '../home/home.component'

@Component({
  selector: 'bet-root',
  imports: [
    DashboardComponent,
    HomeComponent
  ],
  templateUrl: './root.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './root.component.css'
})
export class RootComponent {
  private store = inject(Store)

  user = this.store.selectSignal(selectUser)
  isLoggedIn = computed(() => !!this.user())
}
