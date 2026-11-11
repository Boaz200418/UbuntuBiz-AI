# UbuntuBiz AI

**Where African Stories Become African Opportunities.**

A native Android capstone MVP for turning African entrepreneurs' experience, skills and ideas into practical business drafts. Kotlin, Jetpack Compose, Material 3, private local persistence and deterministic offline generation. No account, subscription or API key is needed for the Android app.

The download also includes a prebuilt development APK at `apk/UbuntuBiz-AI-debug.apk`. Extract the archive before installing it.

## Open and build the Android application

Requirements: Android Studio Ladybug or newer, **JDK 17**, Android SDK Platform **35**, Build Tools **35.0.0**. Minimum supported device: Android **8.0 / API 26**. Internet is needed once to download build dependencies; the installed app works offline.

1. Extract the project and open the `ubuntubiz-ai` folder in Android Studio.
2. Let Gradle sync. Install SDK components when Android Studio prompts.
3. Choose an Android device or emulator and press **Run**.

Command line, from the project root:

```bash
# Linux/macOS
chmod +x gradlew
./gradlew testDebugUnitTest lintDebug assembleDebug

# Windows PowerShell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug
```

Set `ANDROID_HOME` to your SDK directory, or let Android Studio generate `local.properties` with `sdk.dir=...`. Set `JAVA_HOME` to a full JDK 17 installation, not a JRE.

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

The debug APK is installable and signed with a development certificate. For store distribution, use Android Studio **Generate Signed App Bundle / APK**, create your own signing key, keep it outside this project and increment the version code. No production signing credentials are included.

## Functional Android workflows

| Area | Included behaviour |
| --- | --- |
| Home | African-inspired forest green, gold, cream and white identity; vector illustration; featured entrepreneurs and recent demo opportunity; working entry points |
| My Story | Validated founder form; entrepreneur story, biography, business introduction, caption, product description and marketing message |
| Launch | Business plan, company profile, proposal, quotation, startup budget and marketing strategy; structured previews |
| Quotations & budgets | User-entered currency and amounts; decimal arithmetic; negative, malformed and excessive-precision values rejected; no invented tax |
| Language Bridge | English, Shona, isiXhosa, isiZulu, Swahili, French and Portuguese; source/target selection; explicitly labelled greeting phrasebook |
| Discover | Five fictional businesses across Zimbabwe, South Africa, Kenya, Nigeria and Ghana; search, country filters, details and honest demo contact preview |
| Opportunities | Seven fictional funding, grant, mentorship, training, supplier, tender and competition listings; eligibility and demo preparation checklist |
| Profile | Editable founder details and skills; initials image placeholder; explicit local save; deletion with confirmation |
| Ownership | Original story quoted verbatim and attributed; no automatic publication; selectable draft text, Android share sheet and `.txt` export |

The five bottom tabs are Home, My Story, Launch, Discover and Profile. The top bar opens Language Bridge and Opportunities. All main workflows run offline. Demo contact actions explain why fictional listings have no contact details. Opportunity actions display a checklist and never claim to submit an application.

## Local data and demo boundaries

Profile and generated content are stored in app-private Android SharedPreferences as JSON. A save or generation action persists the profile. Six generated story sections and the **latest** business document survive app restarts. Export a document before generating its replacement. Unsaved form edits and ungenerated pricing inputs are session-only. Android backup is disabled. This app does not add separate encryption; use a trusted device and share only information you choose to retain.

The Android manifest does **not** request Internet permission. The Android build has no remote AI, analytics, cloud account sync, real directory, real funding feed or payment functionality. The translation bridge supports the displayed sample greeting only, plus unchanged text when source and target are the same language. Arbitrary text is explicitly marked unsupported; it is never presented as a translation. Phrasebook wording is illustrative and should be reviewed by fluent speakers before real use.

Generated documents are local examples for review, not verified legal, tax or financial advice. Business results, credentials, funding offers and market evidence are not invented. Founders retain attribution for their original testimony. There is no automatic publishing, and deleting local data does not delete exported copies.

## Optional requested web/cloud stack

This project also includes a **companion scaffold**, separate from the complete offline Android MVP:

| Technology | Implementation |
| --- | --- |
| Next.js + React | Responsive founder studio, template previews, browser-local save/load, text download |
| Node.js | Next.js Node route at `POST /api/generate`, validated requests, error and refusal handling |
| OpenAI API | Optional Responses API integration; disabled by default; server-only API key |
| Structured outputs | Strict JSON schema and Zod validation for business documents; original testimony appended deterministically |
| PostgreSQL | Accounts, businesses, original stories and documents schema; local Docker service and migration script |
| AWS Amplify | Monorepo build specification for `web/`; Terraform SSR hosting scaffold |
| Terraform | Opt-in Amplify and optional private RDS PostgreSQL resources; **default flags create nothing** |

The Android app intentionally does not connect to this companion. PostgreSQL provides a schema and migration path, not a finished cloud account system or live publishing backend. Production authentication, authorised data APIs, cloud sync, verified translations and real opportunity ingestion are future integration work.

