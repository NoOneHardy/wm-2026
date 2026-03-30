import {createAction, props} from '@ngrx/store'
import {HomeData} from '../../model/home/home-data'

export const loadHomeData = createAction('[Home] Load data')
export const homeDataLoaded = createAction('[Home] Data loaded', props<{
  data: HomeData
}>())
export const homeDataLoadFailed = createAction('[Home] Data load failed')
