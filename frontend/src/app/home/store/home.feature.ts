import {createFeature, createReducer, on} from '@ngrx/store'
import {homeDataLoaded, homeDataLoadFailed, loadHomeData} from './home.actions'
import {HomeData} from '../../model/home/home-data'

export interface HomeState {
  isHomeLoading: boolean
  homeData: HomeData | null
}

export const initialState: HomeState = {
  isHomeLoading: false,
  homeData: null
}

export const homeFeature = createFeature({
  name: 'home',
  reducer: createReducer(
    initialState,
    on(loadHomeData, (state): HomeState => {
      return {
        ...state,
        isHomeLoading: true
      }
    }),
    on(homeDataLoaded, (state, action): HomeState => {
      return {
        ...state,
        isHomeLoading: false,
        homeData: action.data
      }
    }),
    on(homeDataLoadFailed, (state): HomeState => {
      return {
        ...state,
        isHomeLoading: false
      }
    })
  )
})

export const {
  selectIsHomeLoading,
  selectHomeData
} = homeFeature
