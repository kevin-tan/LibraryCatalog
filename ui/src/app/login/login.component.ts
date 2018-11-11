import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LoginRedirectService} from "./login-redirect.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private http: HttpClient, private loginRedirectService: LoginRedirectService) {
  }

  login(email: string, password: string) {
    let headers = new HttpHeaders({"Authorization": "Basic " + btoa(email + ":" + password), "Content-Type": "application/json"});
    let options = {headers: headers}
    sessionStorage.setItem("loggedIn", "false");
    let body = JSON.stringify({"email": email})
    this.http.post('http://localhost:8080/app/v1/login', body, options).subscribe(response => {
      sessionStorage.setItem("email", email);
      sessionStorage.setItem("loggedIn", "true");
      this.loginRedirectService.redirect();
    })

  }

  ngOnInit() {
  }

}
