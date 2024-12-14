DESCRIPTION = "Yocto FILESEXTRAPATHS_prepend use case example"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/lwl:"

SRC_URI = "file://dummy_app2.c"

S = "${WORKDIR}"

do_compile() {
    ${CC} ${S}/dummy_app2.c ${LDFLAGS} -o dummy_app2
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 dummy_app2 ${D}${bindir}
}

