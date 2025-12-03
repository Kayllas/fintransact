import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = 'http://localhost:8080/api/transactions/';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(private http: HttpClient) { }

  getBalance(): Observable<any> {
    return this.http.get(API_URL + 'balance');
  }

  getHistory(): Observable<any> {
    return this.http.get(API_URL + 'history');
  }

  transfer(targetAccountNumber: string, amount: number): Observable<any> {
    return this.http.post(API_URL + 'transfer', {
      targetAccountNumber,
      amount
    });
  }
}
