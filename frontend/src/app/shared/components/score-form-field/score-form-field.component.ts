import {Component, input} from '@angular/core'
import {ControlValueAccessor, FormsModule, NG_VALUE_ACCESSOR} from '@angular/forms'
import {EMPTY_METHOD, OnChangeFn, OnTouchFn} from '../../helper/control-value-accessor'
import {FormFieldComponent} from '../form-field/form-field.component'
import {NgIf} from '@angular/common'

@Component({
  selector: 'wm-score-form-field',
  imports: [
    FormFieldComponent,
    FormsModule,
    NgIf
  ],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: ScoreFormFieldComponent,
    multi: true
  }],
  templateUrl: './score-form-field.component.html',
  styleUrl: './score-form-field.component.css'
})
export class ScoreFormFieldComponent implements ControlValueAccessor {
  onChange: OnChangeFn<number | null> = EMPTY_METHOD
  onTouch: OnTouchFn = EMPTY_METHOD

  isLarge = input<boolean, boolean | ''>(false, {
    transform: (v) => v === '' || v,
    alias: 'large'
  })

  value: number | null = null
  isDisabled = false

  registerOnChange(fn: OnChangeFn<number | null>): void {
    this.onChange = fn
  }

  registerOnTouched(fn: OnTouchFn): void {
    this.onTouch = fn
  }

  setDisabledState(isDisabled: boolean): void {
    this.isDisabled = isDisabled
  }

  writeValue(value: number | null): void {
    this.value = value
    this.onChange(this.value)
  }

  increment(): void {
    if (!this.value || this.value < 999) this.writeValue((this.value ?? 0) + 1)
  }

  decrement(): void {
    if (this.value && this.value > 0) this.writeValue(this.value - 1)
  }

  onInput(value: string | number): void {
    if (value === '') return this.writeValue(null)
    this.writeValue(Number(value))
  }
}
