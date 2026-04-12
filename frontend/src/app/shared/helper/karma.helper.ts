import {GlobalData} from '../services/base-http/global-data'
import {ServiceError} from '../../model/error'
import {TestRequest} from '@angular/common/http/testing'

export function flushApiResponse<T>(req: TestRequest, data: T, globalData?: GlobalData): void {
  req.flush({
    data: data,
    globalData: globalData ?? {
      id: 'request-id',
      notifications: []
    }
  })
}

export interface ApiErrorResponseMock {
  status?: number,
  message?: string,
  displayMessage?: string
}

function mapErrorMockToServiceError(error?: ApiErrorResponseMock): ServiceError {
  return {
    status: error?.status ?? 500,
    message: error?.message ?? 'Internal Server Error',
    displayMessage: error?.displayMessage ?? 'Ein Fehler ist aufgetreten.',
    timestamp: new Date('2025-08-08T10:57:00')
  }
}

export function flushApiErrorResponse(req: TestRequest, error?: ApiErrorResponseMock): void {
  const response: ServiceError = mapErrorMockToServiceError(error)

  req.flush(response, {
    status: response.status,
    statusText: response.message
  })
}
