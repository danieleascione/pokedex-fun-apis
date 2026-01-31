# Java Installation Guide

Java 21+ is required. I recommend installing Java 25.

## Installation

Install using your OS package manager:

| OS            | Command                                         |
|---------------|-------------------------------------------------|
| macOS         | `brew install openjdk@25`                       |
| Ubuntu/Debian | `sudo apt install openjdk-25-jdk`               |
| Fedora        | `sudo dnf install java-25-openjdk`              |
| Windows       | `winget install EclipseAdoptium.Temurin.25.JDK` |

## Post-installation

### macOS

After installing with Homebrew, add Java to your PATH by adding this to your shell profile (`~/.zshrc` or `~/.bashrc`):

```bash
export PATH="/opt/homebrew/opt/openjdk@25/bin:$PATH"
```

Then reload your shell:

```bash
source ~/.zshrc  # or source ~/.bashrc
```

### Ubuntu/Debian

If you have multiple Java versions installed, select Java 25 as the default:

```bash
sudo update-alternatives --config java
```

### Fedora

If you have multiple Java versions installed, select Java 25 as the default:

```bash
sudo alternatives --config java
```

### Windows

The Temurin installer should configure `JAVA_HOME` and update your `PATH` automatically. If it didn't, add these environment variables manually:

1. Set `JAVA_HOME` to `C:\Program Files\Eclipse Adoptium\jdk-25`
2. Add `%JAVA_HOME%\bin` to your `PATH`

## Verify installation

After installation, verify Java is working:

```bash
java --version
```

You should see output indicating Java 25.

## Alternative: SDKMAN

On macOS and Linux you can use [SDKMAN](https://sdkman.io/) which handles PATH configuration automatically.