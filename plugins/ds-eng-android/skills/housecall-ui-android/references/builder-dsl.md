# Builder DSL — `?screen=builder&layout=<JSON>`

> **Use this when** the designer prompt asks for a screen composition that does NOT match an existing pre-built showcase (e.g. *"create a screen with a name field, an email field, and a submit button"*, or *"show only one outlined text field"*).
>
> The agent generates a JSON layout, URL-encodes it, and embeds it in the iframe `src`. The Compose Wasm preview interprets the JSON at runtime — no rebuild/redeploy needed for new compositions.

## URL contract

```
https://ramzeshcp.github.io/housecall-ui-android/?screen=builder&layout=<URL-encoded JSON>
```

(Production Codefied URL TBD — for now the personal repo on GitHub Pages.)

The `layout` value MUST be a single JSON object, URL-encoded with `encodeURIComponent` (or equivalent). The interpreter passes it through `decodeURIComponent` and feeds it to `kotlinx.serialization.Json` (lenient mode, ignores unknown keys).

## Schema — uniform `LayoutNode`

Every node has the same shape. `type` is required. Other fields are interpreted per type — extra fields on the wrong type are ignored:

```jsonc
{
  "type": "<node-type>",        // required — see types below
  "children": [ <LayoutNode>, … ], // for layout containers

  // Layout props (apply to column/row/box)
  "padding": 16,                 // dp, all sides
  "paddingHorizontal": 16,       // dp
  "paddingVertical": 24,         // dp
  "spacing": 8,                  // dp between children (column/row)
  "fillWidth": true,
  "fillHeight": true,
  "scroll": true,                // adds vertical scroll (column only)
  "mainAxis": "start|center|end|spaceBetween|spaceAround",
  "crossAxis": "start|center|end",

  // Spacer props
  "height": 16,                  // dp
  "width": 16,                   // dp

  // Text props
  "text": "Hello",
  "style": "HeadingLg",          // see typography.md for all token names
  "color": "Text.OnSurfacePrimary", // see colors.md for all paths

  // hc-text-field props (V1 façade — full surface)
  "value": "",                   // initial value
  "label": "Name",
  "placeholder": "Enter name",
  "helperText": "Required",
  "errorText": "Invalid name",
  "isError": false,
  "enabled": true,               // default true
  "outlined": false,             // default false (filled variant)
  "leadingIcon": "search",       // icon name from registry (see "Icon registry" below)
  "trailingIcon": "close",       // icon name from registry
  "trailingText": "Kg",          // mutually exclusive with trailingIcon — pick one
  "passwordToggleEnabled": false,
  "isCharLimited": false,
  "charLimit": 60,
  "singleLine": false            // default false
}
```

## Node types

### Layout primitives

| `type`    | Renders                                                          |
|---|---|
| `column`  | Compose `Column` — children stack vertically                     |
| `row`     | Compose `Row` — children stack horizontally                      |
| `box`     | Compose `Box` — children overlap (positional)                    |
| `spacer`  | Empty space; use `height` or `width` to size it                  |
| `text`    | Compose `Text` with `HcTypography` style + `HcColors` color      |

### DS components (currently shipped)

| `type`            | Renders                                                          |
|---|---|
| `hc-text-field`   | `HcTextField` (V1 façade — covers V2 internally). All props above. |

### DS components (pending migration — NOT YET AVAILABLE)

| `type`         | Will render               | Migration status |
|---|---|---|
| `hc-button`    | `HcButton` (variants)      | 🟡 not yet migrated to externalized lib |
| `hc-checkbox`  | `HcCheckbox`               | 🔴 future iteration |
| `hc-select-text-field` | `HcSelectTextFieldV2` | 🟡 deferred (pending V1 helpers migration) |
| `hc-number-input-field` | `HcNumberInputFieldV2` | 🟡 deferred (pending CurrencyFormatter) |
| `hc-counter-text-field` | `HcCounterTextFieldV2` | 🟡 deferred (pending HcButton) |

If a designer prompt requires a 🟡/🔴 component, the agent MUST:
1. Tell the designer the component is not yet available in the externalized preview.
2. Offer to use only the available types (text fields + layout) as a placeholder.
3. Add a `// TODO(DS-team)` comment in the prototype's `index.tsx` referencing the missing components.

Do NOT invent fake `hc-button` rendering by drawing a `box` with text inside — the result would not match the real DS.

## Examples

### Example 1 — single outlined text field, centered

Prompt: *"una pantalla con un solo text field outlined al centro"*

Layout JSON:
```json
{
  "type": "column",
  "padding": 16,
  "spacing": 16,
  "mainAxis": "center",
  "fillHeight": true,
  "children": [
    {
      "type": "hc-text-field",
      "label": "Company",
      "placeholder": "Acme Inc.",
      "outlined": true
    }
  ]
}
```

