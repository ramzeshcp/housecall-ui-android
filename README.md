# housecall-ui-android (POC)

> **Status:** Proof-of-concept — personal repo for spike validation. Will move
> to `Codefied/housecall-ui-android` once the approach is greenlit. See
> [GDSP-199](https://housecall.atlassian.net/browse/GDSP-199).

Externalized version of the Housecall Pro Android Design System, packaged as a
Kotlin Multiplatform library that publishes:

- **`.aar`** for Android consumers (the Pro app, internal Storybook, etc.)
- **`.klib`** for Compose Multiplatform Wasm consumers (the design preview site)

Both variants come from a single `commonMain` source set — same code, same
Skiko renderer, byte-perfect parity between the Android app and the web preview.

## Repo layout

```
housecall-ui-android/
├── housecall-ui-android/          ← The KMP DS lib (published as Maven artifact)
│   ├── build.gradle.kts
│   └── src/commonMain/
│       ├── kotlin/com/housecall/designsystem/
│       │   ├── color/HcColors.kt
│       │   ├── typography/HcTypography.kt
│       │   ├── utils/ModifierExt.kt
│       │   └── textinput/
│       │       ├── HcTextField.kt          (V1 façade)
│       │       ├── TextFieldHelpers.kt
│       │       └── v2/
│       │           ├── BaseTextFieldV2.kt
│       │           └── HcTextFieldV2.kt
│       └── composeResources/
│           ├── font/manrope_*.ttf
│           └── drawable/ic_visibility_*.xml
│
├── preview/                       ← Compose Wasm showcase site (consumes the lib)
│   ├── build.gradle.kts
│   └── src/wasmJsMain/
│       └── kotlin/Main.kt          (Pixel 9-sized canvas rendering HcTextField variants)
│
└── .github/workflows/
    └── deploy-preview.yml          (auto-deploys preview to GitHub Pages on push to main)
```

## What this proves

1. The Android DS can be extracted into a separate repo and consumed as a
   normal Maven artifact (`.aar`) by the Android app — no source dependency.
2. The same KMP module produces a `.klib` that a Compose Multiplatform Web app
   consumes, rendering the components in the browser via Skiko (canvas).
3. The rendered output is **byte-identical** to what the Android target draws
   (validated via Roborazzi screenshot tests in the parent project).
4. The preview site is a static HTML/JS/Wasm bundle deployable to any static
   host (GitHub Pages, Netlify, Vercel, S3+CloudFront, etc.).
5. Designers can embed the preview in `Codefied/housecall-prototype` via a
   trivial iframe (~25 LOC TSX), without installing any Android tooling locally.

## Run locally

```bash
# Live dev server with hot reload (browser opens automatically)
./gradlew :preview:wasmJsBrowserDevelopmentRun
# → http://localhost:8080

# Production bundle
./gradlew :preview:wasmJsBrowserDistribution
# → preview/build/dist/wasmJs/productionExecutable/

# Publish the lib's .aar + .klib to mavenLocal (consumable from another build)
./gradlew :housecall-ui-android:publishToMavenLocal
# → ~/.m2/repository/com/housecall/housecall-ui-android/0.1.0-SNAPSHOT/
```

## Versions

- Kotlin 2.2.20
- Compose Multiplatform 1.10.0
- AGP 9.2.0 (legacy `com.android.library` plugin + `android.builtInKotlin=false`/`android.newDsl=false`
  workaround flags — required because the new `com.android.kotlin.multiplatform.library`
  plugin in AGP 9.x does not currently package `composeResources/` into the AAR)
- Gradle 9.4.1

## CI/CD

- **`deploy-preview.yml`** — on push to `main`:
  1. Set up JDK 17
  2. `./gradlew :preview:wasmJsBrowserDistribution`
  3. Deploy `preview/build/dist/wasmJs/productionExecutable/` to the `gh-pages` branch
  4. GitHub Pages serves it at <https://ramzeshcp.github.io/housecall-ui-android/>

## See also

- [POC parent project & migration log](https://github.com/Codefied/housecall-pros-android)
  *(branch `POC_compose_web_wasm`, files under `poc/docs/`)*
- [GDSP-199 spike ticket](https://housecall.atlassian.net/browse/GDSP-199)
- [Web prototyper install guide](https://housecall.atlassian.net/wiki/spaces/DS/pages/4119461913/Install+the+Web+Prototyper+Tool)
