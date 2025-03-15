import {Component, input, output} from '@angular/core'

@Component({
  selector: 'wm-button',
  standalone: true,
  imports: [],
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

  clickEvent = output<void>()

  click(event: Event): void {
    event.stopPropagation()
    event.preventDefault()
    this.clickEvent.emit()
  }
}
