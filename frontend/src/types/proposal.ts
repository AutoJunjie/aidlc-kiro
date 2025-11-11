/**
 * Proposal-related type definitions
 */

export interface Proposal {
  id: string;
  title: string;
  tension: Tension;
  proposalType: ProposalType;
  circleId: string;
  proposerId: string;
  status: ProposalStatus;
  createdDate: string;
  submittedDate?: string;
  decisionHistory: DecisionEvent[];
}

export interface Tension {
  description: string;
  currentState: string;
  desiredState: string;
  examples: string[];
  context?: string;
}

export enum ProposalType {
  ROLE_MODIFICATION = 'ROLE_MODIFICATION',
  POLICY_ADJUSTMENT = 'POLICY_ADJUSTMENT',
  CIRCLE_STRUCTURE_CHANGE = 'CIRCLE_STRUCTURE_CHANGE',
  PROCESS_OPTIMIZATION = 'PROCESS_OPTIMIZATION',
}

export enum ProposalStatus {
  DRAFT = 'DRAFT',
  SUBMITTED = 'SUBMITTED',
  PROPOSAL_STAGE = 'PROPOSAL_STAGE',
  CLARIFICATION_STAGE = 'CLARIFICATION_STAGE',
  REACTION_STAGE = 'REACTION_STAGE',
  AMEND_STAGE = 'AMEND_STAGE',
  OBJECTION_STAGE = 'OBJECTION_STAGE',
  INTEGRATION_STAGE = 'INTEGRATION_STAGE',
  APPROVED = 'APPROVED',
  APPLIED = 'APPLIED',
  WITHDRAWN = 'WITHDRAWN',
  REJECTED = 'REJECTED',
}

export interface DecisionEvent {
  id: string;
  eventType: string;
  timestamp: string;
  actorId?: string;
  content: string;
}

export interface CreateProposalRequest {
  title: string;
  tension: Tension;
  proposalType: ProposalType;
  circleId: string;
}
