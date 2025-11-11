-- Initial database schema for XHolacracy MVP
-- Version: 1.0
-- Description: Complete schema for organizations, circles, roles, partners, proposals, and meetings

-- ============================================================================
-- ORGANIZATIONS TABLE
-- ============================================================================
CREATE TABLE organizations (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    anchor_circle_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_organizations_name ON organizations(name);

-- ============================================================================
-- CIRCLES TABLE
-- ============================================================================
CREATE TABLE circles (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    purpose TEXT,
    parent_circle_id VARCHAR(255),
    organization_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE
);

CREATE INDEX idx_circles_parent_id ON circles(parent_circle_id);
CREATE INDEX idx_circles_organization_id ON circles(organization_id);
CREATE INDEX idx_circles_name ON circles(name);

-- ============================================================================
-- CIRCLE ACCOUNTABILITIES TABLE
-- ============================================================================
CREATE TABLE circle_accountabilities (
    circle_id VARCHAR(255) NOT NULL,
    accountability TEXT NOT NULL,
    FOREIGN KEY (circle_id) REFERENCES circles(id) ON DELETE CASCADE
);

CREATE INDEX idx_circle_accountabilities_circle_id ON circle_accountabilities(circle_id);

-- ============================================================================
-- PARTNERS TABLE
-- ============================================================================
CREATE TABLE partners (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_partners_email ON partners(email);
CREATE INDEX idx_partners_name ON partners(name);

-- ============================================================================
-- ROLES TABLE
-- ============================================================================
CREATE TABLE roles (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    purpose TEXT,
    circle_id VARCHAR(255) NOT NULL,
    is_special_role BOOLEAN NOT NULL DEFAULT FALSE,
    special_role_type VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (circle_id) REFERENCES circles(id) ON DELETE CASCADE
);

CREATE INDEX idx_roles_circle_id ON roles(circle_id);
CREATE INDEX idx_roles_special_type ON roles(special_role_type) WHERE is_special_role = TRUE;
CREATE INDEX idx_roles_name ON roles(name);

-- ============================================================================
-- ROLE ACCOUNTABILITIES TABLE
-- ============================================================================
CREATE TABLE role_accountabilities (
    role_id VARCHAR(255) NOT NULL,
    accountability TEXT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE INDEX idx_role_accountabilities_role_id ON role_accountabilities(role_id);

-- ============================================================================
-- DOMAINS TABLE
-- ============================================================================
CREATE TABLE domains (
    id BIGSERIAL PRIMARY KEY,
    role_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    control_type VARCHAR(50) NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE INDEX idx_domains_role_id ON domains(role_id);

-- ============================================================================
-- ROLE ASSIGNMENTS TABLE
-- ============================================================================
CREATE TABLE role_assignments (
    id BIGSERIAL PRIMARY KEY,
    role_id VARCHAR(255) NOT NULL,
    partner_id VARCHAR(255) NOT NULL,
    assigned_by VARCHAR(255) NOT NULL,
    assigned_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (partner_id) REFERENCES partners(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_by) REFERENCES partners(id)
);

CREATE INDEX idx_role_assignments_role_id ON role_assignments(role_id);
CREATE INDEX idx_role_assignments_partner_id ON role_assignments(partner_id);
CREATE INDEX idx_role_assignments_composite ON role_assignments(role_id, partner_id);

-- ============================================================================
-- PROPOSALS TABLE
-- ============================================================================
CREATE TABLE proposals (
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    tension_description TEXT,
    tension_current_state TEXT,
    tension_desired_state TEXT,
    tension_context TEXT,
    proposal_type VARCHAR(50) NOT NULL,
    circle_id VARCHAR(255) NOT NULL,
    proposer_id VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    submitted_date TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (circle_id) REFERENCES circles(id) ON DELETE CASCADE,
    FOREIGN KEY (proposer_id) REFERENCES partners(id)
);

CREATE INDEX idx_proposals_circle_id ON proposals(circle_id);
CREATE INDEX idx_proposals_proposer_id ON proposals(proposer_id);
CREATE INDEX idx_proposals_status ON proposals(status);
CREATE INDEX idx_proposals_created_date ON proposals(created_date DESC);
CREATE INDEX idx_proposals_title ON proposals(title);

-- ============================================================================
-- TENSION EXAMPLES TABLE
-- ============================================================================
CREATE TABLE tension_examples (
    proposal_id VARCHAR(255) NOT NULL,
    example TEXT NOT NULL,
    FOREIGN KEY (proposal_id) REFERENCES proposals(id) ON DELETE CASCADE
);

CREATE INDEX idx_tension_examples_proposal_id ON tension_examples(proposal_id);

-- ============================================================================
-- DECISION EVENTS TABLE
-- ============================================================================
CREATE TABLE decision_events (
    id BIGSERIAL PRIMARY KEY,
    proposal_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actor_id VARCHAR(255),
    content TEXT,
    FOREIGN KEY (proposal_id) REFERENCES proposals(id) ON DELETE CASCADE,
    FOREIGN KEY (actor_id) REFERENCES partners(id)
);

CREATE INDEX idx_decision_events_proposal_id ON decision_events(proposal_id);
CREATE INDEX idx_decision_events_timestamp ON decision_events(timestamp DESC);

-- ============================================================================
-- QUESTIONS TABLE (Clarification Stage)
-- ============================================================================
CREATE TABLE questions (
    id BIGSERIAL PRIMARY KEY,
    proposal_id VARCHAR(255) NOT NULL,
    asker_id VARCHAR(255) NOT NULL,
    question TEXT NOT NULL,
    answer TEXT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proposal_id) REFERENCES proposals(id) ON DELETE CASCADE,
    FOREIGN KEY (asker_id) REFERENCES partners(id)
);

CREATE INDEX idx_questions_proposal_id ON questions(proposal_id);

-- ============================================================================
-- REACTIONS TABLE (Reaction Stage)
-- ============================================================================
CREATE TABLE reactions (
    id BIGSERIAL PRIMARY KEY,
    proposal_id VARCHAR(255) NOT NULL,
    reactor_id VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    order_index INTEGER NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proposal_id) REFERENCES proposals(id) ON DELETE CASCADE,
    FOREIGN KEY (reactor_id) REFERENCES partners(id)
);

CREATE INDEX idx_reactions_proposal_id ON reactions(proposal_id);

-- ============================================================================
-- AMENDMENTS TABLE (Amend Stage)
-- ============================================================================
CREATE TABLE amendments (
    id BIGSERIAL PRIMARY KEY,
    proposal_id VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proposal_id) REFERENCES proposals(id) ON DELETE CASCADE
);

CREATE INDEX idx_amendments_proposal_id ON amendments(proposal_id);

-- ============================================================================
-- OBJECTIONS TABLE (Objection Stage)
-- ============================================================================
CREATE TABLE objections (
    id BIGSERIAL PRIMARY KEY,
    proposal_id VARCHAR(255) NOT NULL,
    objector_id VARCHAR(255) NOT NULL,
    reasoning TEXT NOT NULL,
    is_valid BOOLEAN NOT NULL DEFAULT FALSE,
    validated_by VARCHAR(255),
    reduces_capability BOOLEAN NOT NULL DEFAULT FALSE,
    limits_accountability BOOLEAN NOT NULL DEFAULT FALSE,
    problem_not_exist_without BOOLEAN NOT NULL DEFAULT FALSE,
    causes_harm BOOLEAN NOT NULL DEFAULT FALSE,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proposal_id) REFERENCES proposals(id) ON DELETE CASCADE,
    FOREIGN KEY (objector_id) REFERENCES partners(id),
    FOREIGN KEY (validated_by) REFERENCES partners(id)
);

