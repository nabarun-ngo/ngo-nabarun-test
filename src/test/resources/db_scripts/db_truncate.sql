-- Preserve accounts belonging to TestUser profiles
DELETE FROM public.accounts WHERE "accountHolderId" NOT IN (SELECT id FROM public.user_profiles WHERE "lastName" = 'TestUser');
TRUNCATE TABLE public.donations CASCADE;
TRUNCATE TABLE public.projects CASCADE;
TRUNCATE TABLE public.activities CASCADE;
TRUNCATE TABLE public.activity_expenses CASCADE;
TRUNCATE TABLE public.addresses CASCADE;
TRUNCATE TABLE public.api_keys CASCADE;
TRUNCATE TABLE public.audit_logs CASCADE;
TRUNCATE TABLE public.beneficiaries CASCADE;
TRUNCATE TABLE public."comments" CASCADE;
TRUNCATE TABLE public.document_mappings CASCADE;
TRUNCATE TABLE public.document_references CASCADE;
TRUNCATE TABLE public.earnings CASCADE;
TRUNCATE TABLE public.expenses CASCADE;
TRUNCATE TABLE public.goals CASCADE;
TRUNCATE TABLE public.links CASCADE;
TRUNCATE TABLE public.meetings CASCADE;
TRUNCATE TABLE public.milestones CASCADE;
TRUNCATE TABLE public.notices CASCADE;
TRUNCATE TABLE public.notifications CASCADE;
TRUNCATE TABLE public.oauth_tokens CASCADE;
TRUNCATE TABLE public.phone_numbers CASCADE;
TRUNCATE TABLE public.project_risks CASCADE;
TRUNCATE TABLE public.project_team_members CASCADE;
TRUNCATE TABLE public.reports CASCADE;
TRUNCATE TABLE public.task_assignments CASCADE;
TRUNCATE TABLE public.transactions CASCADE;
TRUNCATE TABLE public.user_fcm_tokens CASCADE;
TRUNCATE TABLE public.user_notifications CASCADE;
-- Preserve roles belonging to TestUser profiles
DELETE FROM public.user_roles WHERE "userId" NOT IN (SELECT id FROM public.user_profiles WHERE "lastName" = 'TestUser');
TRUNCATE TABLE public.workflow_tasks CASCADE;
TRUNCATE TABLE public.workflow_steps CASCADE;
TRUNCATE TABLE public.workflow_instances CASCADE;
DELETE FROM public.user_profiles WHERE "lastName" != 'TestUser'; -- cascades via ON DELETE CASCADE FK constraints (Prisma-generated)
