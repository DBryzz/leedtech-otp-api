import { Routes } from '@angular/router';
import { EnrollmentComponent } from './components/enrollment/enrollment.component';
import { PaymentComponent } from './components/payment/payment.component';
import { PaymentsListComponent } from './components/payments-list/payments-list.component';

export const routes: Routes = [
    { path: '', redirectTo: '/enroll', pathMatch: 'full' },
    { path: 'enroll', component: EnrollmentComponent },
    { path: 'payment', component: PaymentComponent },
    { path: 'payments', component: PaymentsListComponent }
];
