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
