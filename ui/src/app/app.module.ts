import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule} from "@angular/forms";
import { AppComponent } from './app.component';
import { AppRoutingModule, routingComponents } from './app-routing.module';
import { LoginComponent } from './login/login.component';
import { CatalogComponent } from './catalog/catalog.component';
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from './home/home.component';

const appRoutes: Routes =[
  {path: '**', component: HomeComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    routingComponents,
    LoginComponent,
    CatalogComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
