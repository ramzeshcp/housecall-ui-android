# housecall-ui-android (POC)

> **Status:** Proof-of-concept — personal repo for spike validation. Will move to `Codefied/housecall-ui-android` once the approach is greenlit. See [GDSP-199](https://housecall.atlassian.net/browse/GDSP-199), [GDSP-204](https://housecall.atlassian.net/browse/GDSP-204).

Single-source-of-truth repo for everything related to the externalized Housecall Pro Android Design System. The same repo holds **three things** that ship together (mirrors the structure of the web counterpart `Codefied/housecall-ui`):

1. **The KMP design system library** — Kotlin source consumed by the Android app and the Wasm preview.
2. **The Compose Wasm preview app** — public iframe-able rendering of the lib, used by designers in `Codefied/housecall-prototype`.
3. **The Claude Code plugin** — `ds-eng-android` plugin + marketplace declaration that teaches Claude how to generate Android prototypes through the agentic prototyper.

## Repo layout

```
housecall-ui-android/
│
├── housecall-ui-android/          ← #1 The KMP DS lib (published Maven artifact)
│   ├── build.gradle.kts
│   └── src/commonMain/
│       ├── kotlin/com/housecall/designsystem/
│       │   ├── color/HcColors.kt
│       │   ├── typography/HcTypography.kt
│       │   ├── utils/ModifierExt.kt
│       │   └── textinput/
│       │       ├── HcTextField.kt              (V1 façade)
│       │       ├── TextFieldHelpers.kt
│       │       └── v2/
│       │           ├── BaseTextFieldV2.kt
│       │           └── HcTextFieldV2.kt
│       └── composeResources/
│           ├── font/manrope_*.ttf
│           └── drawable/ic_visibility_*.xml
│
├── preview/                       ← #2 Compose Wasm preview app (consumes the lib)
│   ├── build.gradle.kts
│   └── src/wasmJsMain/kotlin/
│       ├── Main.kt                              URL routing + Pixel 9 frame
│       ├── Builder.kt                           JSON layout DSL interpreter
│       └── IconRegistry.kt                      Curated outlined Material icon catalog
│
├── .claude-plugin/                ← #3 Claude Code plugin marketplace
│   └── marketplace.json                          Declares `android-design-system-marketplace`
│
├── plugins/ds-eng-android/        ← #3 The plugin itself
│   ├── .claude-plugin/plugin.json
│   ├── README.md
│   └── skills/housecall-ui-android/
│       ├── SKILL.md                              Entry point + pre-flight rules
│       └── references/
│           ├── android-preview.md                iframe + URL contract
│           ├── builder-dsl.md                    JSON DSL + icon registry + alignment
│           ├── colors.md                         HcColors palette
│           ├── compose-component-catalog.md      Shipped/pending components
│           ├── prototyping-rules.md              When to ask for new screens
│           ├── spacing.md                        dp grid
│           ├── text-field.md                     HcTextField specifics
│           └── typography.md                     Manrope + MML typescale
│
└── .github/workflows/
    └── deploy-preview.yml                        Auto-deploys preview to GitHub Pages
```

## What each piece does

### 1) KMP DS library

Externalized version of the Housecall Pro Android Design System. Publishes:
- **`.aar`** for Android consumers (the Pro app, internal Storybook, etc.)
- **`.klib`** for Compose Multiplatform Wasm consumers (the preview app)

Both variants come from a single `commonMain` source set — same code, same Skiko renderer, byte-perfect parity between the Android app and the web preview (validated via Roborazzi screenshot tests in the parent project).

### 2) Compose Wasm preview app

A static HTML/JS/Wasm bundle deployed to GitHub Pages that designers embed via iframe. Two route shapes:

- **`?screen=text-fields`** (and other named screens) — pre-built showcases.
- **`?screen=builder&layout=<URL-encoded JSON>`** — runtime layout interpretation. Designers (via Claude) compose arbitrary screens from a small JSON DSL: `column`, `row`, `box`, `spacer`, `text`, `hc-text-field`. Includes a curated icon registry of ~80 outlined Material icons (placeholder for future `HcIcons` migration).

Live deploy: <https://ramzeshcp.github.io/housecall-ui-android/>

### 3) Claude Code plugin

`ds-eng-android` is the Android counterpart to `ds-eng:housecall-ui` (the web plugin). It teaches Claude Code, when running inside `Codefied/housecall-prototype`, how to:

- Detect Android intent in designer prompts (`android`, `kotlin`, `compose`, `mobile`, `native` keywords).
- Generate a TSX prototype that wraps `<AndroidPreview src="…" />` instead of `housecall-ui/dist/...` imports.
- Build the iframe URL from a layout JSON (the builder DSL), translating natural-language intent (alignment, paddings, icon names) to DSL props.
- Recognize when a designer prompt requires a not-yet-migrated component and add a `TODO(DS-team)` rather than fake the rendering.

The plugin is **strictly self-contained** — does NOT defer to `ds-core` or `ds-eng` (web plugins) since their references describe Open Sans / MUI / CSS conventions that don't apply to Android. Foundations (Manrope, HcColors, dp grid) are documented inline under `references/`.

## Designer install (once provisioned in `Codefied/`)

```
/plugin marketplace add Codefied/housecall-ui-android
/plugin install ds-eng-android@android-design-system-marketplace
```

And in `Codefied/housecall-prototype/.claude/settings.json`:

```json
{
  "enabledPlugins": {
    "ds-core@design-system-marketplace": true,
    "ds-eng@design-system-marketplace": true,
    "ds-eng-android@android-design-system-marketplace": true
  },
  "extraKnownMarketplaces": {
    "design-system-marketplace": { "source": { "source": "github", "repo": "Codefied/housecall-ui" } },
    "android-design-system-marketplace": { "source": { "source": "github", "repo": "Codefied/housecall-ui-android" } }
  }
}
```

After install, `/prototype "android: <description>"` produces a working Android prototype in seconds.

## Build & run locally

```bash
# Compose Wasm preview — live dev server with hot reload
./gradlew :preview:wasmJsBrowserDevelopmentRun
# → http://localhost:8080

# Compose Wasm preview — production bundle
./gradlew :preview:wasmJsBrowserDistribution
# → preview/build/dist/wasmJs/productionExecutable/

# Publish the KMP lib's .aar + .klib to mavenLocal (so another build can consume it)
./gradlew :housecall-ui-android:publishToMavenLocal
# → ~/.m2/repository/com/housecall/housecall-ui-android/0.1.0-SNAPSHOT/
```

## Versions

- Kotlin 2.2.20
- Compose Multiplatform 1.10.0
- AGP 9.2.0 (legacy `com.android.library` plugin + `android.builtInKotlin=false` / `android.newDsl=false` workaround flags — required because the new `com.android.kotlin.multiplatform.library` plugin in AGP 9.x does not currently package `composeResources/` into the AAR)
- Gradle 9.4.1
- kotlinx-serialization 1.7.3 (used by the preview app for builder-DSL JSON parsing)

## CI/CD

**`deploy-preview.yml`** — on push to `main`:
1. Set up JDK 17 + Android SDK 36
2. `./gradlew :preview:wasmJsBrowserDistribution`
3. Upload `preview/build/dist/wasmJs/productionExecutable/` as a Pages artifact
4. Deploy to GitHub Pages → <https://ramzeshcp.github.io/housecall-ui-android/>

## What this proves

1. The Android DS can be extracted into a separate repo and consumed by the Android app as a normal Maven `.aar` — no source dependency.
2. The same KMP module produces a `.klib` that a Compose Multiplatform Wasm app consumes, rendering the components in the browser via Skiko (byte-identical to the Android target).
3. The preview site is a static bundle deployable to any host (GitHub Pages, Netlify, Vercel, S3+CloudFront).
4. Designers can embed the preview in `Codefied/housecall-prototype` via a trivial iframe component (~25 LOC TSX), with **zero local Android tooling**.
5. The Claude Code plugin lets designers describe arbitrary Android screens in natural language and get them rendered live, without DS-team or designer needing to write any Kotlin or JSON.

## See also

- [POC parent project](https://github.com/Codefied/housecall-pros-android) (branch `POC_compose_web_wasm`, docs under `poc/docs/`)
- [GDSP-199](https://housecall.atlassian.net/browse/GDSP-199) — original Compose Wasm spike
- [GDSP-204](https://housecall.atlassian.net/browse/GDSP-204) — KMP/Wasm consumption-path POC
- [Builder DSL & Skill Integration POC report](https://housecall.atlassian.net/wiki/spaces/DS/pages/4183785899/Android+Prototyper+Builder+DSL+Skill+Integration) — the work that added builder-DSL + plugin
- [Web prototyper install guide](https://housecall.atlassian.net/wiki/spaces/DS/pages/4119461913/Install+the+Web+Prototyper+Tool)
- `Codefied/housecall-ui` — web counterpart (same single-repo pattern: lib + plugin)
