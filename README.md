# AuthSystem

> 🔐 **Advanced Authentication System for Paper & Spigot Servers**

AuthSystem is a modern authentication plugin built for Minecraft servers. It provides secure player authentication, MySQL storage, multilingual support, and a developer-friendly API to easily integrate authentication into your own plugins.

---

## 🚀 Features

### 🔑 Secure Authentication
Fast and reliable login & registration system with secure data validation.

### 💾 MySQL Support
Powered by **HikariCP** for high-performance database connection pooling.

### 🌍 Multilingual
Built-in language support with configurable translations (IT/EN).

### ⚙️ Fully Configurable
Simple YAML configuration for messages, settings, and database options.

### 📚 Developer API
Easily access authentication status and player data from your own plugins.

### 👥 Centralized User Management
Manage authenticated users efficiently across your server.

---

# 📦 Installation

1. Download the latest **AuthSystem.jar**
2. Place it inside your server's **plugins/** folder
3. Start or restart your server
4. Configure the plugin in the generated configuration files
5. You're ready to go!

---

# 🛠 Requirements

- Paper **1.21+**
- Java **21+**
- MySQL Database

---

# ✅ Compatibility

| Software | Supported |
|----------|-----------|
| Paper | ✅ |
| Spigot | ✅ |
| Bukkit | ✅ |

---

# 📚 Developer API

AuthSystem exposes a lightweight API that allows developers to:

- Check if a player is authenticated
- Access authentication data
- Integrate custom authentication workflows
- Manage user information

See the [documentation](https://impoo.gitbook.io/authenticationsystem)

## Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Impopite</groupId>
        <artifactId>AuthSystem</artifactId>
        <version>VERSION</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.Impopite:AuthSystem:VERSION'
}
```

## Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Impopite:AuthSystem:VERSION")
}
```
---

# 🐞 Reporting Bugs

Found a bug or have a feature request?

Open an issue on GitHub:

https://github.com/Impopite/AuthSystem/issues

---

# 📄 License

Licensed under the **MIT License**.

See the **LICENSE** file for more information.

---

# ❤️ Support

If you enjoy using **AuthSystem**, consider leaving a ⭐ on GitHub and sharing it with your community.

Developed with ❤️ by **zImpoo**

- Telegram → https://t.me/tentava
- Discord → https://discord.com/users/Impopite
