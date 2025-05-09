# DRM (Direct Rendering Manager) Display Pipeline Tutorial

## 🎯 Objective

This document provides a practical and theory-based introduction to DRM (Direct Rendering Manager) components in Linux, especially useful for embedded Linux systems using Yocto. It covers the internal components, their roles, and how they interact in the display pipeline.

---

## 🧠 What is DRM?

DRM (Direct Rendering Manager) is a Linux kernel subsystem responsible for interfacing with GPUs and managing graphics rendering pipelines. It enables direct, high-performance rendering without relying on the older framebuffer API.

DRM is modern, efficient, supports multi-display setups, and is essential when using Wayland or headless rendering.

---

## 💡 What is KMS?

KMS (Kernel Mode Setting) is a part of DRM that handles setting display resolution, depth, and refresh rate **from within the kernel** instead of user space.

### ✅ Why KMS is used:

* Allows mode setting before userspace starts (e.g., splash screen during boot)
* Supports multiple monitors with different resolutions
* Used by modern compositors like Weston or Sway
* Enables smooth VT (virtual terminal) switching without flickering

> KMS provides the mechanism, while DRM offers the overall display stack infrastructure.

---

## 📚 DRM vs Framebuffer (fbdev)

| Feature           | DRM/KMS                            | Framebuffer (fbdev)            |
| ----------------- | ---------------------------------- | ------------------------------ |
| Modern API        | ✅                                  | ❌ Legacy                       |
| Multi-display     | ✅ Multiple CRTCs/Connectors        | ❌ Single display               |
| Buffer handling   | ✅ Atomic, multi-plane, zero-copy   | ❌ Single buffer, manual copy   |
| Rotation, scaling | ✅ Via planes                       | ❌ Manual                       |
| Performance       | 🔥 High (GPU/Hardware accelerated) | 🐢 Low (Software based mostly) |

---

## 🧩 DRM Pipeline Components

### 1. Framebuffer (`struct drm_framebuffer`)

* Stores the image data (ARGB8888, RGB565, etc.)
* Acts as the source of pixels
* Created by userspace (e.g., via GBM or dumb buffers)

> Think of it like a canvas in memory

---

### 2. Plane (`struct drm_plane`)

* Feeds a framebuffer into a **CRTC**
* Supports scaling, rotation, blending
* Types:

  * **Primary**: main UI
  * **Overlay**: video or splash
  * **Cursor**: mouse pointer

> Planes are like layers in GIMP or Photoshop

---

### 3. CRTC (`struct drm_crtc`)

* The **scanout engine** of DRM
* Combines one or more planes
* Controls screen refresh timing (vsync, resolution)
* One CRTC outputs to one Encoder

> Think of CRTC as the conductor orchestrating all planes

---

### 4. Encoder (`struct drm_encoder`)

* Converts image data from the CRTC into a physical signal
* Types: HDMI, VGA, LVDS, DSI, eDP

> Example: Translates digital image into HDMI signal

---

### 5. Connector (`struct drm_connector`)

* Represents the physical port (e.g., HDMI, LVDS)
* Reports monitor capabilities
* Handles hotplug detection

> Final step before signal goes to display

---

## 🔁 Display Pipeline Hierarchy

```
Memory (Framebuffers)
   ↓
Planes (layers: UI, cursor, video)
   ↓
CRTCs (combine planes, generate image stream)
   ↓
Encoders (translate image stream to signal)
   ↓
Connectors (send signal to physical monitor)
   ↓
Display
```

---

## 🧪 Real-world Example: HDMI Output on STM32MP1

Goal: Display a red image on HDMI screen.

```
1. Framebuffer
   → Memory buffer filled with red pixels

2. Plane
   → Primary plane with no scaling

3. CRTC
   → Combines plane, outputs 1920x1080@60Hz

4. Encoder
   → Converts image to HDMI TMDS signal

5. Connector
   → HDMI port; detects connected monitor

6. Display
   → Monitor shows the red image
```

