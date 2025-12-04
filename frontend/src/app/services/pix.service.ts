import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const PIX_API = 'http://localhost:8080/api/pix/';

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
    providedIn: 'root'
})
export class PixService {

    constructor(private http: HttpClient) { }

    createKey(key: string, type: string): Observable<any> {
        return this.http.post(PIX_API + 'keys', { key, type }, httpOptions);
    }

    getMyKeys(): Observable<any> {
        return this.http.get(PIX_API + 'keys', httpOptions);
    }

    getKeyOwner(key: string): Observable<any> {
        return this.http.get(PIX_API + 'keys/' + key, httpOptions);
    }

    transfer(targetKey: string, amount: number): Observable<any> {
        return this.http.post(PIX_API + 'transfer', { targetKey, amount }, httpOptions);
    }
}