CREATE INDEX idx_objections_proposal_id ON objections(proposal_id);
CREATE INDEX idx_objections_is_valid ON objections(is_valid);

-- ============================================================================
-- VOTES TABLE
-- ============================================================================
CREATE TABLE votes (
    id BIGSERIAL PRIMARY KEY,
    proposal_id VARCHAR(255) NOT NULL,
    voter_id VARCHAR(255) NOT NULL,
    vote_type VARCHAR(20) NOT NULL,
    comment TEXT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proposal_id) REFERENCES proposals(id) ON DELETE CASCADE,
    FOREIGN KEY (voter_id) REFERENCES partners(id)
);

CREATE INDEX idx_votes_proposal_id ON votes(proposal_id);
CREATE INDEX idx_votes_voter_id ON votes(voter_id);

-- ============================================================================
-- GOVERNANCE MEETINGS TABLE
-- ============================================================================
CREATE TABLE governance_meetings (
    id VARCHAR(255) PRIMARY KEY,
    circle_id VARCHAR(255) NOT NULL,
    scheduled_date TIMESTAMP NOT NULL,
    duration BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    facilitator_id VARCHAR(255),
    secretary_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (circle_id) REFERENCES circles(id) ON DELETE CASCADE,
    FOREIGN KEY (facilitator_id) REFERENCES partners(id),
    FOREIGN KEY (secretary_id) REFERENCES partners(id)
);

