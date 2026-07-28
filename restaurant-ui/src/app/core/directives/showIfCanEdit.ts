import {
  Directive,
  ElementRef,
  inject,
  Input,
  OnChanges,
  Renderer2,
  SimpleChanges
} from '@angular/core';

interface RolePermission {
  id: number;
  roleId: number;
  menuItemId: number;
  canRead: boolean;
  canCreate: boolean;
  canEdit: boolean;
  canDelete: boolean;
}

@Directive({
  selector: '[showIfCanEdit]'
})
export class ShowIfCanEditDirective implements OnChanges {

  @Input() showIfCanEdit!: number;

  private el = inject(ElementRef);
  private renderer = inject(Renderer2);

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['showIfCanEdit']) {
      this.updateVisibility();
    }
  }

  private updateVisibility(): void {
    const menuId = this.showIfCanEdit;

    const permissionsJson = localStorage.getItem('authUserRolePermissions');

    if (!permissionsJson) {
      this.hide();
      return;
    }

    const permissions: RolePermission[] = JSON.parse(permissionsJson);

    const permission = permissions.find(
      p => p.menuItemId === menuId
    );

    if (permission?.canEdit) {
      this.show();
    } else {
      this.hide();
    }
  }

  private show(): void {
    this.renderer.setStyle(this.el.nativeElement, 'display', '');
  }

  private hide(): void {
    this.renderer.setStyle(this.el.nativeElement, 'display', 'none');
  }
}
