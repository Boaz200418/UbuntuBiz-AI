-- Future authenticated persistence; not used by the offline Android MVP.
BEGIN;
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE TABLE IF NOT EXISTS accounts (id uuid PRIMARY KEY DEFAULT gen_random_uuid(),auth_subject text NOT NULL UNIQUE,created_at timestamptz NOT NULL DEFAULT now());
CREATE TABLE IF NOT EXISTS businesses (id uuid PRIMARY KEY DEFAULT gen_random_uuid(),owner_id uuid NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,name text NOT NULL,country text NOT NULL,category text NOT NULL,products text NOT NULL,is_published boolean NOT NULL DEFAULT false,created_at timestamptz NOT NULL DEFAULT now());
CREATE TABLE IF NOT EXISTS stories (id uuid PRIMARY KEY DEFAULT gen_random_uuid(),business_id uuid NOT NULL REFERENCES businesses(id) ON DELETE CASCADE,author_id uuid NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,author_name text NOT NULL,original_text text NOT NULL,generated_sections jsonb NOT NULL DEFAULT '[]'::jsonb,publication_consent boolean NOT NULL DEFAULT false,created_at timestamptz NOT NULL DEFAULT now());
CREATE TABLE IF NOT EXISTS documents (id uuid PRIMARY KEY DEFAULT gen_random_uuid(),owner_id uuid NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,document_kind text NOT NULL,sections jsonb NOT NULL,generation_mode text NOT NULL CHECK(generation_mode IN ('offline-template','openai-structured-output')),created_at timestamptz NOT NULL DEFAULT now());
CREATE INDEX IF NOT EXISTS businesses_owner_idx ON businesses(owner_id);
CREATE INDEX IF NOT EXISTS stories_author_idx ON stories(author_id);
CREATE INDEX IF NOT EXISTS documents_owner_idx ON documents(owner_id);
ALTER TABLE businesses ENABLE ROW LEVEL SECURITY;
ALTER TABLE stories ENABLE ROW LEVEL SECURITY;
ALTER TABLE documents ENABLE ROW LEVEL SECURITY;
-- API must authenticate and set app.account_id per transaction using a non-owner role.
DO $$ BEGIN
 IF NOT EXISTS (SELECT 1 FROM pg_policies WHERE tablename='businesses' AND policyname='owner_business') THEN
 CREATE POLICY owner_business ON businesses USING (owner_id=nullif(current_setting('app.account_id',true),'')::uuid) WITH CHECK (owner_id=nullif(current_setting('app.account_id',true),'')::uuid);
 CREATE POLICY owner_story ON stories USING (author_id=nullif(current_setting('app.account_id',true),'')::uuid) WITH CHECK (author_id=nullif(current_setting('app.account_id',true),'')::uuid AND EXISTS (SELECT 1 FROM businesses b WHERE b.id=business_id AND b.owner_id=author_id));
 CREATE POLICY owner_document ON documents USING (owner_id=nullif(current_setting('app.account_id',true),'')::uuid) WITH CHECK (owner_id=nullif(current_setting('app.account_id',true),'')::uuid);
 END IF;
END $$;
COMMIT;
