import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditSystemRole } from './edit-system-role';

describe('EditSystemRole', () => {
  let component: EditSystemRole;
  let fixture: ComponentFixture<EditSystemRole>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditSystemRole]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditSystemRole);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
