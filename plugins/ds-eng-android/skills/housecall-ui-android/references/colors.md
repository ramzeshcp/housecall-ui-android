# Colors — Android (`HcColors`)

> **Source of truth for Android colors.** Do NOT consult `ds-core:guidelines` for colors in an Android prototype — its palette references are web-leaning. The values below were lifted verbatim from `core/ui/src/main/res/values/colors.xml` and ported to `commonMain` for the externalized library.

## Categories

The `HcColors` object groups tokens by semantic category. Designers refer to these names; the agent should reflect them back when discussing a screen.

### Primary
| Token              | Hex        |
|---|---|
| `Primary.Main`           | `#0057FF` |
| `Primary.Light`          | `#B2CDFF` |
| `Primary.LightRevamped`  | `#E3ECFF` |
| `Primary.Dark`           | `#0046CC` |
| `Primary.OnPrimary`      | `#FFFFFF` |
| `Primary.Container`      | `#E3ECFF` |
| `Primary.OnContainer`    | `#15181D` |

### Secondary
| Token                | Hex        |
|---|---|
| `Secondary.Main`         | `#6B7487` |
| `Secondary.Light`        | `#CAD1E0` |
| `Secondary.Dark`         | `#464E5A` |
| `Secondary.OnSecondary`  | `#FFFFFF` |
| `Secondary.Container`    | `#F2F4F7` |
| `Secondary.OnContainer`  | `#15181D` |

### Success / Warning / Error / Info
| Token              | Hex        |
|---|---|
| `Success.Main`         | `#00A344` |
| `Success.Light`        | `#B9F6CA` |
| `Success.Dark`         | `#007D33` |
| `Warning.Main`         | `#BF8600` |
| `Warning.Light`        | `#FFE082` |
| `Warning.Dark`         | `#8A6100` |
| `Error.Main`           | `#D81860` |
| `Error.Light`          | `#FFDDE9` |
| `Error.Dark`           | `#A01045` |
| `Info.Main`            | `#B2CDFF` |
| `Info.Light`           | `#E3ECFF` |
| `Info.Dark`            | `#0057FF` |

### Surface
| Token                          | Hex        |
|---|---|
| `Surface.Default`                  | `#FFFFFF` |
| `Surface.Background`               | `#FAFBFF` |
| `Surface.BackgroundSecondary`      | `#F2F6FD` |
| `Surface.Elevated01`               | `#FFFFFF` |
| `Surface.Elevated02`               | `#FFFFFF` |
| `Surface.Inverse`                  | `#15181D` |
| `Surface.OnInverse`                | `#FFFFFF` |
| `Surface.ButtonSecondary`          | `#E3ECFF` |
| `Surface.ButtonNeutral`            | `#F2F4F7` |

### Text
| Token                       | Hex            |
|---|---|
| `Text.OnSurfacePrimary`         | `#15181D`      |
| `Text.OnSurfaceSecondary`       | `#6B7487`      |
| `Text.OnSurfaceDisabled`        | `#CAD1E0`      |
| `Text.OnSurfaceHint`            | `#618C96AA` (38% alpha) |

### Border
| Token              | Hex        |
|---|---|
| `Border.Subtle`        | `#E1E6F2` |
| `Border.Default`       | `#CAD1E0` |
| `Border.Emphasis`      | `#8C96AA` |
| `Border.Divider`       | `#E1E6F2` |

### Grey scale
| Token        | Hex        |
|---|---|
| `Grey.c50`   | `#FAFBFF` |
| `Grey.c100`  | `#F7F9FF` |
| `Grey.c150`  | `#EDF1FA` |
| `Grey.c200`  | `#EDF1FA` |
| `Grey.c300`  | `#E1E6F2` |
| `Grey.c400`  | `#CAD1E0` |
| `Grey.c500`  | `#AEB8CC` |
| `Grey.c600`  | `#8C96AA` |
| `Grey.c700`  | `#6B7487` |
| `Grey.c800`  | `#464E5A` |
| `Grey.c900`  | `#23272F` |

### Common
| Token            | Hex        |
|---|---|
| `Common.Black`   | `#15181D` (note: NOT pure `#000000`) |
| `Common.White`   | `#FFFFFF` |

## Implication for prototype generation

The designer never picks raw hex values for an Android prototype. Colors are baked into the `Hc*` components inside the iframe. This file exists so the agent can:

1. Validate color requests (*"use brand blue for the button"* → confirm `Primary.Main` `#0057FF`).
2. Reject mismatched values (*"use #1976D2"* → that's MUI's primary, not HCP Android — explain and offer `Primary.Main`).
3. Coordinate with the DS team if a designer asks for a token that doesn't exist.

## Anti-patterns

- ❌ Don't suggest CSS color names (`'red'`, `'blue'`) — Android tokens are semantic, not literal.
- ❌ Don't write inline `Color(0xFFXXXXXX)` in any prototype — the prototype is just a `<AndroidPreview />` wrapper.
- ❌ Don't use `Common.Black` interchangeably with `#000000`. HCP Android black is `#15181D`.
