import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core'
import {provideRouter} from '@angular/router'

import {routes} from './app.routes'
import {provideHttpClient} from '@angular/common/http'
import {provideState, provideStore} from '@ngrx/store'
import {userFeature} from './user-management/store/user.feature'
import {provideEffects} from '@ngrx/effects'
import {UserEffects} from './user-management/store/user.effects'

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(),
    provideStore(),
    provideState(userFeature),
    provideEffects(UserEffects)
  ]
}
