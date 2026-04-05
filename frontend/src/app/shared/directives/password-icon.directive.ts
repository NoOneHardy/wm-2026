import {AfterViewInit, Directive, effect, ElementRef, inject, signal} from '@angular/core'

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
    const matFormField = this._elRef.nativeElement.closest('mat-form-field')
    this.input.set(matFormField?.querySelector('input[matInput]') ?? null)
  }
}
