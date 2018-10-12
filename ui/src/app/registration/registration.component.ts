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

  userModel = new User(undefined,undefined,undefined,undefined,undefined,undefined,undefined);

  constructor(private _registerService: RegisterService){}

  onSubmit() {
    this._registerService.register(this.userModel)
      .subscribe(
        data => console.log('Success!', data),
        error => console.log('Error!', error)
      )
  }
}
