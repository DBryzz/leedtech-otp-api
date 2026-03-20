import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PaymentService } from '../../services/payment.service';
import { PaymentPayload, PaymentHistory } from '../../models/payment.model';
import {isNonNullArray} from "../../lib/utils";

@Component({
    selector: 'app-payment',
    standalone: true,
    imports: [CommonModule, FormsModule],
    template: `
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h4 class="mb-0">Make Payment</h4>
                    </div>
                    <div class="card-body">
                        <div *ngIf="successMessage" class="alert alert-success">
                            {{ successMessage }}
                        </div>
                        <div *ngIf="errorMessage" class="alert alert-danger">
                            {{ errorMessage }}
                        </div>

                        <form (ngSubmit)="onSubmit()">
                            <div class="mb-3">
                                <label for="studentNumber" class="form-label">Student Number</label>
                                <input 
                                    type="text" 
                                    class="form-control" 
                                    id="studentNumber" 
                                    [(ngModel)]="payload.studentNumber" 
                                    name="studentNumber"
                                    placeholder="Enter your student number"
                                    required>
                            </div>

                            <div class="mb-3">
                                <label for="paymentAmount" class="form-label">Payment Amount</label>
                                <input 
                                    type="number" 
                                    class="form-control" 
                                    id="paymentAmount" 
                                    [(ngModel)]="payload.paymentAmount" 
                                    name="paymentAmount"
                                    min="1"
                                    placeholder="Enter payment amount"
                                    required>
                                <div class="form-text">
                                    Minimum payment: 1. Incentive rates: 1% (&lt;100,000), 3% (100,000-499,999), 5% (500,000+)
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="paymentDate" class="form-label">Payment Date (Optional)</label>
                                <input 
                                    type="date" 
                                    class="form-control" 
                                    id="paymentDate" 
                                    [(ngModel)]="payload.paymentDate" 
                                    name="paymentDate">
                            </div>

                            <button type="submit" class="btn btn-primary" [disabled]="loading">
                                <span *ngIf="loading" class="spinner-border spinner-border-sm me-2"></span>
                                Submit Payment
                            </button>
                        </form>

                        <div *ngIf="paymentResult" class="mt-4">
                            <div class="card bg-light">
                                <div class="card-body">
                                    <h5 class="card-title">Payment Result</h5>
                                    <table class="table table-sm">
                                        <tbody>
                                            <tr>
                                                <td><strong>Payment Amount:</strong></td>
                                                <td>{{ paymentResult.paymentAmount | currency }}</td>
                                            </tr>
                                            <tr>
                                                <td><strong>Incentive Rate:</strong></td>
                                                <td>{{ paymentResult.incentiveRate * 100 | number:'1.0-2' }}%</td>
                                            </tr>
                                            <tr>
                                                <td><strong>Incentive Amount:</strong></td>
                                                <td>{{ paymentResult.incentiveAmount | currency }}</td>
                                            </tr>
                                            <tr>
                                                <td><strong>Total Payment:</strong></td>
                                                <td>{{ paymentResult.totalPaymentAmount | currency }}</td>
<!--                                                <td>{{ paymentResult.paymentAmount + paymentResult.incentiveAmount | currency }}</td>-->
                                            </tr>
                                            <tr>
                                                <td><strong>Previous Balance:</strong></td>
                                                <td>{{ paymentResult.previousAmount | currency }}</td>
                                            </tr>
                                            <tr>
                                                <td><strong>New Balance:</strong></td>
                                                <td class="fw-bold">{{ paymentResult.newBalance | currency }}</td>
                                            </tr>
                                            <tr>
                                                <td><strong>Payment Date:</strong></td>
                                                <td>{{ paymentResult.paymentDate | date:'fullDate' }}</td>
                                            </tr>
                                            <tr>
                                                <td><strong>Next Payment Due:</strong></td>
                                                <td>{{ paymentResult.nextPaymentDueDate | date:'fullDate' }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `
})
export class PaymentComponent {
    payload: PaymentPayload = {
        studentNumber: '',
        paymentAmount: 0,
        paymentDate: null
    };
    paymentResult: PaymentHistory | null = null;
    loading = false;
    successMessage = '';
    errorMessage = '';

    constructor(private paymentService: PaymentService) {}

    onSubmit(): void {
        this.loading = true;
        this.successMessage = '';
        this.errorMessage = '';
        this.paymentResult = null;

        this.paymentService.makePayment(this.payload).subscribe({
            next: (result) => {
                this.loading = false;
                this.paymentResult = result;
                this.successMessage = 'Payment processed successfully!';
            },
            error: (error) => {
                this.loading = false;
                this.errorMessage = isNonNullArray(error.error?.invalidParams)  ? error.error?.invalidParams[0].reason : error.error?.detail;
                // this.errorMessage = error.error?.detail || error.error?.title || 'Payment failed. Please try again.';
                console.error('Payment error:', error);
            }
        });
    }
}
