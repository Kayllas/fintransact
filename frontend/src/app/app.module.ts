import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { PixAreaComponent } from './pages/pix/pix-area.component';
import { PixKeysComponent } from './pages/pix/pix-keys/pix-keys.component';
import { PixTransferComponent } from './pages/pix/pix-transfer/pix-transfer.component';
import { LandingComponent } from './pages/landing/landing.component';

// PrimeNG Imports
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { authInterceptorProviders } from './helpers/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    RegisterComponent,
    DashboardComponent,
    DashboardComponent,
    SettingsComponent,
    PixAreaComponent,
    PixKeysComponent,
    PixTransferComponent,
    LandingComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    InputTextModule,
    ButtonModule,
    CardModule,
    ToastModule,
    TableModule,
    DialogModule
  ],
  providers: [MessageService, authInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
