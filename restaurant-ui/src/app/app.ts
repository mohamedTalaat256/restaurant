import { Component, inject, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Toast } from "primeng/toast";
import { ApplicationSettingService } from "./modules/admin/settings/application-settings/application-setting.service";
import { ProgressSpinnerModule } from 'primeng/progressspinner';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, Toast, ProgressSpinnerModule],
  template: `<p-toast></p-toast>

  @if(applicationSettings.loading()){

    <div class="d-flex justify-content-center align-items-center" style="height:100vh">
      <p-progress-spinner ariaLabel="loading" />
    </div>
  }

  <router-outlet></router-outlet>`
})
export class AppComponent implements OnInit{
  readonly applicationSettings = inject(ApplicationSettingService);

  ngOnInit(): void {
    this.applicationSettings.loadSettingPublic();
  }
}
