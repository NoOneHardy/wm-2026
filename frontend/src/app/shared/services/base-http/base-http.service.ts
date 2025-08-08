import {inject, Injectable} from '@angular/core'
import {HttpClient, HttpErrorResponse} from '@angular/common/http'
import {catchError, map, Observable, tap, throwError} from 'rxjs'
import {BaseResponse} from './base-response'
import {HttpOptions} from './options/http-options'
import {Notification} from '../../../user-management/model/notification'
import {Store} from '@ngrx/store'
import {updateNotifications} from '../../../user-management/store/user.actions'

@Injectable({
  providedIn: 'root'
})
export abstract class BaseHttpService {
  protected http = inject(HttpClient)
  private store = inject(Store)

  protected get<T>(url: string, options?: HttpOptions): Observable<T> {
    return this.http.get<BaseResponse<T>>(url, options).pipe(
      tap(r => this.updateNotifications(r.globalData.notifications)),
      map(r => r.data),
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err.error)
      })
    )
  }

  protected post<T, D = object>(url: string, data: D, options?: HttpOptions): Observable<T> {
    return this.http.post<BaseResponse<T>>(url, data, options).pipe(
      tap(r => this.updateNotifications(r.globalData.notifications)),
      map(r => r.data),
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err.error)
      })
    )
  }

  protected put<T, D = object>(url: string, data: D, options?: HttpOptions): Observable<T> {
    return this.http.put<BaseResponse<T>>(url, data, options).pipe(
      tap(r => this.updateNotifications(r.globalData.notifications)),
      map(r => r.data),
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err.error)
      })
    )
  }

  protected delete<T>(url: string, options?: HttpOptions): Observable<T> {
    return this.http.delete<BaseResponse<T>>(url, options).pipe(
      tap(r => this.updateNotifications(r.globalData.notifications)),
      map(r => r.data),
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err.error)
      })
    )
  }

  private updateNotifications(notifications: Notification[]): void {
    this.store.dispatch(updateNotifications({notifications}))
  }
}