Generated TSX:
```tsx
import React from 'react'
import AndroidPreview from '../../components/AndroidPreview/AndroidPreview'

const layout = {
  type: 'column',
  padding: 16,
  spacing: 16,
  mainAxis: 'center',
  fillHeight: true,
  children: [
    { type: 'hc-text-field', label: 'Company', placeholder: 'Acme Inc.', outlined: true },
  ],
}

const Prototype = () => (
  <AndroidPreview
    src={`https://ramzeshcp.github.io/housecall-ui-android/?screen=builder&layout=${encodeURIComponent(JSON.stringify(layout))}`}
    title="HCP Android — Single text field"
  />
)

export default Prototype
```

### Example 2 — login-like form (text fields only, no button yet)

Prompt: *"login screen with email and password"*

Layout JSON:
```json
{
  "type": "column",
  "padding": 24,
  "spacing": 16,
  "scroll": true,
  "children": [
    { "type": "text", "text": "Sign in", "style": "DisplayDefault" },
    { "type": "text", "text": "Welcome back", "style": "BodyDefault", "color": "Text.OnSurfaceSecondary" },
    { "type": "spacer", "height": 16 },
    { "type": "hc-text-field", "label": "Email", "outlined": true },
    { "type": "hc-text-field", "label": "Password", "outlined": true, "passwordToggleEnabled": true }
  ]
}
```

Add a TODO comment in the TSX:
```tsx
// TODO(DS-team): submit button is not yet available in the externalized preview.
// Will render a real <HcButton> once `housecall-ui-android` ships HcButton migration.
```

### Example 3 — two text fields side by side (row)

```json
{
  "type": "column",
  "padding": 16,
  "spacing": 16,
  "children": [
    { "type": "text", "text": "Name", "style": "HeadingDefault" },
    {
      "type": "row",
      "spacing": 8,
      "fillWidth": true,
      "children": [
        { "type": "hc-text-field", "label": "First", "outlined": true },
        { "type": "hc-text-field", "label": "Last", "outlined": true }
      ]
    }
  ]
}
```

> **Note:** when a `row` contains text fields, each field naturally fills available width. If the row needs explicit weight distribution, that's not supported in the DSL today — file a request with the DS team.

## Prompt → alignment mapping

Designer prompts use natural language. Translate the intent (regardless of input language — Spanish, English, etc.) to the corresponding DSL props on the **root layout node** (typically a `column`). Always emit the DSL values themselves in English (`"start"`, `"center"`, `"end"`, `"spaceBetween"`, `"spaceAround"`) — these are the only values the interpreter accepts.

| Intent                                | `mainAxis` | `crossAxis` | `fillHeight` | Notes                                                  |
|---|---|---|---|---|
| Top-aligned (default)                  | `"start"` (or omit) | — | — | The column starts from top-down — no extra props needed. |
| Centered vertically / in the middle    | `"center"` | `"center"` | `true` | Without `fillHeight: true` the column would only be as tall as its content and "center" is meaningless. |
| Bottom-anchored                        | `"end"`    | — | `true` | Needs `fillHeight: true` for `end` to push items to the bottom. |
| Left-aligned (default for column)      | — | `"start"` (or omit) | — | |
| Centered horizontally                  | — | `"center"` | — | |
| Right-aligned                          | — | `"end"` | — | |
| Spread evenly along main axis          | `"spaceBetween"` | — | `true` | Pushes first item to top, last to bottom, others spaced equally. |
| First/last anchored, equal gap around  | `"spaceAround"` | — | `true` | Equal space before, between, and after each item. |

For a **`row`** node, swap the meaning:
- `mainAxis` controls horizontal arrangement (start/center/end/spaceBetween/spaceAround)
- `crossAxis` controls vertical alignment within the row

### Common combinations

**Centered card-style layout** (single component in middle of screen):
```json
{ "type":"column", "padding":16, "fillHeight":true, "mainAxis":"center", "crossAxis":"center", "children":[ … ] }
```

**Top-aligned form** (default):
```json
{ "type":"column", "padding":16, "spacing":16, "children":[ … ] }
```

**Bottom-anchored content** (e.g. submit button at bottom of screen):
```json
{ "type":"column", "padding":16, "fillHeight":true, "mainAxis":"end", "children":[ … ] }
```

**Header at top, content at bottom** (push apart):
```json
{ "type":"column", "padding":16, "fillHeight":true, "mainAxis":"spaceBetween", "children":[ … ] }
```

## Icon registry

`leadingIcon` and `trailingIcon` accept a string from this curated registry. The interpreter renders the **outlined** Material Icons variant.

> **Trade-off:** these are placeholder icons styled by Material, not the real `HcIcons` set used inside the Android app. Visually close but not byte-perfect. Once `HcIcons` is migrated to the externalized lib, the registry will switch to those drawables — the JSON contract (icon names) stays stable.

If a designer requests an icon name not in this list, the interpreter renders a red `help-outline` placeholder so the gap is visible. The agent should:
1. Pick the closest known name and proceed.
2. Add a `// TODO(DS-team): add "xyz" to the icon registry` comment in the prototype `index.tsx`.

