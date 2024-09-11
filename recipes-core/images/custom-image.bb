SUMMARY = "Minimal custom image for STM32MP1-DK1 board"

AUTHOR= "Mahendra Sondagar | mahendrasondagar08@gmail.com"

IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = "en-us"

LICENSE = "MIT"

inherit core-image
inherit extrausers

# Set rootfs to 200 MiB by default
IMAGE_OVERHEAD_FACTOR ?= "1.2"
IMAGE_ROOTFS_SIZE ?= "204800"

IMAGE_FETAURES += "ssh-server-dropbear spalsh"

EXTRA_USERS_PARAMS += " useradd -m -s /bin/bash -P 'mahi' root;"


#Installing the supported libraries 

IMAGE_INSTALL:append = " python3 python3-pip \
                         usbutils pciutils \
                         util-linux \
                         dropbear \
                         hello-world \
                         hello-test   \
                         hello-mod \
                         mymake    \
                         mycmake   \
                         mygit     \
                         mypackages \
                         mypackages-doc \
                         mypackages2-test   \
                         mydepends          \
                         mystaticlib-staticdev \
                         mystaticlib \
                         mystaticlib-dev    \
                         myapp              \
                         myfilespaths       \
                         myfilespaths2      \
                         module-init-tools \
                         "




# Adding Bluetooth Support 
IMAGE_INSTALL:append = " \
        dbus \
        bluez5 \
        packagegroup-tools-bluetooth \
        expat \
"
DISTRO_FEATURES:append = " \
       bluetooth \ 
"
# Drivers for the TPL-Link Bluetooth dongle 
#IMAGE_INSTALL += "linux-firmware-rtl8761b_fw"
#IMAGE_INSTALL += "linux-firmware-rtl8761b_config"
#IMAGE_INSTALL += "linux-firmware-rtl8188eufw"


# Add kernel module for USB WiFi driver
IMAGE_INSTALL += "kernel-module-r8188eu \
                  linux-firmware-rtl8188 \
                  iw \
                  wpa-supplicant \
                  wireless-regdb-static"


# Autoload WiFi driver on boot (for the Lodable kernel module)
KERNEL_MODULE_AUTOLOAD += "r8188eu"

IMAGE_FEATURE += "spalsh"
CORE_IMAGE_EXTRA_INSTALL += " i2c-tools"


TOOLCHAIN_HOST_TASK:append = " nativesdk-python3"
TOOLCHAIN_TARGET_TASK:append = " libc-staticdev"
