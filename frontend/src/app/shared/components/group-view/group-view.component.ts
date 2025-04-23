import {Component, effect, inject, input, OnInit, Signal} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectActiveGroup, selectIsTournamentLoading, selectIsTournamentSaving} from '../../store/tournament.feature'
import {Mode} from '../../../model/mode'
import {Group} from '../../../model/group/group'
import {saveBets, selectGroup} from '../../store/tournament.actions'
import {DatePipe, DecimalPipe, NgForOf, NgIf} from '@angular/common'
import {BetFormComponent} from '../game/bet-form/bet-form.component'
import {FormArray, FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms'
import {BetForm} from '../../../model/game/bet-form'
import {ButtonComponent} from '../button/button.component'
import {RouterLink} from '@angular/router'
import {SpinnerComponent} from '../spinner/spinner.component'

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
export class GroupViewComponent implements OnInit {
  private store = inject(Store)

  group: Signal<Group | null> = this.store.selectSignal(selectActiveGroup)
  isLoading = this.store.selectSignal(selectIsTournamentLoading)
  isSaving = this.store.selectSignal(selectIsTournamentSaving)

  mode = input<Mode>('bet', {alias: 'mode'})

  form = new FormGroup({
    bets: new FormArray<FormControl<BetForm>>([])
  })

  constructor() {
    effect(() => {
      const games = this.group()?.games ?? []

      this.form.reset()

      games.forEach((game) => {
        this.form.controls.bets.push(new FormControl<BetForm>({
          game: game.id,
          joker: game.bet?.joker ?? 1,
          scoreTeamHome: game.bet?.scoreTeamHome ?? null,
          scoreTeamGuest: game.bet?.scoreTeamGuest ?? null,
        }, {nonNullable: true}), {emitEvent: false})
      })
    })
  }

  ngOnInit() {
    // TODO: remove
    this.store.dispatch(selectGroup({groupId: 'ab774ee8-c95e-48d2-808e-8962df3cd132'}))
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
