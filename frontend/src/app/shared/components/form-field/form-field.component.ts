import {AfterViewInit, Component, ElementRef, inject} from '@angular/core'

@Component({
  selector: 'wm-form-field',
  standalone: true,
  imports: [],
  templateUrl: './form-field.component.html',
  styleUrl: './form-field.component.css'
})
export class FormFieldComponent implements AfterViewInit {
  private el = inject(ElementRef)

  ngAfterViewInit() {
    const input: HTMLInputElement | null = this.el.nativeElement.querySelector('input')

    if (input) {
      input.placeholder = ''
    }
  }
}