### Known icon names

Grouped by category. Aliases listed together — they all render the same icon.

| Category    | Names                                                                       |
|---|---|
| Search      | `search`, `find`, `magnify`                                                 |
| Close       | `close`, `x`, `dismiss`, `cancel`                                            |
| Navigation  | `menu` (alias `hamburger`), `back` / `arrow-back`, `forward` / `arrow-forward`, `chevron-left`, `chevron-right`, `expand-more`, `expand-less`, `arrow-drop-down`, `arrow-drop-up`, `more-vert` (alias `more`), `more-horiz` |
| Identity    | `person` / `user` / `profile`, `account`                                     |
| Contact     | `email` / `mail`, `phone` / `call`                                           |
| Security    | `lock` / `password`, `visibility` / `show`, `visibility-off` / `hide`       |
| CRUD        | `add` / `plus`, `edit` / `pencil`, `delete` / `trash` / `remove`             |
| File        | `file` / `document`, `folder`, `image` / `photo`, `camera`, `attach` / `paperclip`, `copy`, `download` |
| Status      | `info`, `warning` / `alert`, `check` / `checkmark`, `help` / `question`     |
| App         | `settings` / `gear`, `home`, `notifications` / `bell`, `share`, `send`, `refresh` / `reload`, `favorite` / `heart`, `star`, `language` |
| Commerce    | `shopping-cart` / `cart`, `credit-card` / `card`, `money` / `dollar`         |
| Time / loc. | `calendar`, `date` / `date-range`, `time` / `clock`, `location` / `pin` / `map` |

### Examples with icons

**Search field with leading icon:**
```json
{ "type": "hc-text-field", "label": "Search", "placeholder": "Type to search…", "leadingIcon": "search" }
```

**Email + password login pair:**
```json
{ "type": "column", "padding": 16, "spacing": 16, "children": [
  { "type": "hc-text-field", "label": "Email", "leadingIcon": "email" },
  { "type": "hc-text-field", "label": "Password", "leadingIcon": "lock", "passwordToggleEnabled": true }
]}
```

**Searchable customer list header (filled, with clear-button trailing icon):**
```json
{ "type": "hc-text-field", "label": "Find a customer", "leadingIcon": "search", "trailingIcon": "close" }
```

### `trailingIcon` vs `trailingText`

These are mutually exclusive. The DSL interpreter passes both to `HcTextField` if both are set, but only one will render visibly. Use `trailingText` for unit suffixes (`"Kg"`, `"$"`), `trailingIcon` for actions (clear, dropdown, toggle).

## Encoding rules

- The generated TSX uses `encodeURIComponent(JSON.stringify(layout))` — never hand-encode.
- Keep the JSON minimal: omit defaults (`enabled: true`, `singleLine: false`, etc.) — the interpreter resolves them.
- If the URL grows past ~6KB, simplify the layout (split into multiple prototypes) — most browsers accept up to 8KB but proxies can truncate.

## Token references

The DSL's `style` and `color` strings are resolved by the interpreter to the real `HcTypography.X` / `HcColors.X.Y` values. See:
- [typography.md](./typography.md) — full list of valid `style` values
- [colors.md](./colors.md) — full list of valid `color` paths
- [spacing.md](./spacing.md) — dp values to use for `padding`/`spacing`/`height`/`width`

## When NOT to use the builder

- **A pre-built showcase already exists for what the designer wants.** Use it directly via `?screen=text-fields` (or future `?screen=login`, `?screen=checkout`). Pre-built screens are richer (variants, real interactions) than what the DSL can express.
- **The prompt requires components not yet migrated** (HcButton, HcCheckbox, etc.). Use the showcase or document the gap with a TODO; do not fake these components in the DSL.

## Anti-patterns

- ❌ Don't hand-write the JSON inline in TSX without `encodeURIComponent` — special chars break the URL.
- ❌ Don't use `type: "button"` or other made-up types — the interpreter renders a red error message for unknown types (intentional, surfaces typos in dev).
- ❌ Don't try to position with absolute coordinates — there is no `x`/`y` prop. Use `column` / `row` / `box` with alignment props.
- ❌ Don't try to inject custom Composables — the DSL is intentionally constrained to the DS surface.
