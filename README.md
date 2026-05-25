# Layout Corrector

[English](#english) | [Русский](#русский)

---

## English

A Minecraft Fabric client-side mod that automatically corrects chat commands typed in the wrong keyboard layout and supports custom Russian command aliases.

### Features

- **Keyboard Layout Correction:** Automatically translates commands typed in Cyrillic back to Latin (e.g. `.ызфцт` -> `/spawn` or `/ызфцт` -> `/spawn`).
- **Command Aliases Translation:** Allows you to configure custom Russian command aliases that automatically convert to their English counterparts upon sending (e.g., `/спавн` -> `/spawn`, `/ах` -> `/ah`, `/ртп` -> `/rtp`, `/ес` -> `/ec`).
- **Interactive GUI:** Easy-to-use configuration menu to enable/disable features, add custom alias mappings, and display active mappings with direct deletion buttons.

### Controls

- **`J`:** Open the Configuration GUI screen.

### Configuration

The mod configuration is saved to `.minecraft/config/layoutcorrector.json` and can be adjusted through the GUI:
- **Enabled:** Global toggle for the mod.
- **Layout Correction:** Toggle the automatic QWERTY layout correction.
- **Alias Creator:** Text fields to assign any custom Russian word to an English command mapping.

---

## Русский

Клиентский Fabric-мод для Minecraft, который автоматически исправляет команды, набранные на неверной раскладке клавиатуры, и поддерживает настраиваемые русские алиасы (псевдонимы) для команд.

### Возможности

- **Исправление раскладки:** Автоматически переводит команды, набранные на кириллице, в латиницу (например, `.ызфцт` -> `/spawn` или `/ызфцт` -> `/spawn`).
- **Алиасы на русском языке:** Позволяет настроить русские псевдонимы для команд, которые будут автоматически переводиться при отправке (например, `/спавн` -> `/spawn`, `/ах` -> `/ah`, `/ртп` -> `/rtp`, `/ес` -> `/ec`).
- **Удобный интерфейс:** Простое и интуитивное графическое меню для включения/выключения функций, добавления кастомных алиасов и отображения списка активных связок с возможностью их удаления в один клик.

### Управление

- **`J`:** Открытие графического интерфейса настроек.

### Настройка

Файл конфигурации сохраняется в `.minecraft/config/layoutcorrector.json` и настраивается через игровое меню:
- **Enabled:** Включение/выключение работы мода.
- **Layout Correction:** Включение/выключение автоисправления QWERTY раскладки.
- **Alias Creator:** Поля ввода для назначения любого русского слова в качестве алиаса для английской команды.
