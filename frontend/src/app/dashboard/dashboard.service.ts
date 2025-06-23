import { HttpClient } from '@angular/common/http'
import {inject, Injectable} from '@angular/core'
import { Observable } from 'rxjs'
import {DashboardData} from '../model/dashboard/dashboard-data'

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private http = inject(HttpClient)

  loadDashboardData(): Observable<DashboardData> {
    return this.http.get<DashboardData>('/api/dashboard')
  }
}
