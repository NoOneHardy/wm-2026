import {Component, inject, Signal} from '@angular/core'
import {AvatarUploadComponent} from '../../shared/components/avatar-upload/avatar-upload.component'
import {Store} from '@ngrx/store'
import {selectUser} from '../../user-management/store/user.feature'
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms'
import {User} from '../../model/user/user'

@Component({
  selector: 'wm-account-settings',
  standalone: true,
  imports: [
    AvatarUploadComponent,
    ReactiveFormsModule
  ],
  templateUrl: './account-settings.component.html',
  styleUrl: './account-settings.component.css'
})
export class AccountSettingsComponent {
  private store = inject(Store)

  user: Signal<User | null> = this.store.selectSignal(selectUser)

  formGroup = new FormGroup({
    avatar: new FormControl<File | null>(null)
  })

  log(): void {
    console.log(this.formGroup.value)
  }
}
