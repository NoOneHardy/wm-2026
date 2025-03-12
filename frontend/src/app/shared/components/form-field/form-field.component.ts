import {AfterViewInit, Component, ElementRef, inject} from '@angular/core'
import {NgIf} from '@angular/common'

@Component({
  selector: 'wm-form-field',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './form-field.component.html',
  styleUrl: './form-field.component.css'
})
export class FormFieldComponent implements AfterViewInit {
  private el = inject(ElementRef)
  private input: HTMLInputElement | null = null

  isPassword = false
  isPasswordVisible = false

  ngAfterViewInit() {
    this.input = this.el.nativeElement.querySelector('input')

    if (this.input) {
      this.input.placeholder = ''
      this.isPassword = this.input.type === 'password'
    }
  }

  togglePasswordVisibility() {
    if (this.input) {
      this.isPasswordVisible = !this.isPasswordVisible
      this.input.type = this.isPasswordVisible ? 'text' : 'password'
    }
  }
}
