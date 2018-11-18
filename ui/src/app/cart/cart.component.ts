import { Component, OnInit } from '@angular/core';
import {HomeRedirectService} from "../home/home-redirect.service";
import {HttpClient} from "@angular/common/http";
import {MatTableDataSource} from "@angular/material";
import {Book} from "../catalog/dto/book";
import {LoanableItem} from "../catalog/dto/loanableItem";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) { }



  ngOnInit() {
  }

  loanableItems: Array<LoanableItem>;

  logout(): void {
    let body = JSON.stringify({'email': sessionStorage.getItem('email')});
    this.http.post('http://localhost:8080/logout', body, {withCredentials: true}).subscribe(response => {
      this.homeRedirectService.redirect();
      sessionStorage.setItem('loggedIn', 'false');
      sessionStorage.setItem('email', '');
      sessionStorage.setItem('user_id', '');
    }, error => {
      console.log(error);
    });
  }

  getCartItem(){
    this.http.get<Array<LoanableItem>>('http://localhost:8080/user/cart/' + sessionStorage.getItem('user_id') + "/items", {withCredentials: true}).subscribe(response => {
      this.loanableItems = response['loanableItems'] as Array<LoanableItem>;
    }, error => {
      console.log(error);
    });
  }

  addItemToCart(){

  }

  removeItemFromCart(){

  }

  checkout(){

  }
}
