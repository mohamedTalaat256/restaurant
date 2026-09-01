import { inject, Injectable, signal } from "@angular/core";
import { Observable } from "rxjs";
import { ApiResponse } from "../../core/model/api-response.model";
import { HttpClient } from "@angular/common/http";
import { MessageService } from "primeng/api";
import { env } from "../../../environment/env";
import { Router } from "@angular/router";
import { TranslateService } from "../../core/service/translate.service";
import { RoleService } from "../admin/user-and-permissions/roles/role.service";


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private http = inject(HttpClient);
  private messageService = inject(MessageService);
  private translate = inject(TranslateService);
  private router = inject(Router);
  private roleService = inject(RoleService);

  readonly isLoading = signal<boolean>(false);
  readonly error = signal<string | null>(null);


  login(loginData: { email: string, password: string }) {
    this.isLoading.set(true);
    this.error.set(null);

    this.http.post<ApiResponse<any>>(`${env.apiUrl}/auth/login`, loginData).subscribe({
      next: (response: ApiResponse<any>) => {
        this.isLoading.set(false);
        if (response.status) {

          console.log(response.data);
          this.messageService.add({ severity: 'success', summary: this.translate.instant('msg_login_success'), detail: this.translate.instant(response.message) });
          localStorage.setItem('token', response.data.accessToken);
          localStorage.setItem('user', JSON.stringify(response.data.user));

          //load all role permissions and store them in local storage
          this.roleService.loadRolePermissionsByAuthUserRoles();
          this.router.navigate(['/admin']);
        } else {
          this.error.set(response.message);
          this.messageService.add({ severity: 'error', summary: this.translate.instant('msg_login_failed'), detail: this.translate.instant(response.message) });
          // this.router.navigate(['/login']);
        }
      },
      error: (error: ApiResponse<any>) => {
        this.isLoading.set(false);
        this.error.set('An error occurred during login');
        // this.router.navigate(['/login']);
      }
    }
    );
  }

  //
  logout() {
    this.http.post<ApiResponse<any>>(`${env.apiUrl}/auth/logout`, {}).subscribe({
      next: (response: ApiResponse<any>) => {
        if (response.status) {
          this.messageService.add({ severity: 'success', summary: this.translate.instant('msg_logout_success'), detail: this.translate.instant(response.message) });
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          this.router.navigate(['/login']);
        } else {
          this.messageService.add({ severity: 'error', summary: this.translate.instant('msg_logout_failed'), detail: this.translate.instant(response.message) });
        }
      },
      error: (error: ApiResponse<any>) => {
        this.messageService.add({ severity: 'error', summary: this.translate.instant('msg_logout_failed'), detail: this.translate.instant(error.message) });
      }
    }
    );


  }


}
