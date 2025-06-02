# OnyxFx Switcher

**A Custom Toggle Switch Component for JavaFX**

Tired from swtiching to children under VBox, HBox, Panes ? Too much work to transfer data? 
The **OnyxFx Switcher** is a modern and fully customizable toggle switch designed for JavaFX applications. It offers a smooth and elegant alternative to standard checkboxes, suitable for use in themes, settings panels, or any UI element requiring a pagination toggle.

---

## ✨ Features

- ✅ Sleek and minimalist toggle design
- ✅ Fully stylable via CSS
- ✅ Built-in support for hover, focus, and active states
- ✅ Lightweight and easily embeddable in any FXML layout
-  Smooth animations for state changes --- WIP

---

## 🧰 Technologies Used

- JavaFX 19+
- FXML
- Pure Java (no external UI libraries)

---

## 📦 Installation

### Maven

```xml
<dependency>
    <groupId>com.onyxfx</groupId>
    <artifactId>ofx-graphics</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.onyxfx:ofx-graphics:1.0.0'
```

---

## 🚀 Usage

### In FXML

```xml
<onyxfx.controls.Switcher fx:id="darkModeSwitcher" />
```

### How to use ?:

```java
darkModeSwitcher.setIndex(#Number of which child inside it). Thats it :)
```

---

## 📝 Notes

- Designed to integrate cleanly with other OnyxFx components.
- Can be styled using custom themes and variables in CSS.

---

## 📄 License

MIT License — (c) 2025 Mu7ammedDev159