---

## 🚀 What to Learn Next (Yocto + DRM)

1. **Hello DRM**: Minimal DRM program with `drmMode*()` APIs
2. **Atomic Modesetting**: Use DRM atomic API to set framebuffer, plane, and mode
3. **DRM Dumb Buffers**: Allocate & draw pixel data
4. **Direct Display with GBM**: EGL rendering + DRM
5. **Multi-display**: Use 2 CRTCs + 2 connectors
6. **Touchscreen Input (libinput)**: Combine input and DRM
7. **DRM+KMS Weston**: Write custom `weston.ini` and see how planes map

---

## 🧑‍💻 DRM Hello World Program with Yocto

### 🛠️ Overview

Create a simple DRM "Hello World" application using dumb buffers to draw a solid color on screen using Yocto.

### 1. Add dependencies in Yocto:

Ensure `libdrm` is added in your image:

```bitbake
IMAGE_INSTALL:append = " libdrm"
```

### 2. Create the C program (hello\_drm.c):

````c
# DRM (Direct Rendering Manager) Display Pipeline Tutorial

## 🎯 Objective

This document provides a practical and theory-based introduction to DRM (Direct Rendering Manager) components in Linux, especially useful for embedded Linux systems using Yocto. It covers the internal components, their roles, and how they interact in the display pipeline.

---

## 🧠 What is DRM?

DRM (Direct Rendering Manager) is a Linux kernel subsystem responsible for interfacing with GPUs and managing graphics rendering pipelines. It enables direct, high-performance rendering without relying on the older framebuffer API.

DRM is modern, efficient, supports multi-display setups, and is essential when using Wayland or headless rendering.

---

## 💡 What is KMS?

KMS (Kernel Mode Setting) is a part of DRM that handles setting display resolution, depth, and refresh rate **from within the kernel** instead of user space.

### ✅ Why KMS is used:

* Allows mode setting before userspace starts (e.g., splash screen during boot)
* Supports multiple monitors with different resolutions
* Used by modern compositors like Weston or Sway
* Enables smooth VT (virtual terminal) switching without flickering

> KMS provides the mechanism, while DRM offers the overall display stack infrastructure.

---

## 📚 DRM vs Framebuffer (fbdev)

| Feature           | DRM/KMS                            | Framebuffer (fbdev)            |
| ----------------- | ---------------------------------- | ------------------------------ |
| Modern API        | ✅                                  | ❌ Legacy                       |
| Multi-display     | ✅ Multiple CRTCs/Connectors        | ❌ Single display               |
| Buffer handling   | ✅ Atomic, multi-plane, zero-copy   | ❌ Single buffer, manual copy   |
| Rotation, scaling | ✅ Via planes                       | ❌ Manual                       |
| Performance       | 🔥 High (GPU/Hardware accelerated) | 🐢 Low (Software based mostly) |

---

## 🧩 DRM Pipeline Components

### 1. Framebuffer (`struct drm_framebuffer`)

* Stores the image data (ARGB8888, RGB565, etc.)
* Acts as the source of pixels
* Created by userspace (e.g., via GBM or dumb buffers)

> Think of it like a canvas in memory

---

### 2. Plane (`struct drm_plane`)

* Feeds a framebuffer into a **CRTC**
* Supports scaling, rotation, blending
* Types:

  * **Primary**: main UI
  * **Overlay**: video or splash
  * **Cursor**: mouse pointer

> Planes are like layers in GIMP or Photoshop

---

### 3. CRTC (`struct drm_crtc`)

* The **scanout engine** of DRM
* Combines one or more planes
* Controls screen refresh timing (vsync, resolution)
* One CRTC outputs to one Encoder

> Think of CRTC as the conductor orchestrating all planes

---

### 4. Encoder (`struct drm_encoder`)

