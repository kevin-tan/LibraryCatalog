import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MatSnackBar, MatTableDataSource} from '@angular/material';
import {NgForm} from '@angular/forms';
import {SelectionModel} from '@angular/cdk/collections';
import {User} from '../registration/user';

@Component({
  selector: 'manage-users',
  templateUrl: './manage.users.component.html',
  styleUrls: ['./manage.users.component.css']
})

export class ManageUsersComponent implements OnInit {

  displayUserColumns: string[] = ['select', 'title', 'artist', 'label', 'type', 'asin', 'releaseDate', 'quantity'];
  UserList: User[];
  matUserList: MatTableDataSource<User>;
  userSelection = new SelectionModel<User>(false, []);
  userSelectedRow: User;

  @ViewChild('userForm') userForm: NgForm;

  ngOnInit() {
  }

  constructor(private http: HttpClient, public snackBar: MatSnackBar) {
  }

  editUser() {
    console.log('hi from edit');
  }

  deleteUser() {
    console.log('hi from delete');
  }

}