### Run the companion

Use Node.js **22 LTS** or newer:

```bash
cd web
npm ci
npm run test
npm run build
npm run start
# development: npm run dev
```

Open `http://localhost:3000`. The companion uses local templates by default. Its form sends entered details to its local Node server; the default server does not store them. Browser saving is explicit. Unlike the installed Android app, the companion needs its local server and is not an installable offline PWA.

For optional live AI, copy `.env.example` to `.env.local`, set `OPENAI_ENABLED=true`, a valid `OPENAI_API_KEY`, an available structured-output model in `OPENAI_MODEL`, and a strong `MENTOR_ACCESS_TOKEN`. Restart the server, expand the access panel and enter that private token. No key or token is embedded in browser JavaScript or Android. Live requests send the founder input to OpenAI and may incur charges. `store:false` is requested; consult provider retention terms before using sensitive stories.

The private token and per-process five-requests/minute limit are for a controlled demonstration only. Before internet-facing live deployment, implement authenticated users, shared rate limits, spend controls, logging redaction and appropriate privacy/consent handling. Never use a `NEXT_PUBLIC_` variable for secrets. No live AI calls were made as part of the build verification.

### Optional local PostgreSQL

From the project root:

```bash
docker compose up -d postgres
cd web
# Linux/macOS; local demo credentials only
DATABASE_URL=postgresql://ubuntubiz:local_demo_password@localhost:5432/ubuntubiz npm run db:migrate
```

On PowerShell, set `$env:DATABASE_URL` first. `DATABASE_SSL=true` enables certificate-validated TLS for a trusted remote database. The migration is idempotent. The demo database binds only to localhost. Row-level policies are included for a future authorised API: set `app.account_id` per transaction, use a non-owner application role without `BYPASSRLS`, and verify business ownership. The migration owner/superuser bypasses these policies; do not use it as the future API role. No account login or database-backed public routes are exposed in this MVP.

### Terraform / AWS

```bash
cd terraform
terraform init
terraform fmt -check
terraform validate
terraform plan
```

Both `enable_cloud` and `enable_database` default to `false`. Credentials may be needed by the AWS provider even for a zero-resource plan. To provision, review `terraform.tfvars.example`, configure your own AWS credentials, explicitly opt in, review costs and plan, and only then run `terraform apply`. **No cloud resources have been deployed.** AWS resources and OpenAI are optional and may be billable; neither is required for the Android capstone.

After creating the Amplify app, connect a source repository using the Amplify Console's GitHub App flow, reconcile its branch with the Terraform-managed branch, and use app root `web`. The default deployment is template-only. Live SSR secret handling must be configured and tested separately; do not put API secrets in Terraform variables, state, source control or public build variables.

Optional RDS uses private subnets, encrypted storage, managed master credentials, backups, no public endpoint access and no security-group ingress. Amplify SSR cannot directly reach this private database. A VPC-connected backend and controlled migration access must be added before database integration. There is no NAT gateway or public database opening. Deletion protection and a final snapshot are enabled: intentional removal requires updating deletion protection first and choosing a unique final snapshot identifier. Keep remote Terraform state encrypted and access-controlled when deploying as a team.

## Project layout

```text
ubuntubiz-ai/
  app/src/main/java/africa/ubuntubiz/ai/
    domain/       # Pure models, catalogue, generation and phrasebook
    data/         # Android local persistence
    ui/           # Material 3 components, screens, navigation and ViewModel
    MainActivity.kt
  app/src/test/              # Deterministic generation/calculation tests
  app/src/androidTest/       # Navigation, validation and search smoke test
  gradle/wrapper/            # Gradle wrapper, checksum-pinned distribution
  web/                       # Optional Next.js / Node.js companion
  database/                  # Optional PostgreSQL schema
  terraform/                 # Optional AWS infrastructure
  docs/                      # Demo walkthrough, architecture, verification
  .github/workflows/         # Reproducible Android/web verification
  amplify.yml
  compose.yaml
  README.md
```

## Capstone tracks

| Track | Contribution |
| --- | --- |
| Voices & Languages | A transparent African language greeting bridge; integration path for verified translations |
| Discovery & Audience | Searchable fictional business directory and founder profiles |
| Creation & Craft | Attributed founder stories, biographies, captions and business documents |
| Truth, Authorship & Ownership | Verbatim source testimony, attribution, explicit export and local deletion; no automatic publication |

See `docs/DEMO.md` for a five-minute presentation and `docs/VERIFICATION.md` for actual build results and remaining verification limits.

Technical references: [Android Compose compiler setup](https://developer.android.com/develop/ui/compose/setup-compose-dependencies-and-compiler), [OpenAI structured outputs](https://developers.openai.com/api/docs/guides/structured-outputs), [AWS Amplify monorepos](https://docs.aws.amazon.com/amplify/latest/userguide/monorepo-configuration.html), [Terraform AWS provider](https://registry.terraform.io/providers/hashicorp/aws/latest/docs).
