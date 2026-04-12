import {GlobalData} from './global-data'

export interface BaseResponse<T> {
  data: T
  globalData: GlobalData
}
