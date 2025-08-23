import {inject, Injectable} from '@angular/core'
import {HttpClient, HttpErrorResponse} from '@angular/common/http'
import {catchError, map, Observable, pipe, tap, throwError} from 'rxjs'
import {BaseResponse} from './base-response'
import {HttpOptions} from './options/http-options'
import {Notification} from '../../../user-management/model/notification'
import {Store} from '@ngrx/store'
import {updateNotifications} from '../../../user-management/store/user.actions'
import {SnackbarService} from '../snackbar/snackbar.service'
import {ServiceError} from '../../../model/error'

@Injectable({
  providedIn: 'root'
})
export abstract class BaseHttpService {
  protected http = inject(HttpClient)
  private store = inject(Store)
  private snackbarService = inject(SnackbarService)

  private getDefaultPipes<T>() {
    return pipe(
      tap((r: BaseResponse<T>) => this.updateNotifications(r.globalData.notifications)),
      map((r: BaseResponse<T>) => r.data),
      catchError((err: HttpErrorResponse) => {
        this.snackbarService.addMessage({
          type: 'error',
          message: err.error?.displayMessage || 'Ein unerwarteter Fehler ist aufgetreten'
        })
        return throwError((): ServiceError => err.error)
      })
    )
  }

  protected get<T>(url: string, options?: HttpOptions): Observable<T> {
    return this.http.get<BaseResponse<T>>(url, options).pipe(this.getDefaultPipes<T>())
  }

  protected post<T, D = object>(url: string, data: D, options?: HttpOptions): Observable<T> {
    return this.http.post<BaseResponse<T>>(url, data, options).pipe(this.getDefaultPipes<T>())
  }

  protected put<T, D = object>(url: string, data: D, options?: HttpOptions): Observable<T> {
    return this.http.put<BaseResponse<T>>(url, data, options).pipe(this.getDefaultPipes<T>())
  }

  protected delete<T>(url: string, options?: HttpOptions): Observable<T> {
    return this.http.delete<BaseResponse<T>>(url, options).pipe(this.getDefaultPipes<T>())
  }

  private updateNotifications(notifications: Notification[]): void {
    this.store.dispatch(updateNotifications({notifications}))
  }
}
