import { Component, OnInit } from '@angular/core';
import {HomeRedirectService} from "../home/home-redirect.service";
import {HttpClient} from "@angular/common/http";
import {MatTableDataSource} from "@angular/material";

@Component({
  selector: 'app-return',
  templateUrl: './return.component.html',
  styleUrls: ['./return.component.css']
})
export class ReturnComponent implements OnInit {

  constructor(private http: HttpClient, private homeRedirectService: HomeRedirectService) { }

  ngOnInit() {
  }

  getLoanedItems(): void{
  }

}
