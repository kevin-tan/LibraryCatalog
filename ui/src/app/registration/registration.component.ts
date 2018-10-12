import { Component, OnInit } from '@angular/core';
import {User} from "./user";
import { RegisterService } from './register.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  ngOnInit() {
  }
  userModel = new User('1','jeffreyli16@hotmail.com', 'jeff', 'Jeffrey','Li','5145829225', 'test');

  constructor(private _registerService: RegisterService){}

  onSubmit() {
    this._registerService.register(this.userModel)
      .subscribe(
        data => console.log('Success!', data),
        error => console.log('Error!', error)
      )
  }
}
