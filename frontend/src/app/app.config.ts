import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core'
import {provideRouter} from '@angular/router'

import {routes} from './app.routes'
import {provideHttpClient} from '@angular/common/http'
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
import {MAT_DATE_LOCALE, provideNativeDateAdapter} from '@angular/material/core'
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async'
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from '@angular/material/form-field'

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(),
    provideStore(),
    provideState(userFeature),
    provideState(tournamentFeature),
    provideState(homeFeature),
    provideState(adminFeature),
    provideNativeDateAdapter(),
    provideEffects(UserEffects, TournamentEffects, HomeEffects, AdminEffects),
    provideAnimationsAsync(),
    {
      provide: MAT_DATE_LOCALE,
      useValue: 'de-CH'
    },
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: {
        subscriptSizing: 'dynamic'
      }
    }
  ]
}
