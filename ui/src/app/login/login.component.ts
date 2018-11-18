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
    let options = {headers: headers, withCredentials: true};
    sessionStorage.setItem("loggedIn", "false");
    let body = JSON.stringify({"email": email})
    this.http.post('http://localhost:8080/app/v1/login', body, options).subscribe(response => {
      sessionStorage.setItem("email", email);
      sessionStorage.setItem("loggedIn", "true");
      sessionStorage.setItem("user_id", response['id']);
      sessionStorage.setItem("userType", response['userType']);
      this.loginRedirectService.redirect();
    }, error => {
      console.log(error);
    });
  }

  ngOnInit() {
  }

}
