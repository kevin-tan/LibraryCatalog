import {Injectable} from '@angular/core';
import {Router} from "@angular/router";

@Injectable()
export class HomeRedirectService{

  constructor(private router: Router) {
  }

  redirect(): void {
    this.router.navigateByUrl('/catalog');
  }
}
