import {Component, computed, inject} from '@angular/core'
import {AppearanceService, AppearanceTheme} from '../../shared/services/appearance/appearance.service'

type ThemeOption = {
  theme: AppearanceTheme
  title: string
  subtitle: string
  description: string
  kickoff: string
  icon: string
}

@Component({
  selector: 'wm-appearance-settings',
  standalone: true,
  imports: [],
  templateUrl: './appearance-settings.component.html',
  styleUrl: './appearance-settings.component.css'
})
export class AppearanceSettingsComponent {
  private appearanceService = inject(AppearanceService)

  readonly theme = this.appearanceService.theme

  readonly options: readonly ThemeOption[] = [
    {
      theme: 'light',
      title: 'Tagspiel',
      subtitle: 'Klares Tageslicht und ein offenes Stadionbild.',
      description: 'Ideal fuer lange Tipp-Sessions mit maximaler Uebersicht und einer hellen Turnieroberflaeche.',
      kickoff: '15:00',
      icon: 'light_mode'
    },
    {
      theme: 'dark',
      title: 'Flutlichtspiel',
      subtitle: 'Abendstimmung mit leuchtenden Kontrasten.',
      description: 'Perfekt fuer spaete Spieltage mit dunklem Untergrund, satten Farben und Stadionatmosphaere.',
      kickoff: '21:00',
      icon: 'nights_stay'
    }
  ]

  readonly selectedOption = computed(() => this.options.find(option => option.theme === this.theme()) ?? this.options[0])

  selectTheme(theme: AppearanceTheme): void {
    this.appearanceService.setTheme(theme)
  }
}
