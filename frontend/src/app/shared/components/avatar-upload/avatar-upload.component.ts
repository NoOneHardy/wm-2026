import {Component, input} from '@angular/core'
import {NgOptimizedImage} from '@angular/common'
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms'
import {EMPTY_METHOD, OnChangeFn, OnTouchFn} from '../../helper/control-value-accessor'

@Component({
  selector: 'wm-avatar-upload',
  standalone: true,
  imports: [
    NgOptimizedImage
  ],
  templateUrl: './avatar-upload.component.html',
  styleUrl: './avatar-upload.component.css',
  providers: [
    {provide: NG_VALUE_ACCESSOR, useExisting: AvatarUploadComponent, multi: true}
  ]
})
export class AvatarUploadComponent implements ControlValueAccessor {
  defaultUrl = input<string, string | null | undefined>('/assets/user.jpg', {
    transform: (v: string | null | undefined) => v ?? '/assets/user.jpg'
  })

  private onTouch: OnTouchFn = EMPTY_METHOD
  private onChange: OnChangeFn<File | null> = EMPTY_METHOD
  protected _url: string | null = null

  onInputChange(event: Event): void {
    if (!this.isInputElement(event.target)) return
    const file: File | null = event.target.files?.[0] ?? null
    if (!file) return

    this.writeValue(file)
  }

  get url(): string {
    return this._url ?? this.defaultUrl()
  }

  set file(value: File | null) {
    if (!value) {
      this._url = this.defaultUrl()
      return
    }
    const reader = new FileReader()
    reader.readAsDataURL(value)
    reader.onload = () => {
      this._url = reader.result as string ?? this.defaultUrl()
    }
  }

  private isInputElement(target: EventTarget | null): target is HTMLInputElement {
    return target instanceof HTMLInputElement
  }

  writeValue(file: File | null): void {
    this.file = file
    this.onChange(file)
  }

  registerOnChange(fn: OnChangeFn<File | null>): void {
    this.onChange = fn
  }

  registerOnTouched(fn: OnTouchFn): void {
    this.onTouch = fn
  }

  setDisabledState(_: boolean) {
    return
  }
}
