/**
 * Common type definitions used across the application
 */

export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface ErrorResponse {
  errorCode: string;
  message: string;
  details?: Record<string, any>;
  timestamp: string;
}

export interface SelectOption {
  label: string;
  value: string | number;
}
