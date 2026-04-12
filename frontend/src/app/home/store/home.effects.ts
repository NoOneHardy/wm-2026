import {inject, Injectable} from '@angular/core'
import {Actions, createEffect, ofType} from '@ngrx/effects'
import {catchError, exhaustMap, map, of} from 'rxjs'
import {HomeService} from '../home.service'
import {homeDataLoaded, homeDataLoadFailed, loadHomeData} from './home.actions'

// noinspection JSUnusedGlobalSymbols
@Injectable({
  providedIn: 'root'
})
export class HomeEffects {
  private actions$ = inject(Actions)
  private homeService = inject(HomeService)

  loadHomeData = createEffect(() => this.actions$.pipe(
    ofType(loadHomeData),
    exhaustMap(() => this.homeService.loadHomeData().pipe(
      map(data => homeDataLoaded({data})),
      catchError(() => of(homeDataLoadFailed()))
    ))
  ))
}
