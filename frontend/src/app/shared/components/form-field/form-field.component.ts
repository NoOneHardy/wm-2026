import {AfterViewInit, Component, effect, ElementRef, inject, signal} from '@angular/core'
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
