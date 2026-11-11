# Architecture and product decisions

The Android presentation layer uses Compose and Material 3. Navigation Compose owns destinations and back-stack handling. An Android ViewModel holds editable founder state, messages and restored drafts across configuration changes. The data repository owns device-private SharedPreferences JSON. The domain layer has no Android imports: models, catalogue, templates, decimal financial arithmetic and phrasebook can be tested on the JVM.

This is a small local MVP, so dependency injection is manual. The ViewModel creates the single local repository. Preferences are appropriate for one profile, six story outputs and one latest document. A multi-business or long-history version should migrate to Room, provide schema migrations and perform larger storage operations off the UI thread. Current small save actions use synchronous commits so the interface only reports a successful save after storage returns success.

Original testimony is treated as source material. The story generator quotes it exactly and identifies its author. Other narrative sections use supplied name, category, offering and goals without claiming verified facts. Suggested operational steps are framed as a plan or assumptions. Monetary templates use BigDecimal, validate syntax and reject negative values. No currency conversion or tax calculation is implied.

The language bridge is deliberately a deterministic greeting phrasebook. Unsupported text produces a limitation message. Country coverage in business discovery and categories in opportunities are fixed local data. Each opportunity is fictional and explicitly labelled; its action opens a learning checklist rather than a network submission.

Ownership in this MVP means attribution, explicit sharing, local deletion and no automatic publication. This is not a copyright registry or a legal ownership verification mechanism. The original story remains in both the form profile and generated testimony section until the owner edits or deletes it.

The companion web application has a separate Node.js generation route. It validates size and schema, returns local templates by default, and optionally calls the OpenAI Responses API with a strict JSON schema. It handles incomplete responses, refusals, malformed requests and upstream failures. A server secret and demo access token never enter the Android app or public environment variables. The original attributed testimony is appended by code regardless of model wording.

PostgreSQL is an integration scaffold with ownership-aware tables and row-level policies, not an exposed account API. Authentication, tenant isolation tests, consent/version history, remote API access and syncing are not implemented. Terraform creates optional Amplify SSR hosting and private RDS only when enabled. There is intentionally no direct connection from public Amplify compute to private RDS; add an authenticated VPC backend before enabling persistence.

Before a production rollout: recruit language reviewers and entrepreneurs across the represented communities; test TalkBack, large font sizes, low-end devices and intermittent connectivity; implement secure authenticated sync; verify opportunities from primary sources; and establish consent, correction and deletion processes with real business owners. Keep the offline-first path available.
