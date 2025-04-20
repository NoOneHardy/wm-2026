import {Component, inject, OnInit} from '@angular/core'
import {RouterOutlet} from '@angular/router'
import {HeaderComponent} from './shared/material-api'
import {
  SnackbarDisplayComponent
} from './shared/components/snackbar/components/snackbar-display/snackbar-display.component'
import {Store} from '@ngrx/store'
import {fetchUserInfo} from './user-management/store/user.actions'
import {BetFormComponent} from './shared/components/game/bet-form/bet-form.component'
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms'
import {BetForm} from './model/game/bet-form'
import {BetGame} from './model/game/bet-game'

@Component({
  selector: 'wm-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, SnackbarDisplayComponent, BetFormComponent, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  private store = inject(Store)

  mockGame: BetGame = {
    id: 'b48112fe-38d9-4ba8-b96b-bcb96c623c26',
    timestamp: new Date('2025-03-23T21:00:00'),
    teamHome: {
      id: '3237d31d-f994-4a12-8014-32548246aa2a',
      name: 'Deutschland',
      flag: 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/Flag_of_Germany.svg/180px-Flag_of_Germany.svg.png',
      createdAt: new Date('2025-04-13T17:37:13.470342'),
      updatedAt: new Date('2025-04-13T17:37:13.470342'),
      deletedAt: null
    },
    teamGuest: {
      id: '4966d2f8-029f-4ce6-96bf-43f0646d8cb0',
      name: 'Schottland',
      flag: 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Flag_of_Scotland.svg/250px-Flag_of_Scotland.svg.png',
      createdAt: new Date('2025-04-13T17:37:44.985522'),
      updatedAt: new Date('2025-04-13T17:37:44.985522'),
      deletedAt: null
    },
    result: {
      id: 'asdf-1',
      scoreTeamHome: 3,
      scoreTeamGuest: 2,
      gameId: 'b48112fe-38d9-4ba8-b96b-bcb96c623c26',
      createdAt: new Date(),
      updatedAt: new Date(),
      deletedAt: null
    },
    bet: {
      id: 'asdf-1',
      scoreTeamHome: 3,
      scoreTeamGuest: 2,
      joker: 3,
      gameId: 'b48112fe-38d9-4ba8-b96b-bcb96c623c26',
      createdAt: new Date(),
      updatedAt: new Date(),
      deletedAt: null
    },
    createdAt: new Date('2025-04-13T19:37:58.15714'),
    updatedAt: new Date('2025-04-19T21:23:00.972354'),
    deletedAt: null
  }

  formGroup = new FormGroup({
    bet: new FormControl<BetForm | null>(null)
  })

  ngOnInit(): void {
    this.store.dispatch(fetchUserInfo())
  }

  submit(): void {
    console.log(this.formGroup.getRawValue())
  }
}
