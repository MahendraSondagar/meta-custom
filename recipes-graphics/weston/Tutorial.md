
---

# Weston.ini Example for Touchscreen Calibration and Display Rotation (Yocto Tutorial)

## 📋 Overview
This tutorial explains how to customize the `weston.ini` file to:
- **Calibrate touchscreen input** (flip/rotate if needed)
- **Rotate display output** (90°, 180°, 270°)

Customization is done by using a **`weston_%.bbappend`** recipe in **Yocto Project**.

---

## 📁 Directory Structure

Organize your layers like this:

```bash
meta-custom/
└── recipes-graphics/
    └── weston/
        ├── files/
        │   └── weston.ini
        └── weston_%.bbappend
```

- `weston_%.bbappend` : The recipe that appends new config.
- `files/weston.ini` : Your custom Weston configuration.

---

## ✍️ Content of `weston_%.bbappend`

```bitbake
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://weston.ini"

do_install:append() {
    install -d ${D}${sysconfdir}/xdg/weston
    install -m 0644 ${WORKDIR}/weston.ini ${D}${sysconfdir}/xdg/weston/weston.ini
}
```

✅ What this does:
- Adds your local `weston.ini` into Yocto's fetch sources.
- During `do_install`, it places `weston.ini` into `/etc/xdg/weston/`.

---

## ✍️ Example `files/weston.ini`

Here’s a basic example with:
- 180° rotated screen
- Touch input calibration (flipped for 180°)

```ini
[core]
idle-time=0

[shell]
locking=false
panel-position=none

[output]
name=HDMI-A-1
transform=rotate-180

[input-method]
path=/usr/bin/weston-keyboard

[input-device]
# Example touchscreen device
name=your-touchscreen-device-name
calibration=1 0 0 0 -1 1
```

### 📌 Important Points:
- `transform=rotate-180` rotates the display **upside down**.
- The `[input-device]` section `calibration` matrix adjusts touchscreen accordingly:
  - `calibration=1 0 0 0 -1 1` → flip on Y-axis (good for 180° rotation).
- Replace `your-touchscreen-device-name` with the actual device name (check with `weston-info` or `udevadm info`).

---

## 🚀 Build the Image

After setting everything, rebuild Weston or your image:

```bash
bitbake weston
```
or

```bash
bitbake custom-image
```

Then flash and boot — Weston will load your customized `weston.ini` automatically!

---

# 📚 Useful References
- [Official Weston.ini documentation](https://wayland.freedesktop.org/weston/doc/latest/weston.ini.5.html)
- [Yocto Bitbake Manual](https://docs.yoctoproject.org/bitbake/)

---

# 🎯 Quick Summary

| Step | Action |
|:----|:-------|
| 1 | Create `weston_%.bbappend` |
| 2 | Add `weston.ini` in `files/` |
| 3 | Write correct install commands |
| 4 | Build image |
| 5 | Boot and verify |

---

Would you also like me to give you **another sample `weston.ini`** for:
- Only **90° rotation**
- Or **inverted X/Y touch axis separately**?

---
AUTHOR: Mahendra Sondgar(mahendrasondagar08@gmail.com)