* Converts image data from the CRTC into a physical signal
* Types: HDMI, VGA, LVDS, DSI, eDP

> Example: Translates digital image into HDMI signal

---

### 5. Connector (`struct drm_connector`)

* Represents the physical port (e.g., HDMI, LVDS)
* Reports monitor capabilities
* Handles hotplug detection

> Final step before signal goes to display

---

## 🔁 Display Pipeline Hierarchy

```
Memory (Framebuffers)
   ↓
Planes (layers: UI, cursor, video)
   ↓
CRTCs (combine planes, generate image stream)
   ↓
Encoders (translate image stream to signal)
   ↓
Connectors (send signal to physical monitor)
   ↓
Display
```

---

## 🧪 Real-world Example: HDMI Output on STM32MP1

Goal: Display a red image on HDMI screen.

```
1. Framebuffer
   → Memory buffer filled with red pixels

2. Plane
   → Primary plane with no scaling

3. CRTC
   → Combines plane, outputs 1920x1080@60Hz

4. Encoder
   → Converts image to HDMI TMDS signal

5. Connector
   → HDMI port; detects connected monitor

6. Display
   → Monitor shows the red image
```

---

## 🚀 What to Learn Next (Yocto + DRM)

1. **Hello DRM**: Minimal DRM program with `drmMode*()` APIs
2. **Atomic Modesetting**: Use DRM atomic API to set framebuffer, plane, and mode
3. **DRM Dumb Buffers**: Allocate & draw pixel data
4. **Direct Display with GBM**: EGL rendering + DRM
5. **Multi-display**: Use 2 CRTCs + 2 connectors
6. **Touchscreen Input (libinput)**: Combine input and DRM
7. **DRM+KMS Weston**: Write custom `weston.ini` and see how planes map

---

## 🧑‍💻 DRM Hello World Program with Yocto

### 🛠️ Overview

Create a simple DRM "Hello World" application using dumb buffers to draw a solid color on screen using Yocto.

### 1. Add dependencies in Yocto:

Ensure `libdrm` is added in your image:

```bitbake
IMAGE_INSTALL:append = " libdrm"
```

### 2. Create the C program (hello\_drm.c):

```c
#include <fcntl.h>
#include <unistd.h>
#include <xf86drm.h>
#include <xf86drmMode.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <sys/mman.h>

#define COLOR 0xFF0000FF // Red in ARGB8888

int main() {
    int fd = open("/dev/dri/card0", O_RDWR);
    if (fd < 0) return -1;

    drmModeRes *res = drmModeGetResources(fd);
    drmModeConnector *conn = NULL;
    drmModeEncoder *enc = NULL;
    uint32_t conn_id = 0;

    for (int i = 0; i < res->count_connectors; ++i) {
        conn = drmModeGetConnector(fd, res->connectors[i]);
        if (conn->connection == DRM_MODE_CONNECTED) {
            conn_id = conn->connector_id;
            break;
        }
        drmModeFreeConnector(conn);
    }
    if (!conn_id) return -1;

    drmModeModeInfo mode = conn->modes[0];
    enc = drmModeGetEncoder(fd, conn->encoder_id);

    struct drm_mode_create_dumb creq = {
        .width = mode.hdisplay,
        .height = mode.vdisplay,
        .bpp = 32
    };
    ioctl(fd, DRM_IOCTL_MODE_CREATE_DUMB, &creq);

    struct drm_mode_map_dumb mreq = {
        .handle = creq.handle
    };
    ioctl(fd, DRM_IOCTL_MODE_MAP_DUMB, &mreq);

    uint8_t *map = mmap(0, creq.size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, mreq.offset);
    memset(map, COLOR, creq.size);

    struct drm_mode_fb_cmd fb = {
        .width = creq.width,
        .height = creq.height,
        .pitch = creq.pitch,
        .bpp = 32,
        .depth = 24,
        .handle = creq.handle
    };
    ioctl(fd, DRM_IOCTL_MODE_ADDFB, &fb);

    drmModeSetCrtc(fd, enc->crtc_id, fb.fb_id, 0, 0, &conn_id, 1, &mode);

    sleep(10);

    munmap(map, creq.size);
    close(fd);
    return 0;
}
```

