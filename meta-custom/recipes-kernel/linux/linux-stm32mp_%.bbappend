FILESEXTRAPATHS:prepend := "${THISDIR}:${THISDIR}/files:"
SRC_URI += "file://0001-stm32mp157a-dk1-i2c5-added-dts.patch"

# Apply default configuration
KERNEL_DEFCONFIG_stm32mp1 = "defconfig"
