import {Component, input, output} from '@angular/core'
import {RouterLink} from '@angular/router'

@Component({
  selector: 'wm-user-button',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './user-button.component.html',
  styleUrl: './user-button.component.css'
})
export class UserButtonComponent {
  secondary = input<boolean, boolean | ''>(false, {
    transform: (value) => value === '' || value
  })
  route = input<string | null>(null)
  content = input<string>('')
  clickButton = output<void>()
}
