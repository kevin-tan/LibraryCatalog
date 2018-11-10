import { Component, OnInit } from '@angular/core';
import {User} from "./user";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  ngOnInit() {
  }

  constructor(private http: HttpClient) {
  }

  newUser = new User(undefined,undefined,undefined,undefined,undefined,undefined,undefined);
  register(username: string, password: string, userType: string) {

    let headers = new HttpHeaders({'Authorization': 'Basic ' + btoa(username+':'+password), 'Content-Type': 'application/json'});
    let options = {headers: headers}
    let body = JSON.stringify({[userType]:this.newUser})
    this.http.post('http://localhost:8080/app/v1/register', body, options).subscribe(response => {
      console.log(response);
    }, err => {
      console.log(err);
    })
  }
}
