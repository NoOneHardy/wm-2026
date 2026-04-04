import {Directive, effect, ElementRef, inject, signal} from '@angular/core'
import {MatFormField} from '@angular/material/input'

@Directive({
  selector: '[wmPasswordIcon]',
  standalone: true,
  host: {
    '(pointerup)': 'toggleVisibility()',
    tabindex: '0',
    '[style.cursor]': '"pointer"'
  },
})
export class PasswordIconDirective {
  private matFormField = inject(MatFormField)
  private elRef: ElementRef<HTMLElement> = inject(ElementRef)
  isVisible = signal(false)

  toggleVisibility(): void {
    this.isVisible.update(v => !v)
  }

  constructor() {
    effect(() => {
      if (!this.input) return
      if (this.isVisible()) {
        this.input.type = 'text'
        this.elRef.nativeElement.innerHTML = 'visibility_off'
      } else {
        this.input.type = 'password'
        this.elRef.nativeElement.innerHTML = 'visibility'
      }
    })
  }

  get input(): HTMLInputElement | null {
    return this.matFormField._textField.nativeElement.querySelector('input[matInput]')
  }
}
