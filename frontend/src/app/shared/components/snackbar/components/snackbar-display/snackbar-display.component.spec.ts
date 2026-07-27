import {ComponentFixture, TestBed} from '@angular/core/testing'

import {SnackbarDisplayComponent} from './snackbar-display.component'
import {provideMockStore} from '@ngrx/store/testing'
import {SnackbarService} from '../../../../services/snackbar/snackbar.service'

describe('SnackbarDisplayComponent', () => {
  let component: SnackbarDisplayComponent
  let fixture: ComponentFixture<SnackbarDisplayComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SnackbarDisplayComponent],
      providers: [provideMockStore()]
    }).compileComponents()

    fixture = TestBed.createComponent(SnackbarDisplayComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should take signal from snackbar service', () => {
    const service = TestBed.inject(SnackbarService)
    expect(component.messages).toEqual(service.messages)
    expect(component.messages()).toEqual([])

    service.addMessage({message: 'test'})
    fixture.detectChanges()
    expect(component.messages()).toEqual([{message: 'test'}])
  })

  it('should display message', () => {
    const service = TestBed.inject(SnackbarService)
    service.addMessage({message: 'test'})
    fixture.detectChanges()

    const snackbar = fixture.nativeElement.querySelector('bet-snackbar')
    expect(snackbar).toBeTruthy()
    expect(snackbar.innerText).toContain('test')
  })

  it('should display multiple messages', () => {
    const service = TestBed.inject(SnackbarService)
    service.addMessage({message: 'test'})
    fixture.detectChanges()

    const snackbar = fixture.nativeElement.querySelector('bet-snackbar')
    expect(snackbar).toBeTruthy()
    expect(snackbar.innerText).toContain('test')

    service.addMessage({message: 'test2'})
    fixture.detectChanges()

    const snackbars = fixture.nativeElement.querySelectorAll('bet-snackbar')
    expect(snackbars.length).toEqual(2)
    expect(snackbars[1].innerText).toContain('test2')
  })
})
