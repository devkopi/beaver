<div align="center">

# Beaver Documentation
### Setup guides, permissions & configuration reference

<img src="https://i.imgur.com/9mJBZp1.png" alt="Beaver Docs" width="100%">

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net)
[![Author](https://img.shields.io/badge/Developer-ccoding-blue?style=for-the-badge&logo=github)](https://github.com/devkopi)
[![Version](https://img.shields.io/badge/Version-1.0.0-green?style=for-the-badge)](https://github.com/devkopi/Beaver)

</div>

---

## Getting Started

1. **Requirements:** Make sure your Minecraft server is running on **Java 17 or higher** (compatible with Paper, Spigot, and modern forks).
2. **Installation:** Drop the `Beaver.jar` file into your server's `plugins/` directory.
3. **Startup:** Start or reload your server to generate the default configuration files (`config.yml` and `messages.yml`).

---

## Permissions & Security

Beaver uses a lightweight permission node system to secure server maintenance controls and access.

| Permission Node | Default | Description |
| :--- | :--- | :--- |
| `beaver.admin` | `op` | Allows administration of Beaver maintenance system. |
| `beaver.bypass` | `op` | Allows joining the server during maintenance. |

---

## Configuration Guide

### `config.yml`
Configure core settings, MOTD changes during maintenance, and Discord webhook integration. You can use `{center}` at the beginning of any MOTD line to center it perfectly in the server list:

```yaml
#################################################################
#                                                               #
#                        BEAVER PLUGIN                          #
#                    Developed by ccoding                       #
#          GitHub: [https://github.com/devkopi](https://github.com/devkopi)                   #
#                                                               #
#################################################################

# Beaver configuration file
# Documentation: [https://github.com/devkopi/Beaver](https://github.com/devkopi/Beaver)
# Tip: You can use {center} at the beginning of any MOTD line to center it!

debug-mode: false

motd:
  enabled: true

  normal:
    - "{center}&6&lBEAVER &8&l» &eNext-Gen Server Management &f&o(v1.0.0)"
    - "{center}&7Running smoothly • Use &b/maintenance &7to test!"

  maintenance:
    - "{center}&6&lBEAVER &8&l» &4&lSYSTEM UNDER MAINTENANCE"
    - "{center}&cServer temporarily closed for optimizations. Back soon!"

discord:
  enabled: true
  webhook-url: "LINK_WEBHOOK_HERE"
  send-embed: true

```

### `messages.yml`

Customize in-game messages, kick screens, and dynamic Discord embeds for instant and scheduled triggers:

```yaml
#################################################################
#                                                               #
#                     BEAVER MESSAGES CONFIG                    #
#                    Developed by ccoding                       #
#          GitHub: https://github.com/devkopi                   #
#                                                               #
#################################################################

prefix: "&6&lBeaver &7»"

no-permission: "&cYou don't have permission."

plugin:
  reloaded: "&aConfiguration reloaded successfully."

maintenance:
  enabled: "&aMaintenance mode enabled."
  disabled: "&cMaintenance mode disabled."

  status:
    enabled: "&aEnabled"
    disabled: "&cDisabled"

  kick-message: "&cThe server is currently under maintenance."

  discord:
    # Webhook notification when maintenance is enabled instantly (/maintenance on)
    enabled-alert:
      content: "@everyone The server has entered maintenance mode!"
      use-everyone: true
      embed:
        title: "🛡️ Maintenance Enabled"
        description: "The server is temporarily closed for maintenance and optimization tasks."
        color: "#FF5733"
        image-url: "https://i.imgur.com/MCdjJow.png"
        thumbnail-url: ""
        footer: "Beaver Development • ccoding"

    # Webhook notification when maintenance is scheduled (/maintenance schedule <time>)
    schedule:
      content: "@everyone The server will enter maintenance mode soon!"
      use-everyone: true
      embed:
        title: "⏳ Maintenance Scheduled"
        description: "The server will enter maintenance mode in **%time%**."
        color: "#F1C40F"
        image-url: "https://i.imgur.com/MCdjJow.png"
        thumbnail-url: "https://i.imgur.com/XaiaWzP.png"
        footer: "Beaver Development • ccoding"
```

---

## Open Source & Contribution Policy

This project is open-source, and you are completely free to propose new ideas, feature suggestions, or improvements either in writing or via code contributions.

However, please keep the following guidelines in mind:

* **Identity & Ownership:** Impersonating the developer, claiming authorship, or attempting to take ownership/rebrand the project as your own is strictly prohibited.
* **Implementation Control:** Any proposed changes or features will remain suggestions and will not be officially implemented into the plugin until explicitly authorized and reviewed by the project author (`ccoding`).