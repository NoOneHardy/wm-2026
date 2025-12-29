import {provideMockStore} from '@ngrx/store/testing'
import {ComponentFixture, TestBed} from '@angular/core/testing'
import {EmailVerificationComponent} from './email-verification.component'
import {verifyEmail} from '../user-management/store/user.actions'
import {ActivatedRoute} from '@angular/router'

describe('EmailVerificationComponent', () => {
  let component: EmailVerificationComponent
  let fixture: ComponentFixture<EmailVerificationComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmailVerificationComponent],
      providers: [provideMockStore(), {
        provide: ActivatedRoute,
        useValue: {
          snapshot: {
            queryParamMap: {
              get: () => {
                return 'test-code'
              }
            }
          }
        }
      }]
    }).compileComponents()

    fixture = TestBed.createComponent(EmailVerificationComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should read code from query params', () => {
    const spy = spyOn(component['store'], 'dispatch')
    component.ngOnInit()
    expect(spy).toHaveBeenCalledOnceWith(verifyEmail({code: 'test-code'}))
  })
})
