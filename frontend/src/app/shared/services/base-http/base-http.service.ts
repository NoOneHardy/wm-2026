import {inject, Injectable} from '@angular/core'
import {HttpClient} from '@angular/common/http'
import {map, Observable} from 'rxjs'
import {BaseResponse} from './base-response'
import {HttpOptions} from './options/http-options'

@Injectable({
  providedIn: 'root'
})
export abstract class BaseHttpService {
  protected http = inject(HttpClient)

  protected get<T>(url: string, options?: HttpOptions): Observable<T> {
    return this.http.get<BaseResponse<T>>(url, options).pipe(
      map(r => r.data)
    )
  }

  protected post<T, D = object>(url: string, data: D, options?: HttpOptions): Observable<T> {
    return this.http.post<BaseResponse<T>>(url, data, options).pipe(
      map(r => r.data)
    )
  }

  protected put<T, D = object>(url: string, data: D, options?: HttpOptions): Observable<T> {
    return this.http.put<BaseResponse<T>>(url, data, options).pipe(
      map(r => r.data)
    )
  }
}
