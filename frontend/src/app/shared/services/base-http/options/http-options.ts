import {HttpParams} from '@angular/common/http'

export interface HttpOptions {
  headers?: Record<string, string | string[]>,
  params?: HttpParams | Record<string, boolean | string | number | readonly (number | string | boolean)[]>,
}
