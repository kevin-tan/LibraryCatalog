import { Component } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {


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

}
