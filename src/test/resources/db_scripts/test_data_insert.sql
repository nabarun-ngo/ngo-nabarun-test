-- ==============================================================================
-- INSERT TEST DATA (ON CONFLICT DO NOTHING)
-- User profiles and roles are preserved by db_truncate.sql and are NOT re-inserted here.
-- ==============================================================================

-- Insert Projects & Activities
INSERT INTO public.projects
    (id, "name", description, code, category, status, phase,
     "startDate", "endDate", "actualEndDate",
     budget, "spentAmount", currency, "location",
     "targetBeneficiaryCount", "actualBeneficiaryCount",
     "managerId", "sponsorId", tags, metadata,
     "createdAt", "updatedAt", "version", "deletedAt")
VALUES
    ('PROJ1',
     'Test Project',
     'Test Project Description',
     'TEST_PROJ',
     'GENERAL',
     'ACTIVE',
     'PLANNING',
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP + INTERVAL '1 year',
     CURRENT_TIMESTAMP + INTERVAL '1 year',
     100000, 0, 'INR',
     'Kolkata, West Bengal',
     100, 0,
     (SELECT id FROM public.user_profiles WHERE email = 'president@nabarun.com'),
     (SELECT id FROM public.user_profiles WHERE email = 'treasurer@nabarun.com'),
     ARRAY['test', 'seed']::text[],
     '{"source": "seed"}'::jsonb,
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0,
     NULL)  -- deletedAt must be NULL for active records (non-null = soft deleted)
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.activities
    (id, "projectId", "name", description, "scale", "type", status, priority,
     "startDate", "endDate", "actualStartDate", "actualEndDate",
     "location", venue, "assignedTo", "organizerId", "parentActivityId",
     "expectedParticipants", "actualParticipants",
     "estimatedCost", "actualCost", currency,
     tags, metadata,
     "createdAt", "updatedAt", "version", "deletedAt")
VALUES
    ('EVT123',
     'PROJ1',
     'Test Activity',
     'Test Activity Description',
     'MEDIUM',
     'EVENT',
     'ACTIVE',
     'MEDIUM',
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP + INTERVAL '6 months',
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP + INTERVAL '6 months',
     'Kolkata, West Bengal',
     'Community Hall',
     (SELECT id FROM public.user_profiles WHERE email = 'member@nabarun.com'),
     (SELECT id FROM public.user_profiles WHERE email = 'president@nabarun.com'),
     NULL,  -- parentActivityId: NULL means top-level activity (self-ref FK, cannot be set without a parent)
     50, 0,
     5000, 0, 'INR',
     ARRAY['test', 'seed']::text[],
     '{"source": "seed"}'::jsonb,
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0,
     NULL)  -- deletedAt must be NULL for active records
ON CONFLICT (id) DO NOTHING;

-- Insert Accounts (accountHolderId and accountHolderName resolved at runtime from user_profiles)
INSERT INTO public.accounts
    (id, "name", "type", balance, currency, status, description,
     "accountHolderName", "accountHolderId",
     "activatedOn", "bankDetail", "upiDetail", "createdById",
     "createdAt", "updatedAt", "version", "deletedAt")
SELECT
    'ACC_CASHIER_DONATION',
    'Cashier Donation Account',
    'DONATION', 0, 'INR', 'ACTIVE', NULL,
    "firstName" || ' ' || "lastName",
    id,
    CURRENT_TIMESTAMP, NULL, NULL, NULL,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, NULL
FROM public.user_profiles WHERE email = 'cashier@nabarun.com'
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.accounts
    (id, "name", "type", balance, currency, status, description,
     "accountHolderName", "accountHolderId",
     "activatedOn", "bankDetail", "upiDetail", "createdById",
     "createdAt", "updatedAt", "version", "deletedAt")
SELECT
    'ACC_ASST_CASHIER_DONATION',
    'Assistant Cashier Donation Account',
    'DONATION', 0, 'INR', 'ACTIVE', NULL,
    "firstName" || ' ' || "lastName",
    id,
    CURRENT_TIMESTAMP, NULL, NULL, NULL,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, NULL
FROM public.user_profiles WHERE email = 'assistantcashier@nabarun.com'
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.accounts
    (id, "name", "type", balance, currency, status, description,
     "accountHolderName", "accountHolderId",
     "activatedOn", "bankDetail", "upiDetail", "createdById",
     "createdAt", "updatedAt", "version", "deletedAt")
SELECT
    'ACC_TREASURER_PRINCIPAL',
    'Treasurer Principal Account',
    'PRINCIPAL', 0, 'INR', 'ACTIVE', NULL,
    "firstName" || ' ' || "lastName",
    id,
    CURRENT_TIMESTAMP, NULL, NULL, NULL,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, NULL
FROM public.user_profiles WHERE email = 'treasurer@nabarun.com'
ON CONFLICT (id) DO NOTHING;
