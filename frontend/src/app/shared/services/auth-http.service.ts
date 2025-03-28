import {inject, Injectable} from '@angular/core'
import {Store} from '@ngrx/store'
import {selectToken} from '../../user-management/store/user.feature'
import {Observable} from 'rxjs'
import {HttpClient, HttpContext, HttpHeaders, HttpParams} from '@angular/common/http'

@Injectable({
  providedIn: 'root'
})
export class AuthHttpService {
  protected http = inject(HttpClient)
  private store = inject(Store)
  private token = this.store.selectSignal(selectToken)

  get<T>(url: string, options?: HttpOptions): Observable<T> {
    return this.http.get<T>(url, this.getAuthHeader(options))
  }

  private getAuthHeader(options?: HttpOptions): HttpOptions {
    return {
      ...options,
      headers: {
        ...options?.headers,
        Authorization: `Bearer ${this.token()}`
      }
    }
  }
}

interface HttpOptions {
  headers?: HttpHeaders | Record<string, string | string[]>,
  context?: HttpContext,
  observe?: 'body',
  params?: HttpParams | Record<string, string | number | boolean | readonly (string | number | boolean)[]>,
  reportProgress?: boolean,
  withCredentials?: boolean,
  transferCache?: {
    includeHeaders?: string[]
  } | boolean
}
