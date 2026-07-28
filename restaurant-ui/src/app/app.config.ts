import { ApplicationConfig, importProvidersFrom, inject, provideAppInitializer, provideZonelessChangeDetection } from '@angular/core';
import { routes } from './app.routes';
import Aura from '@primeuix/themes/aura';
import { providePrimeNG } from 'primeng/config';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling } from '@angular/router';
import { MessageService } from 'primeng/api';
import { errorInterceptor } from './core/interceptor/errorInterceptor.interceptor';
import { ToastModule } from 'primeng/toast';
import { tokenInterceptor } from './core/interceptor/token.interceptor';
import { registerLocaleData } from '@angular/common';
import { TranslateService } from './core/service/translate.service';
import localeEn from '@angular/common/locales/en';
import localeEnExtra from '@angular/common/locales/extra/en';
import localeAr from '@angular/common/locales/ar';
import localeArExtra from '@angular/common/locales/extra/ar';

// Register locale data for date pipe
registerLocaleData(localeEn, 'en', localeEnExtra);
registerLocaleData(localeAr, 'ar', localeArExtra);
export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptors([errorInterceptor, tokenInterceptor])),
    provideRouter(routes, withInMemoryScrolling({ anchorScrolling: 'enabled', scrollPositionRestoration: 'enabled' }), withEnabledBlockingInitialNavigation()),
    provideZonelessChangeDetection(),
    providePrimeNG({ theme: { preset: Aura, options: { darkModeSelector: '.app-dark' } } }),
    importProvidersFrom(ToastModule),
    MessageService,
    provideAppInitializer(() => inject(TranslateService).setLocale('en')),

  ]
};
