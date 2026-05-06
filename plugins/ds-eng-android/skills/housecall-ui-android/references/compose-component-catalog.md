# Compose component catalog — what's available to prototype today

Updated whenever a new component is added to `housecall-ui-android` and a corresponding showcase is added to the `preview/` module.

| Component | Migration status | Showcase URL param | Builder DSL `type` | Notes |
|---|---|---|---|---|
| `HcTextField` (V1) | ✅ shipped | `?screen=text-fields` | `hc-text-field` | V1 façade delegating to V2. All props supported in the DSL. |
| `HcTextFieldV2` | ✅ shipped | `?screen=text-fields` | `hc-text-field` (V1 façade routes here) | All props in DSL: label, placeholder, helper, error, `leadingIcon`/`trailingIcon` (from icon registry), trailing text, password toggle, outlined, char-limited |
| Material Icons (Outlined) — placeholder for HcIcons | ✅ shipped | n/a | `leadingIcon`/`trailingIcon` strings on `hc-text-field` | ~80 curated names in the registry. **Will swap to real HcIcons once that migration ships** — JSON contract stays stable. See [builder-dsl.md § Icon registry](./builder-dsl.md#icon-registry). |
| `HcColors` foundation | ✅ shipped | used by all screens | referenced via `color` strings (e.g. `"Primary.Main"`) | Full DS palette |
| `HcTypography` | ✅ shipped | used by all screens | referenced via `style` strings (e.g. `"HeadingLg"`) | Manrope family fully bundled (Regular/SemiBold/Bold/ExtraBold). MML typescale + M2 legacy aliases |
| Layout primitives | ✅ shipped | `?screen=builder` | `column`, `row`, `box`, `spacer`, `text` | Compose `Column`/`Row`/`Box`/`Spacer`/`Text` accessible from the DSL |
| `HcSelectTextFieldV2` | 🟡 deferred | n/a | n/a (will expose `hc-select-text-field`) | Pending V1 helpers migration (InputLeadingIcon, TrailingIconSelector) |
| `HcNumberInputFieldV2` | 🟡 deferred | n/a | n/a (will expose `hc-number-input-field`) | Pending `:core:utils.CurrencyFormatter` extraction |
| `HcCounterTextFieldV2` | 🟡 deferred | n/a | n/a (will expose `hc-counter-text-field`) | Pending HcButton migration (`HcButton.IconOutlined`) |
| `HcButton` family | 🟡 not yet migrated | n/a | n/a (will expose `hc-button`) | Migration scoped in next iteration |
| `HcTopAppBar` | 🔴 future | n/a | n/a | Future iteration |
| `HcAvatar` | 🔴 future | n/a | n/a | Future iteration |
| `HcDialog`, `HcSnackbar`, `HcCard`, etc. | 🔴 future | n/a | n/a | Future iterations |

## Designer-facing implication

If a designer asks for a custom composition (e.g. *"two text fields and a button"*), the agent decides:

1. **All required components are ✅ shipped + appear in the "Builder DSL `type`" column** → use `?screen=builder` with a layout JSON. No preview rebuild needed. See [builder-dsl.md](./builder-dsl.md).
2. **Some required components are 🟡 deferred** → use the builder DSL with what IS available, add a `// TODO(DS-team): hc-button is not yet in the DSL — pending HcButton migration.` comment in the prototype's `index.tsx`.
3. **A pre-built showcase already covers the prompt** (e.g. *"show all text-field variants"*) → use `?screen=<showcase>` directly — richer, includes real interactions.

## How this catalog gets updated

This file is hand-maintained by the DS team. When `Codefied/housecall-ui-android` ships a new component:

1. Add the component to the table above (with its DSL `type` if exposed).
2. Optionally add a `?screen=X` showcase in `preview/src/wasmJsMain/kotlin/.../showcases/`.
3. Add the new node type to the DSL interpreter (`Builder.kt`) — typically a 1-3 line addition to the `RenderNode` `when` block.
4. Bump the plugin's `version` in its `plugin.json`.
5. Mention in the release notes that designers should run `/check-for-updates` to pick up the new catalog.
