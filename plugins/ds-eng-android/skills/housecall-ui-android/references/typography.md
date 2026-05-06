# Typography — Android (Manrope)

> **Source of truth for Android typography.** Do NOT consult `ds-core:guidelines` for typography in an Android prototype — that plugin documents Open Sans (web).

## Family

**Manrope** — bundled inside the `housecall-ui-android` library AAR/Wasm bundle as `composeResources/font/manrope_*.ttf`. The library exposes 4 weights as `FontFamily` declarations:

| FontFamily         | Weight       |
|---|---|
| `ManropeRegular`   | Normal (400)  |
| `ManropeSemiBold`  | SemiBold (600) |
| `ManropeBold`      | Bold (700)    |
| `ManropeExtraBold` | ExtraBold (800) |

The designer never selects fonts manually — they are baked into every `Hc*` component rendered inside the iframe.

## MML typescale (Mobile Modernization Library)

Canonical Android typescale. Every value below is rendered byte-perfect by Skiko inside the Wasm preview.

| Token             | Family         | Size  | LineHeight | Weight     |
|---|---|---|---|---|
| `DisplayXl`       | ExtraBold      | 32sp  | 40sp       | ExtraBold  |
| `DisplayLg`       | ExtraBold      | 28sp  | 36sp       | ExtraBold  |
| `DisplayDefault`  | Bold           | 26sp  | 32sp       | Bold       |
| `HeadingLg`       | Bold           | 24sp  | 28sp       | Bold       |
| `HeadingDefault`  | Bold           | 20sp  | 28sp       | Bold       |
| `HeadingSm`       | SemiBold       | 18sp  | 24sp       | SemiBold   |
| `HeadingSmBold`   | Bold           | 18sp  | 24sp       | Bold       |
| `HeadingXs`       | SemiBold       | 16sp  | 22sp       | SemiBold   |
| `HeadingXsBold`   | Bold           | 16sp  | 22sp       | Bold       |
| `SubtitleDefault` | Bold           | 14sp  | 20sp       | Bold       |
| `SubtitleSm`      | Bold           | 12sp  | 18sp       | Bold       |
| `BodyDefault`     | Regular        | 16sp  | 24sp       | Regular    |
| `BodySm`          | Regular        | 14sp  | 20sp       | Regular    |
| `LabelDefault`    | SemiBold       | 14sp  | 18sp       | SemiBold   |
| `LabelSm`         | SemiBold       | 12sp  | 14sp       | SemiBold   |

## Legacy Material 2 aliases (still in widespread use in the codebase)

These map to MML tokens above:

| M2 alias    | Maps to          |
|---|---|
| `H1`        | `DisplayXl`      |
| `H2`        | `HeadingLg`      |
| `H3`        | `HeadingDefault` |
| `H4`        | `DisplayXl`      |
| `H5`        | `HeadingLg`      |
| `H6`        | `HeadingDefault` |
| `Subtitle1` | `HeadingSm`      |
| `Subtitle2` | `HeadingXs`      |
| `Body1`     | `BodyDefault`    |
| `Body2`     | `BodySm`         |
| `Button`    | `LabelDefault`   |
| `Caption`   | `SubtitleSm` (Regular weight) |
| `Toolbar`   | `HeadingDefault` |

## Implication for prototype generation

Designers never write Compose `TextStyle` code in a TSX prototype. Typography is fixed by the iframe contents. This file exists so the agent can:

1. Reason about the visual hierarchy when discussing a screen with the designer.
2. Reject prompts like *"use Roboto"* or *"24px Open Sans"* with the correct rationale (*"Android DS uses Manrope; the closest token to 24px is `HeadingLg` at 24sp"*).
3. Coordinate with the DS team when a designer asks for a typography token that doesn't exist in this list.

## Anti-patterns

- ❌ Don't reference `typography.web.*` tokens — those exist in `ds-core` for the web stack.
- ❌ Don't suggest Open Sans, Roboto, or any font outside Manrope.
- ❌ Don't generate React `<Typography>` components in an Android prototype — the prototype is a thin `<AndroidPreview />` wrapper, no React text rendering.
