import {Component, input, output} from '@angular/core'
import {NgIf} from '@angular/common'

@Component({
  selector: 'wm-button',
  standalone: true,
  imports: [
    NgIf
  ],
  host: {
    '[style]': 'getWidth()'
  },
  templateUrl: './button.component.html',
  styleUrl: './button.component.css'
})
export class ButtonComponent {
  tabindex = input<number>(0)
  secondary = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v
  })
  disabled = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v
  })
  submit = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v
  })
  fullWidth = input<boolean, boolean | ''>(false, {
    transform: v => v === '' || v,
    alias: 'full-width'
  })
  icon = input<string | null>(null)

  clickEvent = output<void>()

  click(event: Event): void {
    event.stopPropagation()
    event.preventDefault()
    this.clickEvent.emit()
  }

  getWidth(): string {
    return this.fullWidth() ? 'width: 100%' : ''
  }
}
