# AndroidPreview component reference

`<AndroidPreview />` is the React component in `Codefied/housecall-prototype/src/components/AndroidPreview/AndroidPreview.tsx` that embeds the deployed Compose Wasm preview from `housecall-ui-android` in an iframe.

## Source

The component lives in `housecall-prototype` (consumer side), not in this plugin. This file documents how to *use* it from generated prototypes.

## Props

| Prop | Type | Default | Purpose |
|---|---|---|---|
| `src` | `string` | `https://housecall-ui-android.netlify.app/` | URL of the deployed preview. Override per-prototype to select a specific screen via URL params. |
| `title` | `string` | `"HCP Android DS preview"` | Iframe accessible title (shown to screen readers, used by some browsers' iframe inspectors). |
| `width` | `number` | `412` | Logical dp width. Pixel 9 default. |
| `height` | `number` | `915` | Logical dp height. Pixel 9 default. |

## Supported URL params (exposed by the preview app)

The preview app reads `window.location.search` and routes to a `@Composable` showcase based on `?screen=X`.

| Param | Allowed values | Effect | Status |
|---|---|---|---|
| `screen` | `text-fields` | Renders the HcTextField variants showcase (label, placeholder, helper, error, password, outlined, char-limit). | ✅ shipped |
| `screen` | `builder` | Renders an arbitrary layout described by `?layout=<URL-encoded JSON>`. **Use this for any custom designer composition.** Full schema: [builder-dsl.md](./builder-dsl.md). | ✅ shipped |
| `screen` | `default` (or omitted) | Same as `?screen=text-fields`. | ✅ shipped |
| `screen` | `scheduling` | Renders a scheduling screen mockup. | 🟡 pending — coordinate with DS team to add the showcase |
| `screen` | `checkout` | Renders a checkout screen mockup. | 🟡 pending |
| `layout` | URL-encoded JSON (a `LayoutNode` tree) | Used together with `?screen=builder`. Interpreted by the preview app's DSL renderer at runtime. | ✅ shipped |

If the preview app does not yet support a pre-built `?screen=X` showcase the prompt asks for:

1. **First, ask:** can the prompt be expressed via `?screen=builder` + a layout JSON? If yes, use the builder DSL — no preview rebuild needed.
2. If the prompt requires a 🟡 component (HcButton, HcCheckbox, etc.) that's not yet in the externalized lib, fall back to `?screen=builder` with what IS available + add a `TODO(DS-team)` comment listing the missing components.
3. Only as a last resort, suggest filing a request to add a new pre-built showcase.

## Examples

### Default (text fields showcase)

```tsx
<AndroidPreview />
```

### Specific pre-built screen

```tsx
<AndroidPreview src="https://ramzeshcp.github.io/housecall-ui-android/?screen=text-fields" />
```

### Custom composition via builder DSL

```tsx
const layout = {
  type: 'column',
  padding: 16,
  spacing: 16,
  children: [
    { type: 'hc-text-field', label: 'Email', outlined: true },
    { type: 'hc-text-field', label: 'Password', outlined: true, passwordToggleEnabled: true },
  ],
}
const src = `https://ramzeshcp.github.io/housecall-ui-android/?screen=builder&layout=${encodeURIComponent(JSON.stringify(layout))}`

<AndroidPreview src={src} />
```

### Custom dimensions (rare — small phone simulation)

```tsx
<AndroidPreview width={360} height={780} />
```

### Pixel 9 Pro Max simulation

```tsx
<AndroidPreview width={428} height={926} />
```

## URLs by environment

| Environment | URL pattern |
|---|---|
| Production (Netlify, Codefied) | `https://housecall-ui-android.netlify.app/` |
| POC (personal repo, GitHub Pages) | `https://ramzeshcp.github.io/housecall-ui-android/` |
| Local dev (developer running the preview repo locally) | `http://localhost:8080/` |

The component default is the production URL. For local development of the preview itself, override `src` per-prototype.
