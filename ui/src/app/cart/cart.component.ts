import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LoanableItem} from "../catalog/dto/loanableItem";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css', '../home/home.component.css']
})
export class CartComponent implements OnInit {

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.getCartItems();
  }

  loanableItems: LoanableItem[];
  errorMessage: string;
  selectedItem: LoanableItem;

  getCartItems(){
    this.http.get<LoanableItem[]>('http://localhost:8080/client/cart/' + sessionStorage.getItem('user_id') + "/items", {withCredentials: true}).subscribe(response => {
      for (let item of response) {
        console.log(item);
      }
      this.loanableItems = response;

      this.errorMessage = "";
    }, error => {
      console.log(error);
    });
  }

  removeItemFromCart(loanableItem: LoanableItem){
    let headers = new HttpHeaders({'Content-Type': 'application/json'});
    let options = {headers: headers, withCredentials: true};

    let json = {};
    json["id"] = loanableItem.id;
    json["available"] = loanableItem.available;
    json["client"] = loanableItem.client;
    json["spec"] = loanableItem.spec;
    json["type"] = loanableItem.type;
    let final = {["LoanableItem"]:json};

    let body = JSON.stringify(final);

    this.http.put<LoanableItem>('http://localhost:8080/client/cart/' + sessionStorage.getItem('user_id') + "/remove", body, options)
      .subscribe(response => {
        this.getCartItems();
        this.selectedItem = null;
      }, error => {
        console.log(error);
      });
  }

  //refresh
  cancel(){
    this.http.delete<LoanableItem>('http://localhost:8080/client/' + sessionStorage.getItem('user_id') + "/cancelLoan", {withCredentials: true},)
      .subscribe(response => {
        this.getCartItems();
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
    let final =[];
    this.loanableItems.forEach(item => {
      let json = {};
      json["id"] = item.id;
      json["available"] = item.available;
      json["client"] = item.client;
      json["spec"] = item.spec;
      json["type"] = item.type;
      final.push({["LoanableItem"]:json});
    });

      let body = JSON.stringify(final);

    console.log(body);

    this.http.post(url, body, options).subscribe(response => {
        this.getCartItems();
      }, error => {
        this.errorMessage = "Invalid";
      })
  }

}
