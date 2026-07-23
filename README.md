# AuthSystem

[![Version](https://img.shields.io/badge/version-1.0.1-blue)](https://github.com/Impopite/AuthSystem/releases)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21+-orange)](https://adoptium.net/)
[![Paper](https://img.shields.io/badge/Paper-1.21+-red)](https://papermc.io/)

A secure and lightweight authentication plugin for Minecraft servers, built with Paper API.

---

## Features

- **Registration & Login** — Secure player authentication with BCrypt password hashing
- **Premium Authentication** — Toggle online-mode login for Mojang verified players
- **MySQL Storage** — Persistent data with HikariCP connection pooling
- **Brute-force Protection** — Configurable kick or timed ban after too many failed attempts
- **Authentication Timeout** — Automatic disconnection for players who don't authenticate in time
- **Multilingual** — Built-in English and Italian language support, easily extensible
- **Admin Tools** — Password reset, player unregister, IP lookup, and config reload
- **Developer API** — Lightweight API for integration with other plugins via JitPack
- **Update Checker** — Notifies admins of new releases on startup

---

## Requirements

- Java **21+**
- Paper **1.21+** (Spigot/Bukkit compatible)
- MySQL **5.7+** or MariaDB **10.3+**

---

## Installation

1. Download the latest `AuthSystem.jar` from the [releases page](https://modrinth.com/plugin/authenticationsystem)
2. Place the JAR in your server's `plugins/` folder
3. Start or restart the server
4. Edit `plugins/AuthSystem/config.yml` with your database credentials
5. Reload or restart the server to apply changes

---

## Documentation

Full documentation including commands, permissions, configuration, and API reference:

**[DOCUMENTATION](https://impoo.gitbook.io/authenticationsystem)**

---

## Bug Reports & Feature Requests

Found a bug or have a suggestion? Open an issue on [GitHub Issues](https://github.com/Impopite/AuthSystem/issues).

---

## License

This project is licensed under the [MIT License](LICENSE).

---

Developed by **zImpoo**

- Telegram: [@tentava](https://t.me/tentava)
- Discord: [Impopite](https://discord.com/users/Impopite)
