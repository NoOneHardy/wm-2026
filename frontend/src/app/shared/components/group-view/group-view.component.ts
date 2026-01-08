import {Component, effect, inject, input, Signal} from '@angular/core'
import {Store} from '@ngrx/store'
import {
  selectActiveGroup,
  selectAvailableJokers,
  selectIsTournamentLoading,
  selectIsTournamentSaving
} from '../../store/tournament.feature'
import {Mode} from '../../../model/mode'
import {Group} from '../../../model/group/group'
import {saveBets, saveResults} from '../../store/tournament.actions'
import {DatePipe, DecimalPipe} from '@angular/common'
import {BetFormComponent} from '../game/bet-form/bet-form.component'
import {FormArray, FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms'
import {BetForm} from '../../../model/game/bet-form'
import {ButtonComponent} from '../button/button.component'
import {Router} from '@angular/router'
import {SpinnerComponent} from '../spinner/spinner.component'
import {BetGame} from '../../../model/game/bet-game'
import {ScoreForm} from '../../../model/game/score-form'
import {RemainingJokerDisplayComponent} from '../remaining-joker-display/remaining-joker-display.component'

@Component({
  selector: 'wm-group-view',
  standalone: true,
  imports: [
    DecimalPipe,
    DatePipe,
    BetFormComponent,
    ReactiveFormsModule,
    ButtonComponent,
    SpinnerComponent,
    RemainingJokerDisplayComponent
  ],
  templateUrl: './group-view.component.html',
  styleUrl: './group-view.component.css'
})
export class GroupViewComponent {
  private store = inject(Store)
  private router = inject(Router)

  jokers = this.store.selectSignal(selectAvailableJokers)
  group: Signal<Group | null> = this.store.selectSignal(selectActiveGroup)
  isLoading = this.store.selectSignal(selectIsTournamentLoading)
  isSaving = this.store.selectSignal(selectIsTournamentSaving)

  mode = input<Mode>('bet', {alias: 'mode'})
  highlight = input<string | null>(null)

  form = new FormGroup({
    bets: new FormArray<FormControl<BetForm>>([])
  })

  constructor() {
    effect(() => {
      this.games.forEach((game, i) => {
        const defaultValue: BetForm = this.getDefaultValues(game)

        const control = this.form.controls.bets.at(i)
        if (control) control.setValue(defaultValue)
        else this.form.controls.bets.push(
          new FormControl<BetForm>(defaultValue, {nonNullable: true})
        )
      })

      // Remove excess controls
      while (this.form.controls.bets.length > this.games.length) {
        this.form.controls.bets.removeAt(this.form.controls.bets.length - 1)
      }
    })
  }

  private getDefaultValues(game: BetGame): BetForm {
    if (this.mode() === 'bet') return {
      game: game.id,
      joker: game.bet?.joker ?? 1,
      scoreTeamHome: game.bet?.scoreTeamHome ?? null,
      scoreTeamGuest: game.bet?.scoreTeamGuest ?? null
    }
    return {
      game: game.id,
      joker: 1,
      scoreTeamHome: game.result?.scoreTeamHome ?? null,
      scoreTeamGuest: game.result?.scoreTeamGuest ?? null
    }
  }

  get games(): BetGame[] {
    return [...(this.group()?.games ?? [])].sort((a, b) => {
      return new Date(a.timestamp).valueOf() - new Date(b.timestamp).valueOf()
    })
  }

  findGame(id: string): BetGame | undefined {
    return this.games.find((game) => game.id === id)
  }

  save(): void {
    const group = this.group()
    if (!group) return

    const bets = (this.form.controls.bets.controls ?? [])
      .filter(c => c.enabled)
      .map(c => c.getRawValue())
    if (this.mode() === 'bet') this.store.dispatch(saveBets({
      groupId: group.id,
      bets: bets
        .map((bet) => {
          return {
            ...bet,
            scoreTeamGuest: bet.scoreTeamGuest,
            scoreTeamHome: bet.scoreTeamHome
          }
        })
    }))
    else this.store.dispatch(saveResults({
      groupId: group.id,
      results: bets
        .map((bet): ScoreForm => {
          return {
            game: bet.game,
            scoreTeamGuest: bet.scoreTeamGuest,
            scoreTeamHome: bet.scoreTeamHome
          }
        })
    }))
  }

  back(): void {
    this.router.navigate([this.mode() === 'admin' ? '/admin/results' : 'bets']).then()
  }
}
