import {Component, effect, inject, input, Signal} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectActiveGroup, selectIsTournamentLoading, selectIsTournamentSaving} from '../../store/tournament.feature'
import {Mode} from '../../../model/mode'
import {Group} from '../../../model/group/group'
import {saveBets} from '../../store/tournament.actions'
import {DatePipe, DecimalPipe, NgForOf, NgIf} from '@angular/common'
import {BetFormComponent} from '../game/bet-form/bet-form.component'
import {FormArray, FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms'
import {BetForm} from '../../../model/game/bet-form'
import {ButtonComponent} from '../button/button.component'
import {ActivatedRoute, RouterLink} from '@angular/router'
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
    NgForOf,
    BetFormComponent,
    ReactiveFormsModule,
    ButtonComponent,
    RouterLink,
    SpinnerComponent
  ],
  templateUrl: './group-view.component.html',
  styleUrl: './group-view.component.css'
})
export class GroupViewComponent {
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
      this.form.reset()

      this.games.forEach((game) => {
        this.form.controls.bets.push(new FormControl<BetForm>({
          game: game.id,
          joker: game.bet?.joker ?? 1,
          scoreTeamHome: game.bet?.scoreTeamHome ?? null,
          scoreTeamGuest: game.bet?.scoreTeamGuest ?? null,
        }, {nonNullable: true}), {emitEvent: false})
      })
    })
  }

  get games(): BetGame[] {
    return [...(this.group()?.games ?? [])].sort((a, b) => {
      return new Date(a.timestamp).valueOf() - new Date(b.timestamp).valueOf()
    })
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

  get backUrl(): string {
    switch (this.mode()) {
      case 'bet':
        return '/group'
      case 'admin':
        return '/admin/result'
      case 'result':
        return '/result'
    }
  }
}
