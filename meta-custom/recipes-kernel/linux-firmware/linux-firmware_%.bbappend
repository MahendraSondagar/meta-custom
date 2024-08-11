FILESEXTRAPATHS_prepend := "${THISDIR}/linux-firmware:"
SRC_URI += "\
              file://rtl8761b_fw.bin \
              file://rtl8761b_config.bin \
              file://rtl8188eufw.bin \
             "

do_install_append () {
        install -m 0644 ${WORKDIR}/rtl8761b_fw.bin ${D}/lib/firmware/rtl_bt/rtl8761b_fw.bin
        install -m 0644 ${WORKDIR}/rtl8761b_config.bin ${D}/lib/firmware/rtl_bt/rtl8761b_config.bin
        install -m 0644 ${WORKDIR}/rtl8188eufw.bin ${D}/lib/firmware/rtlwifi/rtl8188eufw.bin
}

# NOTE: Use "=+" instead of "+=". Otherwise, the file is placed into the linux-firmware package.
PACKAGES =+ "${PN}-rtl8761b_fw"
PACKAGES =+ "${PN}-rtl8761b_config"
PACKAGES =+ "${PN}-rtl8188eufw"

FILES_${PN}-rtl8761b_fw = "/lib/firmware/rtl_bt/rtl8761b_fw.bin"
FILES_${PN}-rtl8761b_config = "/lib/firmware/rtl_bt/rtl8761b_config.bin"
FILES_${PN}-rtl8188eufw = "/lib/firmware/rtlwifi/rtl8188eufw.bin"


