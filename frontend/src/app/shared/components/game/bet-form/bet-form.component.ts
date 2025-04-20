import {Component, computed, effect, input} from '@angular/core'
import {BetGame} from '../../../../model/game/bet-game'
import {ControlValueAccessor, FormControl, FormGroup, NG_VALUE_ACCESSOR, ReactiveFormsModule} from '@angular/forms'
import {BetForm} from '../../../../model/game/bet-form'
import {EMPTY_METHOD, OnChangeFn, OnTouchFn} from '../../../helper/control-value-accessor'
import {DatePipe, NgForOf, NgIf, NgOptimizedImage} from '@angular/common'
import {ScoreFormFieldComponent} from '../../score-form-field/score-form-field.component'
import {toSignal} from '@angular/core/rxjs-interop'

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
  game = input.required<BetGame>()
  highlight = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v
  })
  hasStarted = computed(() => {
    return new Date(this.game().timestamp).valueOf() <= new Date().valueOf()
  })

  get isDisabled(): boolean {
    return this.formGroup.disabled
  }

  formGroup = new FormGroup({
    scoreTeamHome: new FormControl<number | null>(null),
    scoreTeamGuest: new FormControl<number | null>(null),
    joker: new FormControl<1 | 2 | 3>(1, {nonNullable: true})
  })

  valueChanges = toSignal(this.formGroup.valueChanges)

  onChange: OnChangeFn<BetForm> = EMPTY_METHOD
  onTouch: OnTouchFn = EMPTY_METHOD

  constructor() {
    effect(() => {
      const game = this.game()
      this.setDisabledState(!!game.result || this.hasStarted())
      this.writeValue({
        joker: game.bet?.joker ?? 1,
        scoreTeamHome: game.bet?.scoreTeamHome ?? null,
        scoreTeamGuest: game.bet?.scoreTeamGuest ?? null
      })
    })

    effect(() => {
      if (this.valueChanges()) {
        const bet = this.formGroup.getRawValue()
        this.onChange({
          ...bet,
          gameId: this.game().id
        })
      }
    })
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
}
