# Prototyping rules — Android-specific

## When to use AndroidPreview

Always, for any prototype tagged `platform: "android"`.

## Default frame size

Pixel 9 logical: **412 x 915 dp**. This matches what Android designers most commonly target and is the size proven in the GDSP-199 POC. Override only when:

- Prompt explicitly mentions tablet → use 800x1280 (Pixel C-class) or pass through whatever is reasonable
- Prompt explicitly mentions a foldable → use 673x841 (Pixel Fold) for outer screen
- Prompt mentions a small phone → use 360x780 (legacy Android)

## When to ask for a new preview screen

If the designer prompt names a screen/flow that doesn't yet have a `?screen=X` mapping in the preview app (see `compose-component-catalog.md`):

1. Inform the designer: *"The X screen isn't shipped in the preview yet. The closest available demo is `?screen=text-fields`. Want me to use that as a placeholder?"*
2. If yes, generate the prototype with the placeholder URL and add a comment block:
   ```tsx
   // TODO(DS-team): a `scheduling` Compose showcase needs to be added to
   // `Codefied/housecall-ui-android/preview/src/wasmJsMain/...`. This prototype
   // is currently using `?screen=text-fields` as a placeholder.
   ```
3. If no, suggest opening a request to the DS team to author the missing screen.

## When to use which URL

- **For shareable prototypes** (will be merged to `share` branch): use the production Netlify URL. Stable, cached, public.
- **For local DS dev** (designer is iterating with a DS engineer who is running the preview locally): use `http://localhost:8080/?screen=...`. This won't work for `share` PRs but lets the designer see live changes.

The skill should default to the production URL unless the designer explicitly mentions running the preview locally.

## How `meta.json` differs for Android

```diff
  {
    "name": "...",
    "id": "...",
+   "platform": "android",
    "createdAt": "...",
    "label": "...",
    "versions": []
  }
```

The `platform` field is additive — existing web prototypes don't have it (defaults to `"web"` in any consumer code that filters). Adding it is non-breaking.

## How versions work for Android prototypes

Same as web: `/version` copies `index.tsx` to `vN.tsx` and appends to `versions[]` in `meta.json`. Since the prototype TSX is just a thin `<AndroidPreview />` wrapper, versions typically only differ by the `src` URL params (`?screen=A` vs `?screen=B`) or iframe dimensions.

## Sharing Android prototypes

Same flow as web: `/share-prototype` pushes the branch to the `share` branch. The shareable URL points at the deployed `housecall-prototype` site (Vercel/Netlify), and the iframe inside it points at the deployed `housecall-ui-android.netlify.app`. Both are independently versioned and deployed.

## When NOT to invoke this skill

- Web prototypes (no Android keyword in the prompt)
- Modifying `housecall-ui-android` itself (the lib repo) — that work happens in a different repo with different tooling
- Authoring new Compose `@Composable` screens in the preview app — that's Kotlin work in the lib repo, not TSX work here

## Coordination with `ds-core:guidelines`

`ds-core` is multi-platform and contains the canonical typography scale, color tokens, spacing rules, and component decision guide. **Always trust it for foundations.** This skill never overrides token values; it only specifies *how to embed the preview* (the iframe pattern), not what colors/fonts/spacing to use. Those come from `ds-core`.
