import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {MatSnackBar, MatSort, MatTableDataSource} from '@angular/material';
import {NgForm} from '@angular/forms';
import {SelectionModel} from '@angular/cdk/collections';
import {User} from '../registration/user';
import {CatalogUser} from './catalogUser';

@Component({
  selector: 'manage-users',
  templateUrl: './manage.users.component.html',
  styleUrls: ['./manage.users.component.css']
})

export class ManageUsersComponent implements OnInit {

  headers: HttpHeaders = new HttpHeaders({'Content-Type': 'application/json'});
  options: Object = {headers: this.headers, withCredentials: true};

  displayUserColumns: string[] = ['select', 'userType', 'firstName', 'lastName', 'address', 'email', 'phoneNumber'];
  userList: CatalogUser[];
  matUserList: MatTableDataSource<CatalogUser>;
  userSelection = new SelectionModel<CatalogUser>(false, []);
  userSelectedRow: CatalogUser;

  @ViewChild('userForm') userForm: NgForm;
  @ViewChild('userSort') userSort: MatSort;

  ngOnInit() {
    this.startSession();
    this.getAllUsers();
  }

  startSession() {
    this.http.post<string>('http://localhost:8080/admin/catalog/edit', null, {withCredentials: true}).subscribe(response => {
      sessionStorage.setItem('sessionId', response['sessionId']);
    });
  }

  saveAll() {
    this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/save', null, {withCredentials: true}).subscribe(response => {
      this.getAllUsers();
      this.startSession();
      this.snackBar.open('Changes saved successfully', 'OK', {duration: 2000});
    });
  }

  constructor(private http: HttpClient, public snackBar: MatSnackBar) {
  }

  editUser(firstName: string, lastName: string, address: string, email: string, phoneNumber: string, password: string) {
    if (this.userSelection.isSelected(this.userSelectedRow)) {
      let body = JSON.stringify({
        [this.userSelectedRow.userType]: {
          'id': this.userSelectedRow.id, 'firstName': firstName, 'lastName': lastName, 'physicalAddress': address, 'email': email, 'phoneNumber': phoneNumber,
          'password': password === '' ? '' : password
        }
      });

      this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/editUser', body, this.options).subscribe(response => {
        this.userForm.resetForm();
        for (let i = 0; i < this.userList.length; i++) {
          if (this.userList[i].id === this.userSelectedRow.id) {
            this.userList[i] = {
              'userType': this.userSelectedRow.userType, 'id': this.userSelectedRow.id, 'firstName': firstName, 'lastName': lastName, 'physicalAddress': address, 'email': email, 'phoneNumber': phoneNumber,
              'password': ''
            };
            break;
          }
        }
        this.snackBar.open('User deleted successfully!', 'OK', {duration: 2000});

        this.matUserList = new MatTableDataSource(this.userList);
        this.matUserList.sort = this.userSort;
      }, error => {
        this.userSelection.clear();
        this.userForm.resetForm();
        this.snackBar.open('E-mail or phone number is already being used!!', 'OK', {duration: 2000});
      });
    }
  }

  deleteUser() {
    if (this.userSelection.isSelected(this.userSelectedRow)) {
      this.http.post('http://localhost:8080/admin/catalog/' + sessionStorage.getItem('sessionId') + '/deleteUser/' + this.userSelectedRow.id, null, this.options).subscribe(response => {
        this.userForm.resetForm();
        for(let i = 0; i < this.userList.length; i++) {
          if(this.userList[i].id === this.userSelectedRow.id) {
            this.userList.splice(i, 1);
            break;
          }
        }
        this.snackBar.open('User deleted successfully!', 'OK', {duration: 2000});

        this.matUserList = new MatTableDataSource(this.userList);
        this.matUserList.sort = this.userSort;
      }, error => {
        this.userSelection.clear();
        this.userForm.resetForm();
        this.snackBar.open('There must always be at least on Admin in the system!', 'OK', {duration: 2000});
      });
    }
  }

  getAllUsers() {
    this.http.get<Array<CatalogUser>>('http://localhost:8080/admin/catalog/getAllUsers', {withCredentials: true}).subscribe(response => {
      this.userForm.resetForm();
      this.matUserList = new MatTableDataSource(response);
      this.userList = response;
      this.matUserList.sort = this.userSort;
    }, error => {
      console.log(error);
    });
  }

  userRowSelected(row: CatalogUser) {
    this.userSelectedRow = row;
    if (!this.userSelection.isSelected(row)) {
      (<HTMLInputElement>document.getElementById('firstName')).value = row.firstName;
      (<HTMLInputElement>document.getElementById('lastName')).value = row.lastName;
      (<HTMLInputElement>document.getElementById('address')).value = row.physicalAddress;
      (<HTMLInputElement>document.getElementById('email')).value = row.email;
      (<HTMLInputElement>document.getElementById('phoneNumber')).value = row.phoneNumber;
    } else {
      this.userForm.resetForm();
    }
  }
}
