export function isNonNullArray(arr: any[]): boolean {
    return arr !== null && Array.isArray(arr) && arr.length > 0
}


export function formatDate(dateString: string) {
    return new Date(dateString).toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric',
        year: 'numeric'
    });
};