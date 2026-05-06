# ds-eng-android

Claude Code plugin that teaches the agent how to generate **Android prototypes** inside `Codefied/housecall-prototype`.

This plugin is the Android counterpart to `ds-eng:housecall-ui` (the web plugin in `Codefied/housecall-ui`). It does NOT generate Kotlin code — Kotlin lives in this same repo's `housecall-ui-android/` KMP module. This plugin generates the **TSX wrappers** that designers see in the web prototyper, which embed the deployed Compose Wasm preview via iframe.

## What it does

When a designer prompts the prototyper with something Android-related (`/prototype "create an Android scheduling screen"`), this plugin:

1. Detects the Android intent.
2. Generates a TSX prototype that uses `<AndroidPreview src="...?screen=..." />` instead of `housecall-ui/dist/...` imports.
3. Sets `platform: "android"` in the prototype's `meta.json`.
4. Skips the housecall-ui typecheck path; the AndroidPreview component is the only consumer-side dependency.

## Required setup in `Codefied/housecall-prototype`

```
/plugin marketplace add Codefied/housecall-ui-android
/plugin install ds-eng-android@android-design-system-marketplace
```

And `<housecall-prototype>/.claude/settings.json` must enable the plugin:

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

## See also

- [Spec doc](https://housecall.atlassian.net/wiki/spaces/DS/pages/4180017540) (POC report)
- `Codefied/housecall-ui` (web counterpart marketplace)
- [`AndroidPreview.tsx`](https://github.com/Codefied/housecall-prototype/blob/main/src/components/AndroidPreview/AndroidPreview.tsx) component in the prototyper (consumer side)
