SUMMARY = "A small image just capable of allowing a device to boot."

IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = "en-us"

LICENSE = "MIT"

inherit core-image

# Set rootfs to 512 MiB by default
IMAGE_OVERHEAD_FACTOR ?= "1.0"
IMAGE_ROOTFS_SIZE ?= "262144"

# Adding libraries in to the custom yocto image

IMAGE_INSTALL_append += "util-linux pciutils usbutils python3 python3-pip"
IMAGE_INSTALL_append += "hello-world"
IMAGE_INSTALL_append += "hello-mod module-init-tools"

# Add kernel module for USB WiFi driver
IMAGE_INSTALL += "kernel-module-r8188eu \
                  linux-firmware-rtl8188 \
                  dhcp-client \
                  iw \
                  wpa-supplicant \
                  wireless-regdb-static"

# Autoload WiFi driver on boot
KERNEL_MODULE_AUTOLOAD += "r8188eu"

# Adding Bluetooth Support 
IMAGE_INSTALL_append = " \
        dbus \
        bluez5 \
        packagegroup-tools-bluetooth \
        expat \
"
DISTRO_FEATURES_append = " \
       bluetooth \ 
"

# installing Systemd for bluetooth

DISTRO_FEATURES_append = " systemd"
VIRTUAL-RUNTIME_init_manager = "systemd" 

# Drivers for the TPL-Link Bluetooth dongle 
IMAGE_INSTALL += "linux-firmware-rtl8761b_fw"
IMAGE_INSTALL += "linux-firmware-rtl8761b_config"
IMAGE_INSTALL += "linux-firmware-rtl8188eufw"
