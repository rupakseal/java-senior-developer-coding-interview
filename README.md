# Senior Java Developer Coding Interview

This repository contains a 75-minute, production-oriented coding exercise for a Senior Java Developer. You will harden a deliberately unsafe OAuth token-validation service while explaining your technical choices.

Start with [CANDIDATE_INSTRUCTIONS.md](CANDIDATE_INSTRUCTIONS.md). The project intentionally uses only Java 17 standard-library APIs, so it can run without downloading dependencies.

## Quick start

macOS/Linux:

```bash
./run-tests.sh
```

Windows PowerShell:

```powershell
./run-tests.ps1
```

You may also import the repository into any Java IDE or run it as a Maven project.

## Repository structure

```text
src/main/java       production code
src/test/java       dependency-free test harness
CANDIDATE_INSTRUCTIONS.md
ARCHITECTURE_NOTES.md
EVALUATION_AREAS.md
SUBMISSION.md
```

## Important

The starter implementation is intentionally incomplete and unsafe for production. Finding, prioritizing, fixing, and testing the problems are all part of the exercise.

## What this exercise evaluates

- Java 17 design and implementation skills
- concurrency and failure-mode reasoning
- secure handling of tokens and customer data
- deterministic unit testing
- observability and production readiness
- responsible use and review of AI-generated code

This is not an algorithm puzzle. A focused, well-tested solution is better than a large unfinished rewrite.
