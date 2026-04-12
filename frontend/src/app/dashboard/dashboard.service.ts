import {Injectable} from '@angular/core'
import {Observable} from 'rxjs'
import {DashboardData} from '../model/dashboard/dashboard-data'
import {BaseHttpService} from '../shared/services/base-http/base-http.service'

@Injectable({
  providedIn: 'root'
})
export class DashboardService extends BaseHttpService {
  loadDashboardData(): Observable<DashboardData> {
    return this.get<DashboardData>('/api/dashboard')
  }
}
