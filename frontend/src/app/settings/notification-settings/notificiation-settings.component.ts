import { Component } from '@angular/core'
import {MatSlideToggle} from '@angular/material/slide-toggle'

@Component({
  selector: 'wm-email-settings',
  standalone: true,
  imports: [
    MatSlideToggle
  ],
  templateUrl: './notificiation-settings.component.html',
  styleUrl: './notificiation-settings.component.css'
})
export class NotificiationSettingsComponent {

}
