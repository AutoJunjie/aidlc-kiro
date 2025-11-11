/**
 * Meeting-related type definitions
 */

export interface GovernanceMeeting {
  id: string;
  circleId: string;
  scheduledDate: string;
  duration: number; // in minutes
  status: MeetingStatus;
  facilitatorId: string;
  secretaryId: string;
  participants: string[];
  agenda: AgendaItem[];
  createdAt: string;
  updatedAt: string;
}

export enum MeetingStatus {
  SCHEDULED = 'SCHEDULED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
}

export interface AgendaItem {
  id: string;
  proposalId: string;
  proposalTitle: string;
  orderIndex: number;
  status: AgendaItemStatus;
}

export enum AgendaItemStatus {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
}

export interface CreateMeetingRequest {
  circleId: string;
  scheduledDate: string;
  duration: number;
  facilitatorId?: string;
  secretaryId?: string;
}

export interface UpdateMeetingRequest {
  scheduledDate?: string;
  duration?: number;
}
