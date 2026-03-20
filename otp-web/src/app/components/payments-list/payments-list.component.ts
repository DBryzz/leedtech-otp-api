import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PaymentService } from '../../services/payment.service';
import { PaymentHistory } from '../../models/payment.model';

@Component({
    selector: 'app-payments-list',
    standalone: true,
    imports: [CommonModule],
    template: `
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h4 class="mb-0">Payment History</h4>
                        <button class="btn btn-light btn-sm" (click)="loadPayments()">
                            Refresh
                        </button>
                    </div>
                    <div class="card-body">
                        <div *ngIf="errorMessage" class="alert alert-danger">
                            {{ errorMessage }}
                        </div>

                        <div *ngIf="loading" class="text-center">
                            <div class="spinner-border" role="status">
                                <span class="visually-hidden">Loading...</span>
                            </div>
                        </div>

                        <div *ngIf="!loading && payments.length === 0" class="text-center text-muted py-5">
                            <p>No payments found.</p>
                        </div>

                        <div *ngIf="!loading && payments.length > 0" class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Student #</th>
                                        <th>Payment Amount</th>
                                        <th>Incentive. Rate</th>
                                        <th>Incentive</th>
                                        <th>Total Payment Amt</th>
                                        <th>Previous Balance</th>
                                        <th>New Balance</th>
                                        <th>Next Due Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr *ngFor="let payment of payments">
                                        <td>{{ payment.paymentDate | date:'fullDate' }}</td>
                                        <td>{{ payment.studentNumber }}</td>
                                        <td>{{ payment.paymentAmount | currency }}</td>
                                        <td>{{ payment.incentiveRate * 100 | number:'1.0-2' }}%</td>
                                        <td>{{ payment.incentiveAmount | currency }}</td>
                                        <td>{{ payment.totalPaymentAmount | currency }}</td>
                                        <td>{{ payment.previousAmount | currency }}</td>
                                        <td [class.text-success]="payment.newBalance <= 0" 
                                            [class.text-danger]="payment.newBalance > 0">
                                            <strong>{{ payment.newBalance | currency }}</strong>
                                        </td>
                                        <td>{{ payment.nextPaymentDueDate | date:'fullDate' }}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <div *ngIf="!loading && payments.length > 0" class="mt-4">
                            <div class="card bg-light">
                                <div class="card-body">
                                    <h5 class="card-title">Summary</h5>
                                    <div class="row">
                                        <div class="col-md-4">
                                            <p class="mb-1"><strong>Total Payments:</strong></p>
                                            <p class="text-primary fs-5">{{ payments.length }}</p>
                                        </div>
                                        <div class="col-md-4">
                                            <p class="mb-1"><strong>Total Amount Paid:</strong></p>
                                            <p class="text-success fs-5">{{ totalPaid | currency }}</p>
                                        </div>
                                        <div class="col-md-4">
                                            <p class="mb-1"><strong>Current Balance:</strong></p>
                                            <p [class.text-success]="currentBalance <= 0" 
                                               [class.text-danger]="currentBalance > 0"
                                               class="fs-5">
                                               <strong>{{ currentBalance | currency }}</strong>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `
})
export class PaymentsListComponent implements OnInit {
    payments: PaymentHistory[] = [];
    loading = false;
    errorMessage = '';

    constructor(private paymentService: PaymentService) {}

    ngOnInit(): void {
        this.loadPayments();
    }

    loadPayments(): void {
        this.loading = true;
        this.errorMessage = '';

        this.paymentService.getPayments().subscribe({
            next: (payments) => {
                this.loading = false;
                this.payments = payments.sort((a, b) => 
                    new Date(b.paymentDate).getTime() - new Date(a.paymentDate).getTime()
                );
            },
            error: (error) => {
                this.loading = false;
                this.errorMessage = error.error?.detail || 'Failed to load payments. Please try again.';
                console.error('Error loading payments:', error);
            }
        });
    }

    get totalPaid(): number {
        return this.payments.reduce((sum, p) => sum + (p.paymentAmount + p.incentiveAmount), 0);
    }

    get currentBalance(): number {
        if (this.payments.length === 0) return 0;
        const latestPayment = this.payments[0];
        return latestPayment.newBalance;
    }
}
