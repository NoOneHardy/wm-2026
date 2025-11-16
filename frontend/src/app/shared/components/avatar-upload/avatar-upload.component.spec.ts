import {ComponentFixture, TestBed} from '@angular/core/testing'

import {AvatarUploadComponent} from './avatar-upload.component'

describe('AvatarUploadComponent', () => {
  let component: AvatarUploadComponent
  let fixture: ComponentFixture<AvatarUploadComponent>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvatarUploadComponent]
    }).compileComponents()

    fixture = TestBed.createComponent(AvatarUploadComponent)
    component = fixture.componentInstance
    fixture.detectChanges()
  })

  it('should create', () => {
    expect(component).toBeTruthy()
  })

  it('should have default url', () => {
    expect(component.url).toBe('/assets/user.jpg')
  })

  it('should update URL on file input', () => {
    const file = new File([''], 'avatar.png', { type: 'image/png' })
    component.writeValue(file)
    setTimeout(() => {
      expect(component.url).not.toBe('/assets/user.jpg')
      expect(component.url).toContain('data:image/png;base64,')
    }, 100)
  })
})
