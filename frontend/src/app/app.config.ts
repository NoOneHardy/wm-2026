import {ApplicationConfig, LOCALE_ID, provideZoneChangeDetection} from '@angular/core'
import {provideRouter} from '@angular/router'
import {de} from 'date-fns/locale/de'
import localeDeCh from '@angular/common/locales/de-CH'
import {routes} from './app.routes'
import {provideHttpClient, withXhr} from '@angular/common/http'
import {provideState, provideStore} from '@ngrx/store'
import {userFeature} from './user-management/store/user.feature'
import {provideEffects} from '@ngrx/effects'
import {UserEffects} from './user-management/store/user.effects'
import {TournamentEffects} from './shared/store/tournament.effects'
import {tournamentFeature} from './shared/store/tournament.feature'
import {AdminEffects} from './admin/store/admin.effects'
import {adminFeature} from './admin/store/admin.feature'
import {homeFeature} from './home/store/home.feature'
import {HomeEffects} from './home/store/home.effects'
import {MAT_DATE_LOCALE} from '@angular/material/core'
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async'
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from '@angular/material/form-field'
import {registerLocaleData} from '@angular/common'
import {provideDateFnsAdapter} from '@angular/material-date-fns-adapter'
import {MAT_ICON_DEFAULT_OPTIONS} from '@angular/material/icon'
import {MAT_BUTTON_TOGGLE_DEFAULT_OPTIONS} from '@angular/material/button-toggle'

registerLocaleData(localeDeCh, 'de-CH')
registerLocaleData(de, 'de')

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(withXhr()),
    provideStore(),
    provideState(userFeature),
    provideState(tournamentFeature),
    provideState(homeFeature),
    provideState(adminFeature),
    provideDateFnsAdapter(),
    provideEffects(UserEffects, TournamentEffects, HomeEffects, AdminEffects),
    provideAnimationsAsync(),
    {
      provide: LOCALE_ID,
      useValue: 'de-CH',
    },
    {
      provide: MAT_DATE_LOCALE,
      useValue: de
    },
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: {
        subscriptSizing: 'dynamic'
      }
    },
    {
      provide: MAT_ICON_DEFAULT_OPTIONS,
      useValue: {
        fontSet: 'material-symbols-rounded'
      }
    },
    {
      provide: MAT_BUTTON_TOGGLE_DEFAULT_OPTIONS,
      useValue: {
        hideMultipleSelectionIndicator: true,
        hideSingleSelectionIndicator: true
      }
    }
  ]
}
