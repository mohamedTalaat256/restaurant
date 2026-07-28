import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormInputValidation } from './form-input-validation';

describe('FormInputValidation', () => {
  let component: FormInputValidation;
  let fixture: ComponentFixture<FormInputValidation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormInputValidation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormInputValidation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
