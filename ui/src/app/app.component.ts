import { Component } from '@angular/core';
import { User } from "./user";
import { RegisterService } from './register.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'ui';

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
