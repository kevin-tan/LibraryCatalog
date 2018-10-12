import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  title = 'ui';

  constructor(private http: HttpClient) { }

  login(username: string, password: string){

    let headers = new HttpHeaders();
    headers = headers.append("Authorization", "Basic " + btoa("username:password"));
    headers = headers.append("Content-Type", "application/x-www-form-urlencoded");

    this.http.post('http://localhost:8080/app/v1/login ',"", {headers: headers}).subscribe(response => {
      console.log(response);
    }, err => {
      console.log("User authentication failed!");
    })
  }

  ngOnInit() {
  }

}
