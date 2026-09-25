# Candidate Instructions — Senior Java Developer

## Scenario

Your team owns a high-volume OAuth token-validation service.

- Peak traffic: 10,000 requests per second
- Availability objective: 99.99%
- Normal downstream authorization latency: approximately 100 ms
- Incident behavior: downstream calls occasionally take longer than 10 seconds
- Last incident: request threads were exhausted and the token service became unavailable
- Security requirement: raw tokens and customer identifiers must never appear in logs

The supplied implementation passes its small baseline test suite, but it is not production-ready.

## Assignment

Improve the service so that it fails safely and predictably when its downstream dependency is slow or unavailable.

You have 75 minutes: approximately 55 minutes for coding and testing, followed by 20 minutes for review and discussion.

Address as much as you reasonably can:

1. Bound downstream latency with an appropriate timeout.
2. Add circuit-breaker behavior without introducing a third-party library.
3. Decide whether retries are appropriate, and implement a safe policy if they are.
4. Ensure logs do not expose tokens or customer identifiers.
5. Add or improve tests for normal and failure behavior.
6. Add useful operational metrics through the supplied `MetricsRecorder` abstraction.
7. Document incomplete work and production tradeoffs in `ARCHITECTURE_NOTES.md`.
8. Complete `SUBMISSION.md` with a concise summary of your changes and remaining risks.

You may refactor any starter code and add new files. Do not call a real network service.

## AI policy

You may use an AI coding assistant. You remain responsible for every change. Be prepared to explain:

- the context and constraints you gave the assistant;
- what generated output you rejected or changed;
- how you verified correctness and security;
- what you would require before approving the implementation for production.

Do not place confidential data, credentials or proprietary source code in an external AI tool.

## Suggested work order

1. Run the baseline tests.
2. Inspect the service and identify the highest-risk defects.
3. State your plan before changing code.
4. Implement one small, testable improvement at a time.
5. Add deterministic tests as behavior changes.
6. Run the complete test suite and review your diff.

## Discussion topics

After coding, expect to discuss:

- retry amplification and retry storms;
- timeout-budget selection;
- circuit-breaker state transitions;
- concurrency and thread safety;
- fail-open versus fail-closed behavior;
- dashboards, alerts, canary deployment and rollback;
- how you would deploy and monitor the change safely.

## Definition of done

- The code compiles on Java 17.
- `run-tests.sh` or `run-tests.ps1` succeeds.
- New behavior is covered by tests.
- Security-sensitive values are not logged.
- Key design decisions are recorded in `ARCHITECTURE_NOTES.md`.
- `SUBMISSION.md` accurately states completed and incomplete work.
