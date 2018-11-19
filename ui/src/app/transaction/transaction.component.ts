import {Component, OnInit, ViewChild} from "@angular/core";
import {MatSort, MatTableDataSource} from "@angular/material";
import {LoanTransaction, ReturnTransaction, Transaction} from "./transaction";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {HomeRedirectService} from "../home/home-redirect.service";

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class transactionComponent implements OnInit {

  displayLoanTransactionColumns: string[] = ['itemId', 'userId', 'email', 'title', 'type', 'transactionDate', 'dueDate'];
  displayReturnTransactionColumns: string[] = ['itemId', 'userId', 'email', 'title', 'type', 'transactionDate'];
  matLoanTransactionList: MatTableDataSource<LoanTransaction>;
  matReturnTransactionList: MatTableDataSource<ReturnTransaction>;


  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) {
  }

  @ViewChild('loanTransactionSort') loanTransactionSort: MatSort;
  @ViewChild('returnTransactionSort') returnTransactionSort: MatSort;


  ngOnInit() {
    this.getAllTransactions();
  }

  logout() {
    let body = JSON.stringify({'email': sessionStorage.getItem('email')});
    this.http.post('http://localhost:8080/logout', body, {withCredentials: true}).subscribe(response => {
      this.homeRedirectService.redirect();
      sessionStorage.setItem('loggedIn', 'false');
      sessionStorage.setItem('email', '');
    }, error => {
      console.log(error);
    });
  }

  getAllTransactions(): void {
    this.http.get('http://localhost:8080/admin/transactionHistory/searchAll', {withCredentials: true}).subscribe(response => {
      this.matLoanTransactionList = new MatTableDataSource(response ['loanTransaction'] as Array<LoanTransaction>);
      this.matLoanTransactionList.sort = this.loanTransactionSort;
      this.matReturnTransactionList = new MatTableDataSource(response ['returnTransaction'] as Array<ReturnTransaction>);
      this.matReturnTransactionList.sort = this.returnTransactionSort;
    }, error => {
      console.log(error);
    });
  }

  searchTransactionByItemType(itemType: string) {
    this.http.get('http://localhost:8080/admin/transactionHistory/search/' + itemType, {withCredentials: true}).subscribe(response => {
      this.matLoanTransactionList = new MatTableDataSource(response ['loanTransaction'] as Array<LoanTransaction>);
      this.matLoanTransactionList.sort = this.loanTransactionSort;
      this.matReturnTransactionList = new MatTableDataSource(response ['returnTransaction'] as Array<ReturnTransaction>);
      this.matReturnTransactionList.sort = this.returnTransactionSort;
    }, error => {
      console.log(error);
    });
  }

  searchTransactionByUserId(userId: number) {
    this.http.get('http://localhost:8080/admin/transactionHistory/searchByUserId/' + userId, {withCredentials: true}).subscribe(response => {
      this.matLoanTransactionList = new MatTableDataSource(response ['loanTransaction'] as Array<LoanTransaction>);
      this.matLoanTransactionList.sort = this.loanTransactionSort;
      this.matReturnTransactionList = new MatTableDataSource(response ['returnTransaction'] as Array<ReturnTransaction>);
      this.matReturnTransactionList.sort = this.returnTransactionSort;
    }, error => {
      console.log(error);
    });
  }

  searchTransactionByTransactionDate(transactionDate: Date) {
    let body = JSON.stringify({
      "transactionDate": transactionDate
    })
    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post('http://localhost:8080/admin/transactionHistory/searchAllByTransactionDate', body, options).subscribe(response => {
      this.matLoanTransactionList = new MatTableDataSource(response ['loanTransaction'] as Array<LoanTransaction>);
      this.matLoanTransactionList.sort = this.loanTransactionSort;
      this.matReturnTransactionList = new MatTableDataSource(response ['returnTransaction'] as Array<ReturnTransaction>);
      this.matReturnTransactionList.sort = this.returnTransactionSort;
    }, error => {
      console.log(error);
    });
  }

  searchTransactionByDueDate(dueDate: Date) {
    let body2 = JSON.stringify({
      "dueDate": dueDate
    })
    let headers = new HttpHeaders({"Content-Type": "application/json"});
    let options = {headers: headers, withCredentials: true};
    this.http.post('http://localhost:8080/admin/transactionHistory/loanTransactions/searchByDueDate', body2, options).subscribe(response => {
      this.matLoanTransactionList = new MatTableDataSource(response as Array<LoanTransaction>);
      this.matLoanTransactionList.sort = this.loanTransactionSort;
    }, error => {
      console.log(error);
    });
  }

  convertDate(dateToConvert: Date) {
    let date = new Date(dateToConvert); // had to remove the colon (:) after the T in order to make it work
    let day = date.getDate();
    let monthIndex = date.getMonth();
    let year = date.getFullYear();
    let minutes = date.getMinutes();
    let hours = date.getHours();
    let seconds = date.getSeconds();
    let myFormattedDate = year + "-" + (monthIndex + 1) + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
    return myFormattedDate;
  }

}
