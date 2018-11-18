import {Component, OnInit} from '@angular/core';
import {HomeRedirectService} from "../home/home-redirect.service";
import {HttpClient} from "@angular/common/http";
import {LoanableItem} from "../catalog/dto/loanableItem";

@Component({
  selector: 'app-return',
  templateUrl: './return.component.html',
  styleUrls: ['./return.component.css', '../home/home.component.css']
})
export class ReturnComponent implements OnInit {

  clientId: number;
  loanedItems: LoanableItem[] = null;

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) { }

  ngOnInit() {
    this.clientId = +sessionStorage.getItem("user_id");
    this.initializeLoanedItems();
  }

  initializeLoanedItems(): void{
    let url: string = "http://localhost:8080/client/" + this.clientId + "/loanedItems";
    this.http.get<LoanableItem[]>(url,{withCredentials:true}).subscribe( items => {
      console.log(items);
    }, error => {
      console.log(error);
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