### 3. Create Yocto recipe:

Example: `recipes-graphics/drm-hello/drm-hello_0.1.bb`

```bitbake
SUMMARY = "Simple DRM Hello World Example"
LICENSE = "MIT"
SRC_URI = "file://hello_drm.c"
S = "${WORKDIR}"

DEPENDS = "virtual/libdrm"

do_compile() {
    ${CC} hello_drm.c -o hello_drm -ldrm
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 hello_drm ${D}${bindir}/hello_drm
}
```

### 4. Add to image:

```bitbake
IMAGE_INSTALL:append = " drm-hello"
```

Rebuild and run `/usr/bin/hello_drm` to see red screen.

---

## 📘 Bonus Tip: Wayland/Weston Uses DRM

Weston (Wayland compositor) uses DRM/KMS directly for rendering. Your work in DRM makes it easier to debug and optimize display stacks in embedded GUI apps.

---

## 📌 Summary

* DRM is the **foundation** for modern graphics
* Understand framebuffers → planes → CRTCs → encoders → connectors
* KMS is the **mode-setting** mechanism used by DRM
* Practice examples from buffer allocation to screen output
* Integrate with Yocto via `drm`, `weston`, `libdrm` recipes
* Use `hello_drm` to validate minimal DRM stack

---

## 🔗 References

* [DRM/KMS Intro - Chris Wilson @XDC 2018 (YouTube)](https://www.youtube.com/watch?v=nNY7NjUIJRA&t=2040s)
* [Hello KMS Tutorial - Bootlin (YouTube)](https://www.youtube.com/watch?v=wjAJmqwg47k&t=2597s)
* [DRM Subsystem Deep Dive - Bootlin (YouTube)](https://www.youtube.com/watch?v=nau2dgdXWOk&t=922s)

````

### 3. Create Yocto recipe:

Example: `recipes-graphics/drm-hello/drm-hello_0.1.bb`

```bitbake
SUMMARY = "Simple DRM Hello World Example"
LICENSE = "MIT"
SRC_URI = "file://hello_drm.c"
S = "${WORKDIR}"

DEPENDS = "virtual/libdrm"

do_compile() {
    ${CC} hello_drm.c -o hello_drm -ldrm
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 hello_drm ${D}${bindir}/hello_drm
}
```

### 4. Add to image:

```bitbake
IMAGE_INSTALL:append = " drm-hello"
```

Rebuild and run `/usr/bin/hello_drm` to see red screen.

---

## 📘 Bonus Tip: Wayland/Weston Uses DRM

Weston (Wayland compositor) uses DRM/KMS directly for rendering. Your work in DRM makes it easier to debug and optimize display stacks in embedded GUI apps.

---

## 📌 Summary

* DRM is the **foundation** for modern graphics
* Understand framebuffers → planes → CRTCs → encoders → connectors
* KMS is the **mode-setting** mechanism used by DRM
* Practice examples from buffer allocation to screen output
* Integrate with Yocto via `drm`, `weston`, `libdrm` recipes
* Use `hello_drm` to validate minimal DRM stack

---

## 🔗 References

* [DRM/KMS Intro - Chris Wilson @XDC 2018 (YouTube)](https://www.youtube.com/watch?v=nNY7NjUIJRA&t=2040s)
* [Hello KMS Tutorial - Bootlin (YouTube)](https://www.youtube.com/watch?v=wjAJmqwg47k&t=2597s)
* [DRM Subsystem Deep Dive - Bootlin (YouTube)](https://www.youtube.com/watch?v=nau2dgdXWOk&t=922s)

