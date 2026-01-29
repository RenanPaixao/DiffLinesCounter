# LinesDiffTracker
<!-- start description -->
LinesDiffTracker helps you assess the volume of code modifications before submitting a Pull Request (PR), ensuring that PRs remain streamlined and manageable. It's a plugin for JetBrains IDEs that shows the difference between your current working state and a configurable target branch. With just a glance, you can see how many lines have been added or removed compared to your target branch (e.g., `develop` or `main`).

## Features

- **Branch comparison**: Compare your current branch against a configurable target branch
- **Includes all changes**: Shows committed + uncommitted (staged and unstaged) changes
- **Configurable default branch**: Set your preferred target branch in Settings
- **Per-branch overrides**: Set different target branches for different feature branches
- **Click to select target**: Click the widget to change the target branch with a searchable popup
- **Manual refresh**: Click the refresh icon to update the count
- **Auto-refresh**: Updates on file save and branch switch
- **Remote-first**: Compares against remote branch when available, falls back to local

## Installation

1. Navigate to the JetBrains Plugin Repository from your IDE or use the [link here](https://plugins.jetbrains.com/plugin/22879).
2. In the search bar, type `LinesDiffTracker` and hit enter.
3. Click on `Install` to add the plugin to your IDE.
4. Restart your IDE for the changes to take effect.

## Usage

Upon installation, a widget appears in the status bar showing: `→develop: +120:-22`

- **Click the text** to open a searchable popup and select a different target branch
- **Click the refresh icon** to manually update the count
- Go to **Settings → Tools → Lines Diff Tracker** to set the default target branch

The widget shows:
- `→branch`: The target branch being compared against
- `+N`: Lines added compared to target
- `-N`: Lines removed compared to target

Yellow text indicates the remote branch is unavailable and local branch is being used.

## Contributing

Contributions are welcome! Feel free to open an Issue or create a Pull Request.

<a href='https://plugins.jetbrains.com/plugin/22879'>
  <img alt='Get from Marketplace' src='images/button.svg' width='250'/>
</a>
<!-- end description -->