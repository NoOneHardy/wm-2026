import { Component } from '@angular/core'
import {UserManagementPanelComponent} from '../user-management-panel/user-management-panel.component'

@Component({
  selector: 'wm-signup',
  standalone: true,
  imports: [
    UserManagementPanelComponent
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {

}
