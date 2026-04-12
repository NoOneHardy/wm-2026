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

  it('should use the default button label', () => {
    expect(component.buttonLabel()).toBe('Profilbild ändern')
  })

  it('should fall back to the placeholder when defaultUrl is empty', () => {
    fixture.componentRef.setInput('defaultUrl', '')
    fixture.detectChanges()

    expect(component.defaultUrl()).toBe('/assets/user.jpg')
    expect(component.url).toBe('/assets/user.jpg')
  })

  it('should use the latest defaultUrl when no file is selected', () => {
    component.writeValue(null)
    fixture.componentRef.setInput('defaultUrl', '/assets/home.jpg')
    fixture.detectChanges()

    expect(component.url).toBe('/assets/home.jpg')
  })
})
