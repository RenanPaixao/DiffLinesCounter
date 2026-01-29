# Changelog

## [2.3.0] - Jan 29, 2026
### Added
- **Branch comparison**: Compare current branch against a configurable target branch instead of just HEAD
- **Settings panel**: Configure default target branch in Settings → Tools → Diff Lines Counter
- **Per-branch overrides**: Set different target branches for different source branches (persistent)
- **Branch selector popup**: Click the widget text to select target branch with searchable dropdown
- **Refresh button**: Dedicated refresh icon next to the diff count
- **File save listener**: Auto-refresh on file save
- **Branch change listener**: Auto-refresh when switching branches

### Changed
- Display format changed to `→branch: +N:-N` showing target branch and changes
- Now compares against remote branch by default, falls back to local with warning
- Includes uncommitted (staged + unstaged) changes in the diff count

### Removed
- Removed periodic 30-second refresh (now event-driven)
- Removed files changed count from display

## [2.2.7] - Jun 13, 2024
### Added
- Structured Gradle properties using buildSrc.
- Minor bug fixes and optimizations

## [2.2.6] - Jun 12, 2024
### Added
- Created a shortcut `Alt + Shift + D` for refreshing the Git repository status.
  - Created a new action to the Git menu for quick refresh.
- Corrected until-build version to 241.*
- Implemented Detekt for static code analysis.

## [2.2.5] - Jun 11, 2024
### Fixed
- Corrected until-build version to 233.*

## [2.2.4] - Jun 11, 2024
### Changed
- Migrated to using catalogs for dependency version management.
- Updated compatibility version to support IntelliJ 2023.3.1 and future versions.

## [2.2.3] - Oct 17, 2023
### Changed
- Version bump v2.2.3
- Reset counter if Git repo is removed.
- Widget availability check in `isAvailable` method.
- Update README.

## [2.1.3] - Oct 16, 2023
### Changed
- Version bumped to v2.1.3
### Fixed
- Resolved threading issue with Event Dispatch Thread (EDT).
    - Issue reported in #4.

## [2.1.2] - Oct 15, 2023
### Added
- CI/CD pipeline for checks and releases.
- Create tests.
- Version bump v2.1.2.
- Update gitignore.
- Update sources image.
- Refactor action listeners.

## [2.0.2] - Oct 13, 2023
### Changed
- Replaced asynchronous operations.

## [2.0.1] - Oct 13, 2023
### Fixed
- Adjust file change metrics for accurate reporting on macOS.

## [2.0.0] - Oct 13, 2023
### Initial Release
- We're Live! 🎈
