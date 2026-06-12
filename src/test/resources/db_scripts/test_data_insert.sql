-- ==============================================================================
-- INSERT TEST DATA — idempotent, transactional, with logging
--
-- Rules:
--   • Runs as a single atomic transaction (DO block).  Any error rolls back all.
--   • Each record is only inserted when it does not already exist (IF NOT EXISTS).
--   • RAISE NOTICE lines surface progress in psql / CI logs.
--   • RAISE EXCEPTION aborts and rolls back on missing prerequisites or errors.
--
-- Prerequisites (must exist before this script runs):
--   • user_profiles rows for every email listed in the DECLARE section.
--   • Run db_truncate.sql first if a clean slate is required.
-- ==============================================================================

DO $$
DECLARE
    v_manager_id    TEXT;   -- president@nabarun.com
    v_sponsor_id    TEXT;   -- treasurer@nabarun.com  (also used as v_treasurer_id)
    v_member_id     TEXT;   -- member@nabarun.com
    v_cashier_id    TEXT;   -- cashier@nabarun.com
    v_asst_id       TEXT;   -- assistantcashier@nabarun.com
    v_cashier_name  TEXT;
    v_asst_name     TEXT;
    v_treasurer_name TEXT;
BEGIN
    RAISE NOTICE '[TEST DATA] ===== Starting test data insertion at % =====', NOW();

    -- ------------------------------------------------------------------
    -- 1. Resolve required user IDs from user_profiles
    -- ------------------------------------------------------------------
    RAISE NOTICE '[TEST DATA] Resolving prerequisite user IDs ...';

    SELECT id INTO v_manager_id
        FROM public.user_profiles WHERE email = 'president@nabarun.com';
    SELECT id INTO v_sponsor_id
        FROM public.user_profiles WHERE email = 'treasurer@nabarun.com';
    SELECT id INTO v_member_id
        FROM public.user_profiles WHERE email = 'member@nabarun.com';
    SELECT id,  "firstName" || ' ' || "lastName"
        INTO v_cashier_id, v_cashier_name
        FROM public.user_profiles WHERE email = 'cashier@nabarun.com';
    SELECT id,  "firstName" || ' ' || "lastName"
        INTO v_asst_id, v_asst_name
        FROM public.user_profiles WHERE email = 'assistantcashier@nabarun.com';
    SELECT "firstName" || ' ' || "lastName"
        INTO v_treasurer_name
        FROM public.user_profiles WHERE email = 'treasurer@nabarun.com';

    -- Validate — abort early so nothing gets partially inserted
    IF v_manager_id  IS NULL THEN
        RAISE EXCEPTION '[TEST DATA] PREREQUISITE MISSING: president@nabarun.com not found in user_profiles';
    END IF;
    IF v_sponsor_id  IS NULL THEN
        RAISE EXCEPTION '[TEST DATA] PREREQUISITE MISSING: treasurer@nabarun.com not found in user_profiles';
    END IF;
    IF v_member_id   IS NULL THEN
        RAISE EXCEPTION '[TEST DATA] PREREQUISITE MISSING: member@nabarun.com not found in user_profiles';
    END IF;
    IF v_cashier_id  IS NULL THEN
        RAISE EXCEPTION '[TEST DATA] PREREQUISITE MISSING: cashier@nabarun.com not found in user_profiles';
    END IF;
    IF v_asst_id     IS NULL THEN
        RAISE EXCEPTION '[TEST DATA] PREREQUISITE MISSING: assistantcashier@nabarun.com not found in user_profiles';
    END IF;

    RAISE NOTICE '[TEST DATA] All prerequisite users resolved OK';
    RAISE NOTICE '[TEST DATA]   manager   = %', v_manager_id;
    RAISE NOTICE '[TEST DATA]   sponsor   = %', v_sponsor_id;
    RAISE NOTICE '[TEST DATA]   member    = %', v_member_id;
    RAISE NOTICE '[TEST DATA]   cashier   = %', v_cashier_id;
    RAISE NOTICE '[TEST DATA]   asst      = %', v_asst_id;

    -- ------------------------------------------------------------------
    -- 2. Project PROJ1
    -- ------------------------------------------------------------------
    RAISE NOTICE '[TEST DATA] Checking projects id=PROJ1 ...';
    IF NOT EXISTS (SELECT 1 FROM public.projects WHERE id = 'PROJ1') THEN
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
             v_manager_id,
             v_sponsor_id,
             ARRAY['test', 'seed']::text[],
             '{"source": "seed"}'::jsonb,
             CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0,
             NULL);
        RAISE NOTICE '[TEST DATA] [INSERTED] projects id=PROJ1';
    ELSE
        RAISE NOTICE '[TEST DATA] [SKIPPED]  projects id=PROJ1 — already exists';
    END IF;

    -- ------------------------------------------------------------------
    -- 3. Activity EVT123  (depends on PROJ1)
    -- ------------------------------------------------------------------
    RAISE NOTICE '[TEST DATA] Checking activities id=EVT123 ...';
    IF NOT EXISTS (SELECT 1 FROM public.activities WHERE id = 'EVT123') THEN
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
             v_member_id,
             v_manager_id,
             NULL,
             50, 0,
             5000, 0, 'INR',
             ARRAY['test', 'seed']::text[],
             '{"source": "seed"}'::jsonb,
             CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0,
             NULL);
        RAISE NOTICE '[TEST DATA] [INSERTED] activities id=EVT123';
    ELSE
        RAISE NOTICE '[TEST DATA] [SKIPPED]  activities id=EVT123 — already exists';
    END IF;

    -- ------------------------------------------------------------------
    -- 4. Account ACC_CASHIER_DONATION
    -- ------------------------------------------------------------------
    RAISE NOTICE '[TEST DATA] Checking accounts id=ACC_CASHIER_DONATION ...';
    IF NOT EXISTS (SELECT 1 FROM public.accounts WHERE id = 'ACC_CASHIER_DONATION') THEN
        INSERT INTO public.accounts
            (id, "name", "type", balance, currency, status, description,
             "accountHolderName", "accountHolderId",
             "activatedOn", "bankDetail", "upiDetail", "createdById",
             "createdAt", "updatedAt", "version", "deletedAt")
        VALUES
            ('ACC_CASHIER_DONATION',
             'Cashier Donation Account',
             'DONATION', 0, 'INR', 'ACTIVE', NULL,
             v_cashier_name,
             v_cashier_id,
             CURRENT_TIMESTAMP, NULL, NULL, NULL,
             CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, NULL);
        RAISE NOTICE '[TEST DATA] [INSERTED] accounts id=ACC_CASHIER_DONATION';
    ELSE
        RAISE NOTICE '[TEST DATA] [SKIPPED]  accounts id=ACC_CASHIER_DONATION — already exists';
    END IF;

    -- ------------------------------------------------------------------
    -- 5. Account ACC_ASST_CASHIER_DONATION
    -- ------------------------------------------------------------------
    RAISE NOTICE '[TEST DATA] Checking accounts id=ACC_ASST_CASHIER_DONATION ...';
    IF NOT EXISTS (SELECT 1 FROM public.accounts WHERE id = 'ACC_ASST_CASHIER_DONATION') THEN
        INSERT INTO public.accounts
            (id, "name", "type", balance, currency, status, description,
             "accountHolderName", "accountHolderId",
             "activatedOn", "bankDetail", "upiDetail", "createdById",
             "createdAt", "updatedAt", "version", "deletedAt")
        VALUES
            ('ACC_ASST_CASHIER_DONATION',
             'Assistant Cashier Donation Account',
             'DONATION', 0, 'INR', 'ACTIVE', NULL,
             v_asst_name,
             v_asst_id,
             CURRENT_TIMESTAMP, NULL, NULL, NULL,
             CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, NULL);
        RAISE NOTICE '[TEST DATA] [INSERTED] accounts id=ACC_ASST_CASHIER_DONATION';
    ELSE
        RAISE NOTICE '[TEST DATA] [SKIPPED]  accounts id=ACC_ASST_CASHIER_DONATION — already exists';
    END IF;

    -- ------------------------------------------------------------------
    -- 6. Account ACC_TREASURER_PRINCIPAL
    -- ------------------------------------------------------------------
    RAISE NOTICE '[TEST DATA] Checking accounts id=ACC_TREASURER_PRINCIPAL ...';
    IF NOT EXISTS (SELECT 1 FROM public.accounts WHERE id = 'ACC_TREASURER_PRINCIPAL') THEN
        INSERT INTO public.accounts
            (id, "name", "type", balance, currency, status, description,
             "accountHolderName", "accountHolderId",
             "activatedOn", "bankDetail", "upiDetail", "createdById",
             "createdAt", "updatedAt", "version", "deletedAt")
        VALUES
            ('ACC_TREASURER_PRINCIPAL',
             'Treasurer Principal Account',
             'PRINCIPAL', 0, 'INR', 'ACTIVE', NULL,
             v_treasurer_name,
             v_sponsor_id,
             CURRENT_TIMESTAMP, NULL, NULL, NULL,
             CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, NULL);
        RAISE NOTICE '[TEST DATA] [INSERTED] accounts id=ACC_TREASURER_PRINCIPAL';
    ELSE
        RAISE NOTICE '[TEST DATA] [SKIPPED]  accounts id=ACC_TREASURER_PRINCIPAL — already exists';
    END IF;

    -- ------------------------------------------------------------------
    RAISE NOTICE '[TEST DATA] ===== Test data insertion complete at % =====', NOW();

EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION '[TEST DATA] FAILED (SQLSTATE=%) — transaction rolled back: %', SQLSTATE, SQLERRM;
END;
$$;
