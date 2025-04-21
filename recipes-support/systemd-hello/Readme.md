
---

# 📘 Systemd Service Tutorial for Yocto (Kirkstone)

This tutorial guides you through creating and enabling a `systemd` service in Yocto (Kirkstone) to run a custom shell script (`hello.sh`) at boot. It also includes how to manage the service using `systemctl` commands.

---

## 🧾 1. Script to Run at Boot

Create a shell script `hello.sh`:

```bash
#!/bin/sh
while true; do
    echo "Hello Yocto world"
    sleep 1
done
```

Make it executable:

```bash
chmod +x hello.sh
```

---

## ⚙️ 2. Systemd Service File

Create a file named `hello.service` with the following content:

```ini
[Unit]
Description=Hello Yocto Service
After=default.target

[Service]
ExecStart=/usr/bin/hello.sh
Restart=always

[Install]
WantedBy=multi-user.target
```

### 🔍 Explanation:
- **[Unit]**
  - `Description`: Description shown in `systemctl status`.
  - `After=default.target`: Starts after the system reaches default boot target.
- **[Service]**
  - `ExecStart`: Command to start the script.
  - `Restart=always`: Automatically restarts if it exits.
- **[Install]**
  - `WantedBy=multi-user.target`: Makes the service run in normal multi-user mode (default boot mode).

---

## 📦 3. Yocto Recipe: `systemd-hello_1.0.0.bb`

Create the recipe structure:

```
meta-custom/
└── recipes-support/
    └── systemd-hello/
        ├── files/
        │   ├── hello.sh
        │   └── hello.service
        └── systemd-hello_1.0.0.bb
```

### Contents of `systemd-hello_1.0.0.bb`:

```bitbake
SUMMARY = "Hello Yocto systemd example"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
SRC_URI = "file://hello.sh \
           file://hello.service"

S = "${WORKDIR}"

RDEPENDS:${PN} = "bash"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/hello.sh ${D}${bindir}/hello.sh

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/hello.service ${D}${systemd_system_unitdir}/hello.service
}

SYSTEMD_AUTO_ENABLE   = "enable"
SYSTEMD_SERVICE:${PN} = "hello.service"
inherit systemd
```

### 🔍 Explanation:
- `RDEPENDS:${PN} = "bash"`: Ensures the script runs with Bash.
- `SYSTEMD_SERVICE:${PN}`: Specifies the service to install and manage.
- `SYSTEMD_AUTO_ENABLE = "enable"`: Enables the service automatically at boot.
- `inherit systemd`: Enables systemd-specific integration during packaging.

---

## ⚙️ 4. Enable systemd in Yocto

Edit `conf/local.conf`:

```conf
# Add systemd support
DISTRO_FEATURES:append = " systemd"

# Disable sysvinit to avoid conflict
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"

# Set systemd as the init manager
VIRTUAL-RUNTIME_init_manager = "systemd"
VIRTUAL-RUNTIME_initscripts = "systemd-compat-units"
```

---

## 🧩 5. Add Recipe to Your Image

In your image recipe (`my-custom-image.bb`):

```bitbake
IMAGE_INSTALL += "systemd-hello"
```

Alternatively, in `local.conf` for testing:

```conf
IMAGE_INSTALL:append = " systemd-hello"
```

---

## 🛠️ 6. Build the Image

From your build directory:

```bash
bitbake systemd-hello
bitbake my-custom-image
```

Flash the image and boot your device. You should see "Hello Yocto world" printed every second.

---

## 🧪 7. Manage the Service with systemctl

Once booted, you can manage your service using `systemctl`:

| Command | Purpose |
|--------|---------|
| `systemctl status hello.service` | Check service status and logs |
| `systemctl stop hello.service` | Stop the service |
| `systemctl start hello.service` | Start the service manually |
| `systemctl restart hello.service` | Restart the service |
| `systemctl enable hello.service` | Enable service at boot (should be already done via recipe) |
| `systemctl disable hello.service` | Disable autostart on boot |
| `systemctl is-enabled hello.service` | Check if the service is enabled |

### 💡 Example:

```bash
systemctl status hello.service
systemctl stop hello.service
systemctl start hello.service
```

You can also monitor output via `journalctl`:

```bash
journalctl -u hello.service -f
```

---

## ✅ Summary

| Step | Description |
|------|-------------|
| Script | Shell script that prints text every 1 second |
| Systemd Service | Ensures script runs on boot |
| Recipe | Packages and installs script + service |
| Init Manager | systemd configured as the default |
| Commands | Use `systemctl` and `journalctl` to manage the service |

---
## Author: Mahendra Sondagar (mahendrasondagar08@gmail.com)
