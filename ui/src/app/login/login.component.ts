import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string) {
    let headers = new HttpHeaders({"Authorization" : "Basic " + btoa(username + ":" + password)});
    let options = {headers: headers}
    this.http.post('http://localhost:8080/app/v1/login', null, options).subscribe(response => {
      console.log(response);
    }, err => {
      console.log(err);
    })
  }

  ngOnInit() {
  }

}
