import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "./user";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  _url = 'http://localhost:4020/';
  constructor(private _http: HttpClient) { }

  register(user: User) {
    return this._http.post<any>(this._url, user);
  }


  userModel = new User('jeff', 'jeff', 'Jeffrey','Li', 'jeffreyli16@hotmail.com', '5145829225', 'test');

  constructor(private _registerService: RegisterService){}

  onSubmit() {
    this._registerService.register(this.userModel)
      .subscribe(
        data => console.log('Success!', data),
        error => console.log('Error!', error)
      )
  }
}
