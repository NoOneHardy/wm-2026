import {AfterViewInit, Directive, effect, ElementRef, inject, signal} from '@angular/core'
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
export class PasswordIconDirective implements AfterViewInit {
  private _matFormField = inject(MatFormField)
  private _elRef: ElementRef<HTMLElement> = inject(ElementRef)
  private input = signal<HTMLInputElement | null>(null)

  isVisible = signal(false)

  toggleVisibility(): void {
    this.isVisible.update(v => !v)
  }

  constructor() {
    effect(() => {
      const input = this.input()
      if (!input) return
      if (this.isVisible()) {
        input.type = 'text'
        this._elRef.nativeElement.innerHTML = 'visibility_off'
      } else {
        input.type = 'password'
        this._elRef.nativeElement.innerHTML = 'visibility'
      }
    })
  }

  ngAfterViewInit(): void {
    this.input.set(this._matFormField._textField.nativeElement.querySelector('input[matInput]'))
  }
}
