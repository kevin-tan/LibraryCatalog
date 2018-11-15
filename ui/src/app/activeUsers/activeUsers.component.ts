import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {User} from "../registration/user";
import {ActiveUser} from "./ActiveUser";
import {isNullOrUndefined} from "util";
import {HomeRedirectService} from "../home/home-redirect.service";

@Component({
  selector: 'app-activeUsers',
  templateUrl: './activeUsers.component.html',
  styleUrls: ['./activeUsers.component.css']
})
export class activeUsersComponent implements OnInit {

  userList: Array<User> = [];

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) { }

  ngOnInit() {
    this.getActiveUsers();
  }

  logout(){
    let body = JSON.stringify({'email': sessionStorage.getItem('email')});
    this.http.post('http://localhost:8080/logout', body, {withCredentials:true}).subscribe(response => {
      this.homeRedirectService.redirect();
      sessionStorage.setItem('loggedIn', 'false');
      sessionStorage.setItem('email', '');
    }, error => {
      console.log(error);
    });
  }

  getActiveUsers(){
    this.http.get<Array<ActiveUser>>('http://localhost:8080/admin/viewActiveUsers', {withCredentials: true}).subscribe(response => {
      response.forEach((activeUser) => {
        this.addUser(activeUser);
      })
    });
  }

  addUser(activeUser: ActiveUser){
    this.http.get<User>('http://localhost:8080/admin/user/' + activeUser.id, {withCredentials: true}).subscribe(response => {
      if (!isNullOrUndefined(response["Admin"])){
        this.userList.push(response["Admin"] as User);
      }
      if (!isNullOrUndefined(response["Client"])){
        this.userList.push(response["Client"] as User);
      }
    });
  }

}
