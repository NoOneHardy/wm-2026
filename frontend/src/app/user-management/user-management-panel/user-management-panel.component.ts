import {Component, input, ChangeDetectionStrategy} from '@angular/core'

@Component({
  selector: 'bet-user-management-panel',
  imports: [],
  templateUrl: './user-management-panel.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './user-management-panel.component.css'
})
export class UserManagementPanelComponent {
  heading = input<string | null>(null)
}
