# Spacing — Android (dp grid)

> **Source of truth for Android spacing.** Do NOT consult `ds-core:guidelines` for spacing — its values are described in `px` for the web stack. Android uses `dp` (density-independent pixels), and the values below are the conventions actually used by `Hc*` components in `core/ui` and the externalized library.

## Base grid

**4dp grid.** All spacing is a multiple of 4. The most common ladder:

| Step | dp     | Common usage                                             |
|---|---|---|
| 1    | `4dp`  | Tight inline spacing (icon ↔ label inside a chip)        |
| 2    | `8dp`  | Compact padding (small button inner padding)             |
| 3    | `12dp` | Mid-density padding (text field inner vertical padding)  |
| 4    | `16dp` | **Default screen edge padding**, default vertical rhythm |
| 5    | `20dp` | Section header → first content                           |
| 6    | `24dp` | Card padding, large section breaks                       |
| 8    | `32dp` | Page-level vertical separation                           |
| 12   | `48dp` | Top app bar height                                        |

## Touch targets

- **Minimum interactive size: 48dp x 48dp** (Material baseline). Buttons, icon buttons, list rows.
- `HcTextField` minimum height: ~56dp (label + input + helper text); helper text adds ~16dp below when present.

## Conventions used by HCP Android components

| Component        | Inner padding (horizontal × vertical) | Notes                                                |
|---|---|---|
| `HcTextField`    | 16dp × 12dp                           | Outlined variant; padding excludes the border itself |
| `HcButton` (default) | 24dp × 12dp                       | Pending migration but values are stable               |
| `HcCard`         | 16dp                                  | Edge-to-content                                       |
| Screen edges     | 16dp horizontal                        | Standard "side gutters"                               |
| Vertical rhythm  | 16dp between unrelated stacks        | Use 24dp for visual section break                    |

## Iframe frame size (logical dp)

The `<AndroidPreview />` defaults render the Compose Wasm preview at:

| Device                | width × height (dp) |
|---|---|
| **Default (Pixel 9)** | `412 × 915`           |
| Small phone           | `360 × 780`           |
| Pixel 9 Pro Max       | `428 × 926`           |
| Foldable (outer)      | `673 × 841`           |
| Tablet (Pixel C)      | `800 × 1280`          |

Override only when the prompt explicitly mentions one of those form factors.

## Implication for prototype generation

The designer never sets dp values in TSX. They are baked into the iframe's Compose layer. This file exists so the agent can:

1. Validate spacing intent (*"add a bit of breathing room"* → 16dp or 24dp depending on context).
2. Reject CSS-style answers (*"let's use 1rem"* → no, dp is the unit; the closest is 16dp).
3. Coordinate with the DS team if a designer asks for a non-grid value (e.g. 13dp).

## Anti-patterns

- ❌ Don't use `px`, `rem`, or `em` in any Android prototype discussion. The unit is `dp`.
- ❌ Don't suggest values off the 4dp grid (e.g. 13dp, 18dp, 27dp).
- ❌ Don't try to inject `style={{ padding: '16px' }}` on the iframe — padding lives inside the Compose preview.
