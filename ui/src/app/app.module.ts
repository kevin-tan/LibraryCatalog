import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule} from "@angular/forms";
import { AppComponent } from './app.component';
import { AppRoutingModule, routingComponents } from './app-routing.module';
import { LoginComponent } from './login/login.component';
import { CatalogComponent } from './catalog/catalog.component';
import { HomeComponent } from './home/home.component';
import {LoginRedirectService} from "./login/login-redirect.service";
import {bookSearchComponent} from "./bookSearch/bookSearch.component";
import {musicSearchComponent} from "./musicSearch/musicSearch.component";
import {movieSearchComponent} from "./movieSearch/movieSearch.component";
import {magazineSearchComponent} from "./magazineSearch/magazineSearch.component";
import {activeUsersComponent} from "./activeUsers/activeUsers.component";
import {transactionComponent} from "./transaction/transaction.component";
import {HomeRedirectService} from "./home/home-redirect.service";
import {ManageCatalogComponent} from './manageCatalog/manage.catalog.component';
import {MatTableModule} from '@angular/material/table';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatSortModule} from '@angular/material/sort';
import {MatRadioModule} from '@angular/material/radio';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { ReturnComponent } from './return/return.component';
import { CartComponent } from './cart/cart.component';
import {MatListModule} from '@angular/material/list';
import { NavbarComponent } from './navbar/navbar.component';
import { DetailsComponent } from './details/details.component';
import {Location} from "@angular/common";

import {MatCheckboxModule} from '@angular/material/checkbox';
import {AddItemSpecService} from './manageCatalog/add.item.spec.service';
import {DeleteItemSpecService} from './manageCatalog/delete.item.spec.service';
import {EditItemSpecService} from './manageCatalog/edit.item.spec.service';
import {EditInventoryService} from './manageCatalog/edit.inventory.service';
import {ManageUsersComponent} from './manageUsers/manage.users.component';

@NgModule({
  declarations: [
    AppComponent,
    routingComponents,
    LoginComponent,
    CatalogComponent,
    HomeComponent,
    bookSearchComponent,
    musicSearchComponent,
    movieSearchComponent,
    magazineSearchComponent,
    activeUsersComponent,
    transactionComponent,
    ReturnComponent,
    CartComponent,
    NavbarComponent,
    DetailsComponent,
    ManageCatalogComponent,
    ManageUsersComponent
  ],
  imports: [
    BrowserModule,
    MatRadioModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    MatTableModule,
    MatSortModule,
    MatListModule,
    MatSnackBarModule,
    MatCheckboxModule,
    BrowserAnimationsModule
  ],
  providers: [LoginRedirectService, HomeRedirectService, Location, AddItemSpecService, DeleteItemSpecService, EditItemSpecService, EditInventoryService],
  bootstrap: [AppComponent]
})
export class AppModule { }
