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

  login(username: string, password: string) {
    let headers = new HttpHeaders({"Authorization" : "Basic " + btoa(username + ":" + password)});
    let options = {headers: headers}
    this.http.post('http://localhost:8080/app/v1/login', null, options).subscribe(response => {
      console.log(response);
      this.loginRedirectService.redirect();
    }, err => {
      console.log(err);
    })

  }

  ngOnInit() {
  }

}
