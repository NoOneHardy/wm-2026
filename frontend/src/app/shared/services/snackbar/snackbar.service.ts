import {Injectable, signal} from '@angular/core'
import {SnackbarMessage} from '../../components/snackbar/model/snackbar-message'

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {
  private _messages = signal<SnackbarMessage[]>([])

  get messages() {
    return this._messages.asReadonly()
  }

  addMessage(message: SnackbarMessage) {
    this._messages.update(messages => [...messages, message])
    setTimeout(() => {
        this._messages.update(messages => messages.filter(m => m !== message))
      }, message.duration ?? 5000)
  }
}
