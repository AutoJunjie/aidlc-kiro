/**
 * Role-related type definitions
 */

export interface Role {
  id: string;
  name: string;
  purpose: string;
  accountabilities: string[];
  domains: Domain[];
  circleId: string;
  isSpecialRole: boolean;
  specialRoleType?: SpecialRoleType;
  assignments: RoleAssignment[];
  createdAt: string;
  updatedAt: string;
}

export interface Domain {
  id: string;
  name: string;
  description: string;
  controlType: string;
}

export interface RoleAssignment {
  id: string;
  roleId: string;
  partnerId: string;
  partnerName: string;
  assignedBy: string;
  assignedDate: string;
}

export enum SpecialRoleType {
  CIRCLE_LEAD = 'CIRCLE_LEAD',
  FACILITATOR = 'FACILITATOR',
  SECRETARY = 'SECRETARY',
  CIRCLE_REP = 'CIRCLE_REP',
}

export interface CreateRoleRequest {
  name: string;
  purpose: string;
  accountabilities?: string[];
  domains?: Omit<Domain, 'id'>[];
  circleId: string;
}

export interface UpdateRoleRequest {
  name?: string;
  purpose?: string;
  accountabilities?: string[];
  domains?: Omit<Domain, 'id'>[];
}

export interface AssignRoleRequest {
  roleId: string;
  partnerId: string;
}
