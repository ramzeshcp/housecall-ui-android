# Text Fields — Android (`HcTextField` / `HcTextFieldV2`)

> **Source of truth for Android text fields.** `ds-core:guidelines` does not document the Android variant — its references are about the web `<TextField>` (MUI). Use this file instead.

## What ships today

The Compose Wasm preview at `?screen=text-fields` renders the full `HcTextField` showcase from `housecall-ui-android`. Both API surfaces are byte-perfect ports of the Android library used by the production app.

| Component         | Status      | Notes                                                |
|---|---|---|
| `HcTextField` (V1) | ✅ shipped  | Façade delegating to V2 — preserves all V1 props for legacy callers |
| `HcTextFieldV2`   | ✅ shipped  | Modern API — preferred for new screens               |

V2 supports: `label`, `placeholder`, `helperText`, `errorText`, `leadingIcon`, `trailingIcon`, `trailingText`, password toggle, outlined variant, character-limit counter.

Both leading and trailing icon slots are exposed in the builder DSL via string names (e.g. `"leadingIcon": "search"`). Full list of icon names: see [builder-dsl.md § Icon registry](./builder-dsl.md#icon-registry).

## How to prototype a text field flow

The prototype is a thin TSX wrapper. The TSX never declares the text field — the iframe does.

```tsx
import React from 'react'
import AndroidPreview from '../../components/AndroidPreview/AndroidPreview'

const Prototype = () => (
  <AndroidPreview
    src="https://housecall-ui-android.netlify.app/?screen=text-fields"
    title="HCP Android — Text fields prototype"
  />
)

export default Prototype
```

The full showcase (label, placeholder, helper, error, password, outlined, char-limit) renders inside the iframe. Designers can screenshot, share, comment.

## Showing a single variant (or any custom composition)

Use `?screen=builder` with a layout JSON. The DSL exposes the full prop surface of `HcTextField` — you can render exactly one outlined field, a stack of three filled fields, an error-state field next to a regular one, etc. Without rebuilding or redeploying the preview app.

Example — single outlined text field:
```tsx
const layout = {
  type: 'column',
  padding: 16,
  children: [
    { type: 'hc-text-field', label: 'Company', placeholder: 'Acme Inc.', outlined: true },
  ],
}
const src = `https://ramzeshcp.github.io/housecall-ui-android/?screen=builder` +
            `&layout=${encodeURIComponent(JSON.stringify(layout))}`
```

Full DSL: [builder-dsl.md](./builder-dsl.md). All prop names match the V1 `HcTextField` signature 1:1.

## URL contract

| Param value         | Renders                                          | Status     |
|---|---|---|
| `?screen=text-fields` | All `HcTextField` / `HcTextFieldV2` variants — pre-built showcase  | ✅ shipped |
| `?screen=builder&layout=…` | Arbitrary layout from JSON DSL — can render 1 or N text fields with any prop combination | ✅ shipped |
| `?screen=default`   | Same as `?screen=text-fields`                     | ✅ shipped |

## Validation and error states

Validation is handled inside the Compose layer — the React side cannot inject errors or values. If a designer wants to demo the *error* state, the `?screen=text-fields` showcase already includes a slot showing an error variant.

If a future need is to demo *interactive validation* (designer types in the field, validation runs), the `housecall-ui-android` preview app would need a new screen where the showcase composable wires up real `MutableState` handling. File a request with the DS team.

## Coordination with foundations

- **Typography:** input text and label use MML tokens (`BodyDefault` for input, `LabelDefault` for label). Helper/error text is `SubtitleSm` regular weight. See [typography.md](./typography.md).
- **Colors:** outlined border = `Border.Default`, focused = `Primary.Main`, error = `Error.Main`, helper text = `Text.OnSurfaceSecondary`. See [colors.md](./colors.md).
- **Spacing:** inner padding 16dp × 12dp, helper text below adds ~16dp. See [spacing.md](./spacing.md).

## Anti-patterns

- ❌ Don't try to mimic `HcTextField` with MUI `<TextField>` in the prototype — defeats the iframe's purpose.
- ❌ Don't import `housecall-ui/dist/TextField` — that's the *web* DS, not Android.
- ❌ Don't write controlled-input handlers in the TSX wrapper — the iframe is opaque.
- ❌ Don't ask the designer to install Android tooling to "see" a text field. The deployed Wasm preview is the only thing they need.
