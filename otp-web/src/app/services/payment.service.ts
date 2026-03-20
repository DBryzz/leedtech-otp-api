import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentHistory, PaymentPayload } from '../models/payment.model';
import {AuthService} from "./auth.service";

@Injectable({
    providedIn: 'root'
})
export class PaymentService {
    private apiUrl = 'http://localhost:8081/api/v1';

    constructor(private http: HttpClient, private authService: AuthService) { }

    makePayment(payload: PaymentPayload): Observable<PaymentHistory> {
        return this.http.post<PaymentHistory>(
            `${this.apiUrl}/secure/student/one-time-fee-payment`,
            payload, {
                headers: {
                    'Authorization': `Bearer ${this.authService.getToken()}`
                }
            }
        );
    }

    getPayments(): Observable<PaymentHistory[]> {
        return this.http.get<PaymentHistory[]>(
            `${this.apiUrl}/secure/student/payments`, {
                headers: {
                    'Authorization': `Bearer ${this.authService.getToken()}`
                }
            }
        );
    }

    getPayment(id: string): Observable<PaymentHistory> {
        return this.http.get<PaymentHistory>(
            `${this.apiUrl}/secure/student/payments/${id}`, {
                headers: {
                    'Authorization': `Bearer ${this.authService.getToken()}`
                }
            }
        );
    }

    // headers?: HttpHeaders | {
    //     [header: string]: string | string[];
    // };
}
