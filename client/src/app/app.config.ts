import {ApplicationConfig, provideZoneChangeDetection, provideZonelessChangeDetection} from '@angular/core';
import {provideHttpClient} from "@angular/common/http";

export const appConfig: ApplicationConfig = {
  providers: [provideZonelessChangeDetection(), provideHttpClient()]
};
