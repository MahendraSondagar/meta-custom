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

