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
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async'

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(),
    provideStore(),
    provideState(userFeature),
    provideState(tournamentFeature),
    provideState(adminFeature),
    provideEffects(UserEffects, TournamentEffects, AdminEffects), provideAnimationsAsync(),
  ]
}
