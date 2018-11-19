import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {HomeRedirectService} from "../home/home-redirect.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css', '../home/home.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) {
  }

  userType: string;

  ngOnInit() {
    this.userType = sessionStorage.getItem("userType");
  }

  logout() {
    let body = JSON.stringify({'email': sessionStorage.getItem('email')});
    this.http.post('http://localhost:8080/logout', body, {withCredentials: true}).subscribe(response => {
      this.homeRedirectService.redirect();
      sessionStorage.setItem('loggedIn', 'false');
      sessionStorage.setItem('email', '');
      sessionStorage.setItem("user_id", '');
      sessionStorage.setItem("userType", '');
    }, error => {
      console.log(error);
    });
  }


}
