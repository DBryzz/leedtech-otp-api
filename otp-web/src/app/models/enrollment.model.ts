export interface AcademicLevel {
    id: string;
    name: string;
    fees: number;
}

export interface Enrollment {
    id: string;
    fees: number;
    initialDeposit: number;
    student: User | null;
    academicLevel: AcademicLevel;
    academicYear: string;
    createdOn: string;
}

export interface User {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
}

export interface EnrollmentRequest {
    fees: number;
    initialDeposit: number;
    academicLevelId: string;
    academicYear: string;
    paymentDate: string | null;
}
