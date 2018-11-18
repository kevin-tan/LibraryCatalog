import { Component, OnInit } from '@angular/core';
import {HomeRedirectService} from "../home/home-redirect.service";
import {HttpClient} from "@angular/common/http";
import {LoanableItem} from "../catalog/dto/loanableItem";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css', '../home/home.component.css']
})
export class CartComponent implements OnInit {

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) { }

  ngOnInit() {
    this.getCartItems();
  }

  loanableItems: Array<LoanableItem>;
  displayColumn: string[] = ['title'];

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

  getCartItems(){
    this.http.get<Array<LoanableItem>>('http://localhost:8080/client/cart/' + sessionStorage.getItem('user_id') + "/items", {withCredentials: true}).subscribe(response => {
      this.loanableItems = response['loanableItems'] as Array<LoanableItem>;
      console.log(this.loanableItems);
    }, error => {
      console.log(error);
    });
  }

  addItemToCart(loanableItem: LoanableItem){
    this.http.post<LoanableItem>('http://localhost:8080/client/cart/' + sessionStorage.getItem('user_id') + "/add", {withCredentials: true, loanableItem}, )
      .subscribe(response => {
      console.log(this.loanableItems);
    }, error => {
      console.log(error);
    });
  }

  removeItemFromCart(loanableItem: LoanableItem){
    this.http.put<LoanableItem>('http://localhost:8080/client/cart/' + sessionStorage.getItem('user_id') + "/remove", {withCredentials: true, loanableItem},)
      .subscribe(response => {
      }, error => {
        console.log(error);
      });
  }

  checkout(){
    this.http.post<LoanableItem>('http://localhost:8080/client/' + sessionStorage.getItem('user_id') + "/loan", {withCredentials: true},)
      .subscribe(response => {
      }, error => {
        console.log(error);
      });
  }

  cancel(){
    this.http.delete<LoanableItem>('http://localhost:8080/client/' + sessionStorage.getItem('user_id') + "/cancelLoan", {withCredentials: true},)
      .subscribe(response => {
      }, error => {
        console.log(error);
      });
  }
}
