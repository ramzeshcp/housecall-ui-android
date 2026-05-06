---
name: housecall-ui-android
description: >
  Guide for generating Android prototypes inside `Codefied/housecall-prototype`.
  Use when the user prompt mentions Android, mobile, native mobile, Compose,
  or Kotlin in the context of creating or editing a prototype.
when_to_use: >
  Generating an Android prototype, embedding the Compose Wasm preview,
  configuring URL parameters for the preview iframe, or answering
  Android-specific design system questions in the prototyper context.
user-invocable: true
allowed-tools: Read, Glob, Edit, Write
---

# Housecall UI Android

Android prototypes in `housecall-prototype` are **not generated as React component code**. They are generated as TSX files that wrap the deployed **Compose Multiplatform Wasm preview** in an `<AndroidPreview />` iframe.

The prototype's visual content is rendered by the externalized `com.housecall:housecall-ui-android` library — same Kotlin source as the Android app, same Skiko renderer, byte-perfect parity. React's job is purely to host the iframe inside the existing prototyper registry / sharing / version-snapshot system.

## Mandatory pre-flight (read every time)

Before generating ANY Android prototype, even if you've used this skill earlier in the session:

1. **Re-read [builder-dsl.md](${CLAUDE_PLUGIN_ROOT}/skills/housecall-ui-android/references/builder-dsl.md)** — the DSL surface evolves (new node types, new props, new icons). Stale knowledge produces false TODOs and missed features.
2. **Re-read [compose-component-catalog.md](${CLAUDE_PLUGIN_ROOT}/skills/housecall-ui-android/references/compose-component-catalog.md)** — components migrate from 🟡 to ✅ over time.
3. **Always create a NEW prototype directory** per `/prototype.md` steps 1-4 (infer name → generate ID → create slug → `mkdir src/prototypes/{slug}-{id}/`). NEVER overwrite or modify an existing prototype's `index.tsx`. If you find yourself editing a folder that doesn't match the slug you just generated, stop and start over.

## Currently shipped DSL features (as of 2026-05-06)

This list is the source of truth for what the agent should NOT mark as a TODO. If your context says otherwise (e.g. *"the DSL doesn't support icons yet"*), trust this list:

- ✅ **Layout primitives:** `column`, `row`, `box`, `spacer`, `text`
- ✅ **Layout props:** `padding`, `paddingHorizontal`, `paddingVertical`, `spacing`, `fillWidth`, `fillHeight`, `scroll`, `mainAxis`, `crossAxis`
- ✅ **Row weight distribution** — paired children (e.g. First/Last name) automatically split width 50/50
- ✅ **`hc-text-field`** with full V1 prop surface: `label`, `placeholder`, `helperText`, `errorText`, `isError`, `enabled`, `outlined`, `trailingText`, `passwordToggleEnabled`, `isCharLimited`, `charLimit`, `singleLine`, `value`, **`leadingIcon`**, **`trailingIcon`**
- ✅ **Icon registry** — ~80 names (search, email, phone, location, lock, person, …) mapped to outlined Material Icons. See builder-dsl.md § Icon registry. Treat this as the canonical list — don't guess.
- ✅ **Text styling:** `text` node accepts `style` (HcTypography token name) and `color` (HcColors path)

NOT yet shipped (legitimate TODOs):
- ❌ `hc-button`, `hc-checkbox`, `hc-select-text-field`, `hc-number-input-field`, `hc-counter-text-field` — pending DS migration
- ❌ Unequal row weight distribution (e.g. City 2x ZIP) — only equal split today
- ❌ `minLines` / `maxLines` props on `hc-text-field` — multi-line works via `singleLine: false` but starts at 1-line height

## Core Rules

