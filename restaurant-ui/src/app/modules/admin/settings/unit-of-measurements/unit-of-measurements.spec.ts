import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnitOfMeasurements } from './unit-of-measurements';

describe('UnitOfMeasurements', () => {
  let component: UnitOfMeasurements;
  let fixture: ComponentFixture<UnitOfMeasurements>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UnitOfMeasurements]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UnitOfMeasurements);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
