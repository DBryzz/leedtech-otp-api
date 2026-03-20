export interface PaymentHistory {
    id: string;
    studentNumber: string;
    previousAmount: number;
    paymentAmount: number;
    incentiveRate: number;
    incentiveAmount: number;
    totalPaymentAmount: number;
    newBalance: number;
    paymentDate: string;
    nextPaymentDueDate: string;
    createdOn: string;
}

export interface PaymentPayload {
    studentNumber: string;
    paymentAmount: number;
    paymentDate: string | null;
}
