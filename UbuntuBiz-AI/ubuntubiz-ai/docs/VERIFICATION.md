# Verification record

Prepared 5 November 2026. This record distinguishes completed build verification from untested deployment/runtime integrations.

| Check | Result |
| --- | --- |
| Android Gradle configuration | Passed with AGP 8.7.3, Kotlin/Compose compiler 2.0.21, Gradle 8.10.2 |
| Android compilation and debug APK | Passed; final log included in `verification/android-build.txt` |
| JVM generation tests | 8 passed, 0 failures, 0 skipped; XML included |
| Android lint | Passed, no issues found (0 errors, 0 warnings); final report included in `verification/android-lint.txt` |
| Android instrumentation APK | Compiled successfully |
| APK signature | Verified using APK Signature Scheme v2; development signing only |
| Companion unit tests | 3 passed: testimony attribution, input validation, supported generation kinds |
| Companion production build | Passed Next.js 16.4.5 and TypeScript checks |
| Companion browser workflow | Passed generation, source-section preview, browser save/reload/load, invalid API input, mobile horizontal-overflow and JavaScript console checks |
| Companion visual review | Desktop and mobile layouts reviewed locally |
| Terraform HCL formatting/parsing | `terraform fmt -check -recursive` passed |
| Terraform full validation | Not completed: provider subprocess requires Unix domain sockets, which this execution environment does not support. Standard CI includes `init` and `validate`. Provider ZIP checksum matched the official release checksum. |
| AWS deployment | Not performed; default flags create no resources |
| Live OpenAI generation | Not called; integration is opt-in and needs owner-provided credentials |
| PostgreSQL migration runtime | Not executed; schema, Docker service and migration runner supplied |
| Native emulator / device runtime | Instrumentation test is provided and compiled. The software-only emulator did not become ready within the local test window; runtime UI execution is not claimed. Run the smoke test on an emulator or physical device. |

Reproduce the core Android checks with:

```bash
./gradlew testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest
```

Run native UI smoke coverage on a connected Android device:

```bash
./gradlew connectedDebugAndroidTest
```

The provided smoke test checks navigation, required-name validation, directory filtering and the no-results state. Before a capstone presentation, run the five-minute walkthrough on your intended phone. Device accessibility, very large text, low-memory behaviour and real-world language quality still require human review.

The Android application is the complete offline MVP. The companion's live AI, PostgreSQL accounts/persistence, authenticated cloud sync and AWS hosting are optional integration scaffolding. A successful local build does not establish those services as deployed or production-ready.
