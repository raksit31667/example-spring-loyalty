# 5. Use split.io as a feature toggle

Date: 2022-01-08

## Status

Accepted

## Context

References: [Reduce tangled code branches with trunk-based development](https://www.split.io/solutions/trunk-based-dev/)

This project is using trunk-based development reducing cycle time, speeding up the journey from code
to production. It allows teams to move faster by integrating changes continually, and prevents
long-lived branches, where each branch can cause merge conflicts.

The only way to ensure that the trunk (i.e. `master` branch) is always releasable is to hide
unfinished features behind flags that are turned off by default. We can build a new feature by
turning the flag off and deploying to trunk at any time, preventing merge conflicts before a new
feature is completed.

## Decision

We use [split.io](https://www.split.io) as a feature toggle tool.

## Consequences

Split makes trunk-based development seamless with feature flags to keep code dormant and built-in
alerting to warn you when newly released code should be turned off.