- **Use `<AndroidPreview />` from `src/components/AndroidPreview/`** — never attempt to recreate Android components in React.
- **Never import** from `housecall-ui/dist/` or `housecall-icons/dist/` for Android prototypes. Those are web-only.
- **Set `platform: "android"`** in the prototype's `meta.json` so future registry filters can distinguish Android from web prototypes.
- **Configure content via URL params**:
  - For pre-built showcases: `?screen=text-fields` (or other shipped showcases — see catalog).
  - For arbitrary screen compositions: `?screen=builder&layout=<URL-encoded JSON>` — the **builder DSL** lets you compose any layout from primitives (column/row/box/spacer/text) + DS components (hc-text-field). See [builder-dsl.md](${CLAUDE_PLUGIN_ROOT}/skills/housecall-ui-android/references/builder-dsl.md).
- **Default iframe size: 412 x 915 dp** (Pixel 9 logical dimensions). Only override for tablet or small-phone targets when the prompt is explicit.

## Decision flow: which URL pattern to use

```
Designer prompt
   │
   ├─ Matches an existing pre-built showcase exactly? (e.g. "show all text-field variants")
   │     → Use ?screen=<showcase-name>      (richer, includes real interactions)
   │
   ├─ Asks for a custom composition? (e.g. "screen with a name field and an email field")
   │     → Use ?screen=builder + layout JSON  (builder DSL — see builder-dsl.md)
   │
   └─ Requires a component not yet migrated? (HcButton, HcCheckbox, …)
         → Use builder DSL with what's available + add a TODO(DS-team) comment
           OR offer to file a request for a new pre-built showcase
```

## Generation Pattern

### Pattern A — Pre-built showcase

For a prompt that maps to an existing showcase (e.g. *"text fields demo"*):

`src/prototypes/text-fields-android-{id}/index.tsx`:

```tsx
import React from 'react'
import AndroidPreview from '../../components/AndroidPreview/AndroidPreview'

const Prototype = () => (
  <AndroidPreview
    src="https://ramzeshcp.github.io/housecall-ui-android/?screen=text-fields"
    title="HCP Android — Text fields"
  />
)

export default Prototype
```

### Pattern B — Builder DSL (custom composition)

For a prompt like *"a screen with a single outlined text field for company name"*:

`src/prototypes/single-textfield-android-{id}/index.tsx`:

```tsx
import React from 'react'
import AndroidPreview from '../../components/AndroidPreview/AndroidPreview'

const layout = {
  type: 'column',
  padding: 16,
  spacing: 16,
  children: [
    { type: 'hc-text-field', label: 'Company', placeholder: 'Acme Inc.', outlined: true },
  ],
}

const previewUrl =
  `https://ramzeshcp.github.io/housecall-ui-android/?screen=builder` +
  `&layout=${encodeURIComponent(JSON.stringify(layout))}`

const Prototype = () => (
  <AndroidPreview src={previewUrl} title="HCP Android — Single text field" />
)

