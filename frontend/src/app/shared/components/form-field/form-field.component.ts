import {AfterViewInit, Component, effect, ElementRef, inject, signal} from '@angular/core'

import {SpinnerComponent} from '../spinner/spinner.component'

@Component({
  selector: 'wm-form-field',
  imports: [
    SpinnerComponent
  ],
  templateUrl: './form-field.component.html'
})
export class FormFieldComponent implements AfterViewInit {
  private el = inject(ElementRef)
  private input = signal<HTMLInputElement | null>(null)

  isPassword = false
  isPasswordVisible = false

  constructor() {
    effect(() => {
      const input = this.input()
      if (input) {
        input.placeholder = ''
        this.isPassword = input.type === 'password'
      }
    })
  }

  ngAfterViewInit() {
    this.input.set(this.el.nativeElement.querySelector('input'))
  }

  togglePasswordVisibility() {
    const input = this.input()
    if (input) {
      this.isPasswordVisible = !this.isPasswordVisible
      input.type = this.isPasswordVisible ? 'text' : 'password'
    }
  }
}
