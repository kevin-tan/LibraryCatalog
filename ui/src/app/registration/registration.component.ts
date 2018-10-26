import { Component, OnInit } from '@angular/core';
import {User} from "./user";
import { RegisterService } from './register.service';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  ngOnInit() {
  }

  //userModel = new User(undefined,undefined,undefined,undefined,undefined,undefined,undefined);

  // constructor(private _registerService: RegisterService){}
  //
  // onSubmit() {
  //   console.log(this.userModel);
  //   this._registerService.register(this.userModel)
  //     .subscribe(
  //       data => console.log('Success!', data),
  //       error => console.log('Error!', error)
  //     )
  // }

  constructor(private http: HttpClient) {
  }

  register(username: string, password: string, userType: string, id: string, email: string, newUserPassword: string, firstName: string, lastName: string, phoneNumber: string, address: string) {
    console.log(userType, id, email, newUserPassword, firstName, lastName, phoneNumber, address);
    let Admin = "Admin:{"+id + ", " + email + ", "+newUserPassword+", "+firstName+", "+lastName+", "+phoneNumber+", "+address+"}";
    let headers = new HttpHeaders({"Authorization" : "Basic " + btoa(username + ":" + password)});
    let options = {headers: headers};
    this.http.post('http://localhost:8080/admin/register', Admin, options).subscribe(response => {
      console.log(response);
    }, err => {
      console.log(err);
    })
  }

}
