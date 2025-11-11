/**
 * Circle-related type definitions
 */

export interface Circle {
  id: string;
  name: string;
  purpose: string;
  accountabilities: string[];
  parentCircleId?: string;
  organizationId: string;
  roleCount?: number;
  subCircleCount?: number;
  createdAt: string;
  updatedAt: string;
}

export interface CircleHierarchy extends Circle {
  subCircles: CircleHierarchy[];
  roles: RoleBasic[];
}

export interface RoleBasic {
  id: string;
  name: string;
  isSpecialRole: boolean;
  specialRoleType?: string;
}

export interface CreateCircleRequest {
  name: string;
  purpose: string;
  accountabilities?: string[];
  parentCircleId?: string;
  organizationId: string;
}

export interface UpdateCircleRequest {
  name?: string;
  purpose?: string;
  accountabilities?: string[];
}
