import {Component, effect, inject, input, OnDestroy, Signal} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectActiveGroup, selectIsTournamentLoading, selectIsTournamentSaving} from '../../store/tournament.feature'
import {Mode} from '../../../model/mode'
import {Group} from '../../../model/group/group'
import {deselectGroup, saveBets} from '../../store/tournament.actions'
import {DatePipe, DecimalPipe, NgIf} from '@angular/common'
import {BetFormComponent} from '../game/bet-form/bet-form.component'
import {FormArray, FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms'
import {BetForm} from '../../../model/game/bet-form'
import {ButtonComponent} from '../button/button.component'
import {ActivatedRoute} from '@angular/router'
import {SpinnerComponent} from '../spinner/spinner.component'
import {BetGame} from '../../../model/game/bet-game'
import {toSignal} from '@angular/core/rxjs-interop'
import {map} from 'rxjs'

@Component({
  selector: 'wm-group-view',
  standalone: true,
  imports: [
    DecimalPipe,
    NgIf,
    DatePipe,
    BetFormComponent,
    ReactiveFormsModule,
    ButtonComponent,
    SpinnerComponent
  ],
  templateUrl: './group-view.component.html',
  styleUrl: './group-view.component.css'
})
export class GroupViewComponent implements OnDestroy {
  private store = inject(Store)
  private activatedRoute = inject(ActivatedRoute)

  group: Signal<Group | null> = this.store.selectSignal(selectActiveGroup)
  isLoading = this.store.selectSignal(selectIsTournamentLoading)
  isSaving = this.store.selectSignal(selectIsTournamentSaving)

  mode = input<Mode>('bet', {alias: 'mode'})
  highlight = toSignal(this.activatedRoute.queryParamMap.pipe(
    map(params => params.get('g'))
  ), {initialValue: null})

  form = new FormGroup({
    bets: new FormArray<FormControl<BetForm>>([])
  })

  constructor() {
    effect(() => {
      this.games.forEach((game, i) => {
        const defaultValue: BetForm = {
          game: game.id,
          joker: game.bet?.joker ?? 1,
          scoreTeamHome: game.bet?.scoreTeamHome ?? null,
          scoreTeamGuest: game.bet?.scoreTeamGuest ?? null
        }

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

  get games(): BetGame[] {
    return [...(this.group()?.games ?? [])].sort((a, b) => {
      return new Date(a.timestamp).valueOf() - new Date(b.timestamp).valueOf()
    })
  }

  findGame(id: string): BetGame | undefined {
    return this.games.find((game) => game.id === id)
  }

  save() {
    const group = this.group()
    if (!group) return

    const bets = this.form.getRawValue().bets
    this.store.dispatch(saveBets({
      groupId: group.id,
      bets: bets.filter((bet) => bet.scoreTeamHome !== null && bet.scoreTeamGuest !== null)
    }))
  }

  ngOnDestroy(): void {
    this.back()
  }

  back(): void {
    this.store.dispatch(deselectGroup())
  }
}
