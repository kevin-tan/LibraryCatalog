import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from "../registration/user";
import {ActiveUser} from "./ActiveUser";
import {isNullOrUndefined} from "util";

@Component({
  selector: 'app-activeUsers',
  templateUrl: './activeUsers.component.html',
  styleUrls: ['./activeUsers.component.css']
})
export class activeUsersComponent implements OnInit {

  userList: Array<User> = [];

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.getActiveUsers();
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
