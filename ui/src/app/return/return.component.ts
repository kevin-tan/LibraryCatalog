import {Component, OnInit} from '@angular/core';
import {HomeRedirectService} from "../home/home-redirect.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LoanableItem} from "../catalog/dto/loanableItem";

@Component({
  selector: 'app-return',
  templateUrl: './return.component.html',
  styleUrls: ['./return.component.css', '../home/home.component.css']
})
export class ReturnComponent implements OnInit {

  clientId: number;
  loanedItems: LoanableItem[] = null;
  selectedItems: LoanableItem[];

  errorMessage: string = "";

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) { }

  ngOnInit() {
    this.clientId = +sessionStorage.getItem("user_id");
    this.initializeLoanedItems();
  }

  initializeLoanedItems(): void {
    let url: string = "http://localhost:8080/client/" + this.clientId + "/loanedItems";
    this.http.get<LoanableItem[]>(url,{withCredentials:true}).subscribe( items => {
      for (let item of items) {
        item.client = item.client['Client'];
        item.spec = item.spec[item.type];
      }
      this.loanedItems = items;

      this.selectedItems = [];
      this.errorMessage = "";
    }, error => {
      console.log(error);
    })
  }

  returnItems(adminUsername: string, adminPassword: string) {
    let url: string = "http://localhost:8080/client/" + this.clientId + "/return";
    let headers = new HttpHeaders({'Authorization': 'Basic ' + btoa(adminUsername+':'+adminPassword), 'Content-Type': 'application/json'});
    let options = {headers: headers};

    let obj = [];
    this.selectedItems.forEach(item => {
      let json = {};
      json["id"] = item.id;
      json["available"] = item.available;
      json["client"] = {["Client"]:item.client};
      json["spec"] = {[item.type]:item.spec};
      json["type"] = item.type;
      obj.push({["LoanableItem"]:json});
    });

    let body = JSON.stringify(obj);

    this.http.post(url, body, options).subscribe(response => {
      this.initializeLoanedItems();
    }, error => {
      this.errorMessage = "Invalid Admin Credentials";
    })
  }

  logout(){
    let body = JSON.stringify({'email': sessionStorage.getItem('email')});
    this.http.post('http://localhost:8080/logout', body, {withCredentials:true}).subscribe(response => {
      this.homeRedirectService.redirect();
      sessionStorage.setItem('loggedIn', 'false');
      sessionStorage.setItem('email', '');
    }, error => {
      console.log(error);
    });
  }

}
