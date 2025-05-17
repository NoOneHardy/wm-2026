import {Component, computed, effect, inject, input} from '@angular/core'
import {BetGame} from '../../../../model/game/bet-game'
import {ControlValueAccessor, FormControl, FormGroup, NG_VALUE_ACCESSOR, ReactiveFormsModule} from '@angular/forms'
import {BetForm} from '../../../../model/game/bet-form'
import {EMPTY_METHOD, OnChangeFn, OnTouchFn} from '../../../helper/control-value-accessor'
import {DatePipe, NgForOf, NgIf, NgOptimizedImage} from '@angular/common'
import {ScoreFormFieldComponent} from '../../score-form-field/score-form-field.component'
import {toSignal} from '@angular/core/rxjs-interop'
import {Store} from '@ngrx/store'
import {selectAvailableJokers} from '../../../store/tournament.feature'
import {grantJDouble, grantJTriple, revokeJDouble, revokeJTriple} from '../../../store/tournament.actions'

@Component({
  selector: 'wm-bet-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    DatePipe,
    NgOptimizedImage,
    ScoreFormFieldComponent,
    NgForOf
  ],
  templateUrl: './bet-form.component.html',
  styleUrl: './bet-form.component.css',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: BetFormComponent
    }
  ]
})
export class BetFormComponent implements ControlValueAccessor {
  private store = inject(Store)

  game = input.required<BetGame>()
  highlight = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v
  })
  knockout = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v
  })
  admin = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v
  })
  hasStarted = computed(() => {
    return new Date(this.game().timestamp).valueOf() <= new Date().valueOf()
  })

  availableJokers = this.store.selectSignal(selectAvailableJokers)

  get isDisabled(): boolean {
    return this.formGroup.disabled
  }

  formGroup = new FormGroup({
    scoreTeamHome: new FormControl<number | null>(null),
    scoreTeamGuest: new FormControl<number | null>(null),
    joker: new FormControl<1 | 2 | 3>(1, {nonNullable: true})
  })

  private value: BetForm = {
    game: '',
    ...this.formGroup.getRawValue()
  }

  valueChanges = toSignal(this.formGroup.valueChanges)

  onChange: OnChangeFn<BetForm> = EMPTY_METHOD
  onTouch: OnTouchFn = EMPTY_METHOD

  constructor() {
    effect(() => {
      const game = this.game()
      this.setDisabledState(!this.admin() && (!!game.result || this.hasStarted()))
    })

    effect(() => {
      if (this.valueChanges()) {
        const bet = this.formGroup.getRawValue()
        const game = this.game()

        if (this.value.joker != bet.joker) {
          if (this.value.joker === 2) this.store.dispatch(grantJDouble())
          else if (this.value.joker === 3) this.store.dispatch(grantJTriple())

          if (bet.joker === 2) this.store.dispatch(revokeJDouble())
          else if (bet.joker === 3) this.store.dispatch(revokeJTriple())
        }

        this.value = {
          ...bet,
          game: game.id
        }
        this.onChange(this.value)
      }
    }, {allowSignalWrites: true})
  }

  writeValue(bet: {
    joker: 1 | 2 | 3,
    scoreTeamHome: number | null,
    scoreTeamGuest: number | null
  } | null): void {
    if (!bet) return
    // Needs to access property's explicit because of the not existing field 'gameId'
    this.formGroup.setValue({
      scoreTeamHome: bet.scoreTeamHome,
      scoreTeamGuest: bet.scoreTeamGuest,
      joker: bet.joker
    })
    this.value = {
      ...bet,
      game: this.game().id
    }
  }

  registerOnChange(fn: OnChangeFn<BetForm>): void {
    this.onChange = fn
  }

  registerOnTouched(fn: OnTouchFn): void {
    this.onTouch = fn
  }

  setDisabledState(isDisabled: boolean) {
    if (isDisabled) this.formGroup.disable()
    else this.formGroup.enable()
  }

  fillJoker(value: number): boolean {
    const selectedJoker = this.formGroup.controls.joker.value
    return value <= selectedJoker
  }

  hasJokersAvailable(joker: number): boolean {
    switch (joker) {
      case 1:
        return true
      case 2:
        return (this.availableJokers()?.jdouble ?? 0) > 0
      case 3:
        return (this.availableJokers()?.jtriple ?? 0) > 0
    }
    return false
  }
}
