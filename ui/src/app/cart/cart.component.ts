import { Component, OnInit } from '@angular/core';
import {HomeRedirectService} from "../home/home-redirect.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
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

  loanableItems: LoanableItem[];
  errorMessage: string;
  selectedItem: LoanableItem;

  getCartItems(){
    this.http.get<LoanableItem[]>('http://localhost:8080/client/cart/' + sessionStorage.getItem('user_id') + "/items", {withCredentials: true}).subscribe(response => {
      for (let item of response) {
        item.client = item.client['Client'];
        item.spec = item.spec[item.type];
      }
      this.loanableItems = response;

      this.errorMessage = "";
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

  cancel(){
    this.http.delete<LoanableItem>('http://localhost:8080/client/' + sessionStorage.getItem('user_id') + "/cancelLoan", {withCredentials: true},)
      .subscribe(response => {
      }, error => {
        console.log(error);
      });
  }

  cancelRemove(){
    this.selectedItem = null;
  }

  checkout(adminUsername: string, adminPassword: string) {
    let url: string = "http://localhost:8080/client/" + sessionStorage.getItem('user_id') + "/loan";
    let headers = new HttpHeaders({'Authorization': 'Basic ' + btoa(adminUsername+':'+adminPassword), 'Content-Type': 'application/json'});
    let options = {headers: headers};
    let obj = [];
    this.loanableItems.forEach(item => {
      let json = {};
      json["id"] = item.id;
      json["available"] = item.available;
      json["client"] = {["Client"]:item.client};
      json["spec"] = {[item.type]:item.spec};
      json["type"] = item.type;
      obj.push({["LoanableItem"]:json});

      let body = JSON.stringify(obj);

      this.http.post(url, body, options).subscribe(response => {
        this.getCartItems();
      }, error => {
        this.errorMessage = "Invalid Admin Credentials";
      })

    });
  }

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
}
