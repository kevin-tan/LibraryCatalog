import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {User} from "../registration/user";
import {ActiveUser} from "./ActiveUser";
import {Magazine} from "../catalog/dto/magazine";
import {isNullOrUndefined} from "util";

@Component({
  selector: 'app-activeUsers',
  templateUrl: './activeUsers.component.html',
  styleUrls: ['./activeUsers.component.css']
})
export class activeUsersComponent implements OnInit {

  activeUserList: Array<ActiveUser> = [];
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
    }, error => {
      console.log(error);
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
    }, error => {
      console.log(error);
    });
  }

}
