import {Component, effect, inject, OnInit, Signal} from '@angular/core'
import {MatSlideToggle} from '@angular/material/slide-toggle'
import {Store} from '@ngrx/store'
import {fetchNotificationPreferences} from '../../user-management/store/user.actions'
import {NotificationPreference} from '../../user-management/model/notification-preference'
import {selectNotificationPreferences} from '../../user-management/store/user.feature'
import {NotificationTypePipePipe} from '../../shared/pipes/notification-type.pipe'
import {FormArray, FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms'
import {NotificationChannel} from '../../user-management/model/notification-channel'
import {JsonPipe} from '@angular/common'

@Component({
  selector: 'wm-notification-settings',
  standalone: true,
  imports: [
    MatSlideToggle,
    NotificationTypePipePipe,
    ReactiveFormsModule,
    JsonPipe
  ],
  templateUrl: './notification-settings.component.html',
  styleUrl: './notification-settings.component.css'
})
export class NotificationSettingsComponent implements OnInit {
  private store = inject(Store)

  notifications: Signal<NotificationPreference[]> = this.store.selectSignal(selectNotificationPreferences)

  form = new FormGroup({
    channels: new FormArray<ChannelFormGroup>([])
  })

  ngOnInit(): void {
    this.store.dispatch(fetchNotificationPreferences())
  }

  constructor() {
    effect(() => {
      const prefs = this.notifications()
      if (prefs) {
        const channels = prefs.map(p => p.channel)
          .filter((p, i, self) => self.indexOf(p) === i)
        for (const channel of channels) {
          const channelPreferences = prefs.filter(p => p.channel === channel)
          const group = this.createFormGroup(channelPreferences)
          if (group) this.form.controls.channels.controls.push(group)
        }
      }
    })
  }

  createFormGroup(prefs: NotificationPreference[]): ChannelFormGroup | null {
    if (prefs.length === 0) return null

    const channel = prefs[0].channel
    const preferences: PreferenceFormGroup[] = prefs.map(p => new FormGroup({
      data: new FormControl<NotificationPreference>(p, {nonNullable: true}),
      enabled: new FormControl<boolean>(p.selected, {nonNullable: true})
    }))
    const isEnabled = preferences.some(p => p.controls.enabled.value)

    return new FormGroup({
      data: new FormControl<NotificationChannel>(channel, {nonNullable: true}),
      enabled: new FormControl<boolean>(isEnabled, {nonNullable: true}),
      preferences: new FormArray<PreferenceFormGroup>(preferences)
    })
  }
}

export type ChannelFormGroup = FormGroup<{
  data: FormControl<NotificationChannel>
  enabled: FormControl<boolean>
  preferences: FormArray<PreferenceFormGroup>
}>

export type PreferenceFormGroup = FormGroup<{
  data: FormControl<NotificationPreference>
  enabled: FormControl<boolean>
}>
