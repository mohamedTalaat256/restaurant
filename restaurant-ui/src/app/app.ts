import { Component, effect, inject, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Toast } from "primeng/toast";
import { ApplicationSettingService } from "./modules/admin/settings/application-settings/application-setting.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, Toast],
  template: `<p-toast></p-toast><router-outlet></router-outlet>`
})
export class AppComponent implements OnInit{
 private applicationSettings = inject(ApplicationSettingService);

 constructor() {
   effect(() => {
      const iconUrl = this.applicationSettings.appIcon();
      const direction =  this.applicationSettings.appDirection();

      if (iconUrl) {
        this.setFavicon(iconUrl);
      }
      if (direction) {
        this.setDirection(direction);
      }
    });
 }

  ngOnInit(): void {
    this.applicationSettings.loadSetting();


  }

  private setFavicon(iconUrl: string): void {
    let link: HTMLLinkElement | null =
      document.querySelector("link[rel*='icon']");

    if (!link) {
      link = document.createElement('link');
      link.rel = 'icon';
      document.head.appendChild(link);
    }

    link.type = 'image/x-icon';
    link.href = iconUrl;
  }

  private setDirection(direction: string): void {
    document.documentElement.dir = direction.toLowerCase() === 'rtl' ? 'rtl' : 'ltr';
  }
}

/*
  constructor() {


    const translations = {}

    const languageId = 2;

    const sqlStatements = Object.entries(translations)
      .map(([key, value]) =>
        `INSERT INTO restaurant_db.language_translations ` +
        `(translation_key, translation_value, language_id) ` +
        `VALUES ('${key.replace(/'/g, "''")}', '${value.replace(/'/g, "''")}', ${languageId});`
      )
      .join('\n');

    console.log(sqlStatements);

  }
 */
