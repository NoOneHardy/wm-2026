import {Component, input} from '@angular/core'
import {NgOptimizedImage} from '@angular/common'
import {Team} from '../../../../model/team/team'

@Component({
  selector: 'wm-team-previous-games',
  imports: [
    NgOptimizedImage
  ],
  templateUrl: './team-previous-games.component.html',
  styleUrl: './team-previous-games.component.css'
})
export class TeamPreviousGamesComponent {
  teamSignal = input.required<Team>({alias: 'team'})
}
