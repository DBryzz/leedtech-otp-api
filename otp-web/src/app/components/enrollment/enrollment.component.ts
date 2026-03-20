import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EnrollmentService } from '../../services/enrollment.service';
import { AcademicLevel, EnrollmentRequest } from '../../models/enrollment.model';
import {isNonNullArray} from "../../lib/utils";

@Component({
    selector: 'app-enrollment',
    standalone: true,
    imports: [CommonModule, FormsModule],
    template: `
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h4 class="mb-0">Student Enrollment</h4>
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
                                <label for="academicLevel" class="form-label">Academic Level</label>
                                <select 
                                    class="form-select" 
                                    id="academicLevel" 
                                    [(ngModel)]="request.academicLevelId" 
                                    name="academicLevelId"
                                    required>
                                    <option value="" disabled>Select Academic Level</option>
                                    <option *ngFor="let level of academicLevels" [value]="level.id">
                                        {{ level.name }} - {{ level.fees | currency }}
                                    </option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="academicYear" class="form-label">Academic Year</label>
                                <input 
                                    type="text" 
                                    class="form-control" 
                                    id="academicYear" 
                                    [(ngModel)]="request.academicYear" 
                                    name="academicYear"
                                    placeholder="e.g., 2026"
                                    required>
                            </div>

                            <div class="mb-3">
                                <label for="fees" class="form-label">Total Fees</label>
                                <input 
                                    type="number" 
                                    class="form-control" 
                                    id="fees" 
                                    [(ngModel)]="request.fees" 
                                    name="fees"
                                    min="1"
                                    required>
                            </div>

                            <div class="mb-3">
                                <label for="initialDeposit" class="form-label">Initial Deposit</label>
                                <input 
                                    type="number" 
                                    class="form-control" 
                                    id="initialDeposit" 
                                    [(ngModel)]="request.initialDeposit" 
                                    name="initialDeposit"
                                    min="1"
                                    required>
                            </div>

                            <div class="mb-3">
                                <label for="paymentDate" class="form-label">Payment Date (Optional)</label>
                                <input 
                                    type="date" 
                                    class="form-control" 
                                    id="paymentDate" 
                                    [(ngModel)]="request.paymentDate" 
                                    name="paymentDate">
                            </div>

                            <button type="submit" class="btn btn-primary" [disabled]="loading">
                                <span *ngIf="loading" class="spinner-border spinner-border-sm me-2"></span>
                                Enroll Now
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    `
})
export class EnrollmentComponent implements OnInit {
    academicLevels: AcademicLevel[] = [];
    request: EnrollmentRequest = {
        fees: 0,
        initialDeposit: 0,
        academicLevelId: '',
        academicYear: new Date().getFullYear().toString(),
        paymentDate: null
    };
    loading = false;
    successMessage = '';
    errorMessage = '';

    constructor(private enrollmentService: EnrollmentService) {}

    ngOnInit(): void {
        this.loadAcademicLevels();
    }

    loadAcademicLevels(): void {
        this.enrollmentService.getAcademicLevels().subscribe({
            next: (levels) => {
                this.academicLevels = levels;
            },
            error: (error) => {
                this.errorMessage = 'Failed to load academic levels';
                console.error('Error loading academic levels:', error);
            }
        });
    }

    onSubmit(): void {
        this.loading = true;
        this.successMessage = '';
        this.errorMessage = '';

        this.enrollmentService.enroll(this.request).subscribe({
            next: (enrollment) => {
                this.loading = false;
                this.successMessage = 'Enrollment successful!';
                console.log('Enrollment:', enrollment);
            },
            error: (error) => {
                this.loading = false;
                this.errorMessage = isNonNullArray(error.error?.invalidParams)  ? error.error?.invalidParams[0].reason : error.error?.detail;
                // this.errorMessage = error.error?.detail || 'Enrollment failed. Please try again.';
                console.error('Enrollment error:', error);
            }
        });
    }
}
