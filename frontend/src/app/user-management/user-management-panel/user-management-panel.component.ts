import {Component, input} from '@angular/core'

@Component({
  selector: 'wm-user-management-panel',
  standalone: true,
  imports: [],
  templateUrl: './user-management-panel.component.html',
  styleUrl: './user-management-panel.component.css'
})
export class UserManagementPanelComponent {
  title = input<string | null>(null)
}