CREATE INDEX idx_meetings_circle_id ON governance_meetings(circle_id);
CREATE INDEX idx_meetings_scheduled_date ON governance_meetings(scheduled_date DESC);
CREATE INDEX idx_meetings_status ON governance_meetings(status);
CREATE INDEX idx_meetings_facilitator_id ON governance_meetings(facilitator_id);
CREATE INDEX idx_meetings_secretary_id ON governance_meetings(secretary_id);

-- ============================================================================
-- MEETING PARTICIPANTS TABLE
-- ============================================================================
CREATE TABLE meeting_participants (
    meeting_id VARCHAR(255) NOT NULL,
    partner_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (meeting_id, partner_id),
    FOREIGN KEY (meeting_id) REFERENCES governance_meetings(id) ON DELETE CASCADE,
    FOREIGN KEY (partner_id) REFERENCES partners(id)
);

CREATE INDEX idx_meeting_participants_meeting_id ON meeting_participants(meeting_id);
CREATE INDEX idx_meeting_participants_partner_id ON meeting_participants(partner_id);

-- ============================================================================
-- MEETING AGENDA ITEMS TABLE
-- ============================================================================
CREATE TABLE meeting_agenda_items (
    id BIGSERIAL PRIMARY KEY,
    meeting_id VARCHAR(255) NOT NULL,
    proposal_id VARCHAR(255),
    order_index INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (meeting_id) REFERENCES governance_meetings(id) ON DELETE CASCADE,
    FOREIGN KEY (proposal_id) REFERENCES proposals(id)
);

CREATE INDEX idx_agenda_items_meeting_id ON meeting_agenda_items(meeting_id);
CREATE INDEX idx_agenda_items_proposal_id ON meeting_agenda_items(proposal_id);

-- ============================================================================
-- MEETING RECORDS TABLE
-- ============================================================================
CREATE TABLE meeting_records (
    id BIGSERIAL PRIMARY KEY,
    meeting_id VARCHAR(255) NOT NULL UNIQUE,
    check_in_notes TEXT,
    additional_notes TEXT,
    closing_notes TEXT,
    FOREIGN KEY (meeting_id) REFERENCES governance_meetings(id) ON DELETE CASCADE
);

CREATE INDEX idx_meeting_records_meeting_id ON meeting_records(meeting_id);

-- ============================================================================
-- PROPOSAL OUTCOMES TABLE
-- ============================================================================
CREATE TABLE proposal_outcomes (
    id BIGSERIAL PRIMARY KEY,
    meeting_record_id BIGINT NOT NULL,
    proposal_id VARCHAR(255) NOT NULL,
    outcome VARCHAR(50) NOT NULL,
    notes TEXT,
    FOREIGN KEY (meeting_record_id) REFERENCES meeting_records(id) ON DELETE CASCADE,
    FOREIGN KEY (proposal_id) REFERENCES proposals(id)
);

CREATE INDEX idx_proposal_outcomes_meeting_record_id ON proposal_outcomes(meeting_record_id);
CREATE INDEX idx_proposal_outcomes_proposal_id ON proposal_outcomes(proposal_id);

-- ============================================================================
-- TRIGGERS FOR UPDATED_AT
-- ============================================================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_organizations_updated_at BEFORE UPDATE ON organizations
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_circles_updated_at BEFORE UPDATE ON circles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_partners_updated_at BEFORE UPDATE ON partners
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_roles_updated_at BEFORE UPDATE ON roles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_proposals_updated_at BEFORE UPDATE ON proposals
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_meetings_updated_at BEFORE UPDATE ON governance_meetings
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
