import {AfterContentInit, Component, ElementRef, inject} from '@angular/core'

import {SpinnerComponent} from '../spinner/spinner.component'

@Component({
  selector: 'wm-form-field',
  imports: [
    SpinnerComponent
  ],
  templateUrl: './form-field.component.html'
})
export class FormFieldComponent implements AfterContentInit {
  private el = inject(ElementRef)
  private input: HTMLInputElement | null = null

  isPassword = false
  isPasswordVisible = false

  ngAfterContentInit() {
    this.input = this.el.nativeElement.querySelector('input')
    if (!this.input) return

    this.input.placeholder = ''
    this.isPassword = this.input.type === 'password'
  }

  togglePasswordVisibility() {
    if (this.input) {
      this.isPasswordVisible = !this.isPasswordVisible
      this.input.type = this.isPasswordVisible ? 'text' : 'password'
    }
  }
}
