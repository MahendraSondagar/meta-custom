---

# 📘 Wayland Tutorial for Yocto (Kirkstone)

This tutorial explains how to enable and test **Wayland** on a Yocto-based Linux image. It also provides a comparison with X11, necessary configurations, and testing commands.

---

## 📋 Prerequisites

Before you proceed, ensure:
- The **meta-wayland** layer is added to your `bblayers.conf`.

Example:

```conf
BBLAYERS += "path-to/meta-wayland"
```

Without this layer, Wayland components won't be available.

---

## 🧠 1. What is Wayland and Why It's Important?

**Wayland** is a modern replacement for the traditional X11 system to handle display and window management on Linux.

- Lightweight and efficient for embedded systems.
- Provides faster and direct communication between clients and compositor.
- Better security by isolating clients from each other.
- Full hardware (GPU) acceleration for rendering.
- Simpler protocol — easier to maintain and extend.

Wayland is **designed for the future**, where display servers are simpler, faster, and better suited for modern graphics hardware.

---

## 🔍 2. How Wayland is Different from X11?

Here’s a high-level comparison:

| Feature           | X11                                  | Wayland                             |
|-------------------|--------------------------------------|-------------------------------------|
| Architecture      | Client → X Server → Compositor       | Client → Compositor (direct)        |
| Communication     | Many layers, protocol extensions     | Minimal, direct communication      |
| Performance       | More overhead                        | Lightweight and fast               |
| Input/Output      | Managed by X server                  | Handled by compositor directly      |
| Security          | Less secure (global window access)   | Secure (isolated surfaces)          |
| Compositing       | Optional (external)                  | Always integrated                  |
| Protocol          | Older, complicated                   | Modern, simpler                    |

---

### 🖼 Architecture Diagrams

#### Wayland Architecture

![Wayland Architecture](./wayland-architecture.png)

---

#### X11 Architecture

![X11 Architecture](./x-architecture.png)

---

### Simplified Wayland Communication Flow

![Wayland Communication](./Wayland-diagram.webp)

---

## 🛠️ 3. Wayland Installation Steps in Yocto

### a) Update `local.conf`

Add the following lines:

```conf
# Adding Wayland support to distro
DISTRO_FEATURES:append = " wayland opengl pam"
PACKAGECONFIG:append_pn-qemu-native = " sdl"
```

**Explanation:**
- `wayland` — Enables Wayland protocol support.
- `opengl` — Enables GPU-based rendering.
- `pam` — Enables pluggable authentication modules (for Weston login).
- `sdl` — Ensures SDL (Simple DirectMedia Layer) support for emulation.

---

### b) Update Your Image Recipe

Modify your custom image recipe (e.g., `my-custom-image.bb`) to install Wayland packages:

```bitbake
IMAGE_INSTALL += " \
    weston \
    wayland \
    wayland-utils \
    mesa \
    dbus \
    packagegroup-core-boot \
    packagegroup-core-x11-xserver \
"
```

**Notes:**
- `weston` — Weston compositor for Wayland.
- `wayland` — Core Wayland libraries.
- `wayland-utils` — Tools for Wayland.
- `mesa` — OpenGL/EGL libraries.
- `dbus` — IPC mechanism required by Weston.
- `packagegroup-core-boot` — Basic system packages.
- `packagegroup-core-x11-xserver` — If X11 fallback is needed.

---

### c) Touchscreen Support Packages

For enabling touch input (evdev/tslib):

Add the following packages:

```bitbake
IMAGE_INSTALL += " \
    tslib \
    tslib-calibrate \
    tslib-tests \
    xf86-input-evdev \
"
```

**Recommended drivers:**
- `tslib` — Generic touchscreen input library.
- `evdev` — Input driver for generic Linux input devices.

These packages ensure that touchscreens using `/dev/input/event*` nodes work properly under Wayland/Weston.

---

### d) Build and Deploy the Image

```bash
bitbake my-custom-image
```

Flash the generated image to your board (using `dd`, `bmaptool`, or any preferred tool).

---

## 🧪 4. Commands to Test Wayland on the SBC

Once the system is booted:

### a) Check Weston Status

```bash
systemctl status weston
```

✅ Should show `active (running)` if Wayland is working.

---

### b) Run Wayland Information Tools

```bash
wayland-info
```

- Displays server version, outputs, input devices, and extensions.

---

### c) Run Sample Applications

```bash
weston-terminal
```

- Opens a terminal inside Weston.

```bash
weston-simple-egl
```

- Simple OpenGL demo showing hardware acceleration.

---

### 💡 Common Service Commands

| Command                        | Purpose                          |
|---------------------------------|----------------------------------|
| `systemctl start weston`        | Start Weston manually            |
| `systemctl stop weston`         | Stop Weston                      |
| `systemctl restart weston`      | Restart Weston                   |
| `systemctl enable weston`       | Enable Weston on boot            |
| `systemctl disable weston`      | Disable Weston on boot           |

---

## 📚 References

- [Wayland Official Website](https://wayland.freedesktop.org/)
- [Introduction to Wayland (YouTube)](https://www.youtube.com/watch?v=FksHyTvBUNs)

---

# ✅ Summary

| Step | Description |
|------|-------------|
| Pre-reqs | Add `meta-wayland` layer |
| Configuration | Update `local.conf` and image recipe |
| Build | Bitbake your custom image |
| Test | Use `systemctl`, `wayland-info`, and Weston demos |

---
AUTHOR: Mahendra Sondagar (mahendrasondagar08@gmail.com)