export default Prototype
```

Full DSL schema and more examples: [builder-dsl.md](${CLAUDE_PLUGIN_ROOT}/skills/housecall-ui-android/references/builder-dsl.md).

`src/prototypes/scheduling-android-{id}/meta.json`:

```json
{
  "name": "Scheduling — Android",
  "id": "{{ID}}",
  "platform": "android",
  "createdAt": "{{TIMESTAMP}}",
  "label": "{{designer-name}}",
  "versions": []
}
```

## Reference Files

Read when topic relevant:

**Iframe contract & catalog**
- **[android-preview.md](${CLAUDE_PLUGIN_ROOT}/skills/housecall-ui-android/references/android-preview.md)** — full API of the `AndroidPreview` component, available URL params, supported screen names exposed by the preview app
- **[compose-component-catalog.md](${CLAUDE_PLUGIN_ROOT}/skills/housecall-ui-android/references/compose-component-catalog.md)** — list of components currently rendered by the Compose Wasm preview (HcTextField, HcButton, etc.) — what is available to prototype today
- **[builder-dsl.md](${CLAUDE_PLUGIN_ROOT}/skills/housecall-ui-android/references/builder-dsl.md)** — JSON layout DSL for `?screen=builder`. Use this to compose arbitrary screens from primitives + DS components without rebuilding the preview app.
- **[prototyping-rules.md](${CLAUDE_PLUGIN_ROOT}/skills/housecall-ui-android/references/prototyping-rules.md)** — HCP Android prototyping conventions: when to ask for a new screen to be added to the preview vs. composing existing ones via URL params

**Android-specific design foundations** (self-contained — do NOT defer to `ds-core`)
- **[typography.md](${CLAUDE_PLUGIN_ROOT}/skills/housecall-ui-android/references/typography.md)** — Manrope family + MML typescale + M2 legacy aliases. Use this; `ds-core` describes Open Sans (web).
- **[colors.md](${CLAUDE_PLUGIN_ROOT}/skills/housecall-ui-android/references/colors.md)** — `HcColors` palette (Primary, Surface, Border, Error, Common, Grey scale) with exact hex values
- **[spacing.md](${CLAUDE_PLUGIN_ROOT}/skills/housecall-ui-android/references/spacing.md)** — dp grid (4/8/16/24), padding conventions, target sizes
- **[text-field.md](${CLAUDE_PLUGIN_ROOT}/skills/housecall-ui-android/references/text-field.md)** — `HcTextField` / `HcTextFieldV2` usage in Wasm preview context (variants, URL params, when to use which)

## Reporting the result

After generating an Android prototype (and the `/prototype` slash command's typecheck step has passed), the response MUST end with this exact block, with `{slug}-{id}` substituted for the new prototype's directory name. Format the URL and path as markdown links so the IDE renders them clickable:

```
**Preview locally:** [http://localhost:3000/prototype/{slug}-{id}](http://localhost:3000/prototype/{slug}-{id})
**Source:** [src/prototypes/{slug}-{id}/index.tsx](src/prototypes/{slug}-{id}/index.tsx)
**To share publicly:** run `/share-prototype` to push the share branch and get a public URL.
```

This block is mandatory — do not summarize the work without it. Reasoning:

- The localhost URL is the dev preview (only works on the designer's machine while the dev server is running).
- The source path lets the designer jump to the file in one click.
- The `/share-prototype` reminder is critical because designers often forget that the localhost URL isn't shareable to others — they need to run that command to publish to the share branch.

This requirement is Android-specific (lives in this skill, not in the slash command) so the parallel web flow is not affected.

## Coordination With Other Plugins

**This skill is self-contained. Do NOT auto-invoke `ds-core:guidelines` or `ds-eng:housecall-ui` from an Android prototype flow.**

Reasons:
- `ds-core:guidelines` declares itself multi-platform but its current references and Key Rules are 100% web-biased: Open Sans typography, MUI components, CSS hover states, `typography.web.*` token names. Following them would produce broken Android prototypes (wrong font, wrong components, wrong API surface).
- `ds-eng:housecall-ui` is exclusively for `housecall-ui/dist/` (web React/MUI) — its imports and patterns do not apply to the iframe wrapper used here.
- All Android-specific tokens (Manrope, HcColors, dp spacing) are documented inline in this skill's references above. There is no need to reach outside.

**`/prototype` slash command** — when the prompt indicates Android intent (keywords: `android`, `kotlin`, `compose`, `mobile`, `native`), the slash command routes generation through THIS skill only and does not invoke the web plugins. Web prompts route to `ds-core` + `ds-eng:housecall-ui` and never invoke this skill. The two flows are isolated by design — never mixed.

## Anti-patterns

DO NOT:

- Generate Kotlin code in the React prototyper. Kotlin lives in the `housecall-ui-android` repo. Prototypes are TSX wrappers only.
- Generate React components that mimic Android styling (e.g. trying to build an Android-looking text field with MUI). The whole point of the iframe is to use the *real* Android component.
- Import from `housecall-ui/dist/` in an Android prototype — that is the web DS, not Android.
- Try to communicate between the React parent and the iframe via direct function calls. Use `postMessage` if cross-frame messaging is ever required (currently not needed for static previews).
- Ask the user to install Android tooling locally (JDK, Gradle, Android SDK). The whole point of the iframe pattern is that designers don't need any of that — the preview is already deployed.
