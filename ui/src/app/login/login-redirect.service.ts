import {Injectable} from '@angular/core';
import {Router} from "@angular/router";

@Injectable()
export class LoginRedirectService{

  constructor(private router: Router) {
  }

  redirect(): void {
      this.router.navigateByUrl('/home');
  }
}
