DESCRIPTION = "Yocto static library example recipe"
SUMMARY = "${DESCRIPTION}"
AUTHOR = "Mahendra Sondagar (mahendrasondagar08@gmail.com)"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://test.c \
           file://header.h"

S = "${WORKDIR}"

PACKAGES = "${PN} ${PN}-dev ${PN}-staticdev"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -c test.c -o test.o
    ar rcs mylib.a test.o
}

do_install() {
    install -d ${D}${includedir}
    install -m 0644 header.h ${D}${includedir}/

    install -d ${D}${libdir}
    install -m 0644 mylib.a ${D}${libdir}/
}

# The main package (mystaticlib) is intentionally empty
FILES:${PN} = ""

# Specify the static library for the staticdev package
FILES:${PN}-staticdev = "${libdir}/mylib.a"

# Specify the header file for the dev package
FILES:${PN}-dev = "${includedir}/header.h"

# Ensure main package is available even if empty
ALLOW_EMPTY:${PN} = "1"

