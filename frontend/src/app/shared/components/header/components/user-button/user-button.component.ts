import {Component, input, output, ChangeDetectionStrategy} from '@angular/core'
import {RouterLink} from '@angular/router'
import {MatRipple} from '@angular/material/core'

@Component({
  selector: 'bet-user-button',
  imports: [
    RouterLink,
    MatRipple
  ],
  templateUrl: './user-button.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
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
