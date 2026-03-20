import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AcademicLevel, Enrollment, EnrollmentRequest } from '../models/enrollment.model';
import {AuthService} from "./auth.service";

@Injectable({
    providedIn: 'root'
})
export class EnrollmentService {
    private apiUrl = 'http://localhost:8081/api/v1';

    constructor(private http: HttpClient, private authService: AuthService) { }

    getAcademicLevels(): Observable<AcademicLevel[]> {
        return this.http.get<AcademicLevel[]>(`${this.apiUrl}/academic-levels`, {
            headers: {
                'Authorization': `Bearer ${this.authService.getToken()}`
            }
        });
    }

    enroll(request: EnrollmentRequest): Observable<Enrollment> {
        return this.http.post<Enrollment>(
            `${this.apiUrl}/secure/student/enrollments`,
            request, {
                headers: {
                    'Authorization': `Bearer ${this.authService.getToken()}`
                }
            }
        );
    }
}
