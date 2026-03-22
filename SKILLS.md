# Skill: Android security review
# Trigger: user asks to "review", "audit", or "check security of" a file, function, or PR diff.
# Purpose: step-by-step procedure for performing a security review on AI-generated Android code.

---

## When to apply this skill

Apply this skill when the user says any of the following (or close variants):
- "review this for security"
- "audit this file"
- "security check on this PR / diff / function"
- "what are the security issues here"
- "run a security review"

Do NOT apply for general code generation, the rules in `GEMINI.md` handle that.

---

## Step 1 — Identify scope

Before reviewing, confirm what you are looking at:
- A single function → focused review on that function's inputs, outputs, and side effects.
- A file → full file review across all 10 rule categories.
- A diff / PR → review only changed lines, but flag if a change breaks something safe that
  existed before.

State the scope explicitly at the start of your review output.

---

## Step 2 — Structured review pass

Work through each category below in order. For each one, state:
- `PASS` — no issues found in this category for the given scope.
- `WARN` — potential issue, depends on context not visible in the snippet.
- `FAIL` — clear violation present.

| # | Category              | Check for |
|---|-----------------------|-----------|
| 1 | Secrets               | Hardcoded keys, tokens, passwords anywhere in the code |
| 2 | Storage               | Plain SharedPreferences for sensitive data; raw key material in files |
| 3 | Manifest exports      | Missing `android:exported`; intent-filter without explicit export |
| 4 | Network               | `http://` URLs; trust-all TrustManager; hostname verification disabled |
| 5 | WebView               | JS enabled without justification; `addJavascriptInterface` present |
| 6 | Intents               | Implicit intents carrying sensitive data; no resolving component check |
| 7 | Logging               | Sensitive data in `Log.*` calls; logging not gated on `BuildConfig.DEBUG` |
| 8 | Dependencies          | Unpinned versions (`+`); unfamiliar artifact IDs |
| 9 | Build config          | `isMinifyEnabled = false` in release; `debuggable = true` in release |
|10 | Input / SQL           | String-interpolated SQL; unvalidated deep link / intent parameters |

---

## Step 3 — For each FAIL or WARN

Produce a finding block in this format:

```
FINDING [severity: HIGH | MEDIUM | LOW]
Rule:     R<N> — <rule name>
Location: <file>:<line> or <function name>
Issue:    One sentence describing exactly what is wrong.
Fix:      Concrete corrected code snippet or config change.
```

---

## Step 4 — Summary

End the review with a one-paragraph summary:
- Total findings by severity (e.g. "2 HIGH, 1 MEDIUM, 3 LOW").
- The single most important fix to make first.
- Whether the code is safe to ship as-is (yes / no / with caveats).

---

## What to do when uncertain

If a finding could be a WARN or a FAIL depending on context you cannot see:
1. Mark it `WARN`.
2. State exactly what additional context would resolve it.
3. Add a `// TODO(security): confirm this data is non-sensitive` comment at the location.
