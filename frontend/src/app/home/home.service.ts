import {Injectable} from '@angular/core'
import {Observable} from 'rxjs'
import {BaseHttpService} from '../shared/services/base-http/base-http.service'
import {HomeData} from '../model/home/home-data'

@Injectable({
  providedIn: 'root'
})
export class HomeService extends BaseHttpService {
  public loadHomeData(): Observable<HomeData> {
    return this.get<HomeData>('/api/home')
  }
}
