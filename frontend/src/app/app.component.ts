import {Component, inject, OnInit} from '@angular/core'
import {RouterOutlet} from '@angular/router'
import {HeaderComponent} from './shared/material-api'
import {
  SnackbarDisplayComponent
} from './shared/components/snackbar/components/snackbar-display/snackbar-display.component'
import {Store} from '@ngrx/store'
import {fetchUserInfo} from './user-management/store/user.actions'
import {OverviewComponent} from './shared/components/overview/overview.component'
import {CardGroup} from './model/group/card-group'

@Component({
  selector: 'wm-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, SnackbarDisplayComponent, OverviewComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  private store = inject(Store)

  ngOnInit(): void {
    this.store.dispatch(fetchUserInfo())
  }

  groups: CardGroup[] = [
    {
      'id': '0a217c3a-6610-461f-8e4d-c87da62317d4',
      'name': 'Gruppe A',
      'percentage': 50.0,
      'percentageResult': 100.0,
      'thumbnail': [
        'https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Flag_of_Switzerland.svg/250px-Flag_of_Switzerland.svg.png',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Flag_of_Sweden.svg/250px-Flag_of_Sweden.svg.png',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/Flag_of_Germany.svg/180px-Flag_of_Germany.svg.png',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Flag_of_France.svg/250px-Flag_of_France.svg.png'
      ],
      'isKnockout': false,
      'createdAt': new Date('2025-04-11T14:17:58.533322'),
      'updatedAt': new Date('2025-04-11T14:17:58.533322'),
      'deletedAt': null
    },
    {
      'id': '0a217c3a-6610-461f-8e4d-c87da62317d4',
      'name': 'Gruppe B',
      'percentage': 25.0,
      'percentageResult': 100.0,
      'thumbnail': [
        'https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Flag_of_Switzerland.svg/250px-Flag_of_Switzerland.svg.png',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Flag_of_Sweden.svg/250px-Flag_of_Sweden.svg.png',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/b/ba/Flag_of_Germany.svg/180px-Flag_of_Germany.svg.png',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Flag_of_France.svg/250px-Flag_of_France.svg.png'
      ],
      'isKnockout': false,
      'createdAt': new Date('2025-04-11T14:17:58.533322'),
      'updatedAt': new Date('2025-04-11T14:17:58.533322'),
      'deletedAt': null
    },
    {
      'id': '0a217c3a-6610-461f-8e4d-c87da62317d4',
      'name': 'Achtelfinale',
      'percentage': 50.0,
      'percentageResult': 100.0,
      'thumbnail': [],
      'isKnockout': true,
      'createdAt': new Date('2025-04-11T14:17:58.533322'),
      'updatedAt': new Date('2025-04-11T14:17:58.533322'),
      'deletedAt': null
    }
  ]
}
