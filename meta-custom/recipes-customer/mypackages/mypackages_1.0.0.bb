DESCRIPTION = "This is the Yocto packages test recipe"

SUMMARY = "${DESCRIPTION}"

AUTHOR = "Mahendra Sondagar (mahendrasondagar08@gmail.com)"

LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://mycode.c \
           file://readme.txt"

S = "${WORKDIR}"

do_compile() {
    ${CC} ${CFLAGS} mycode.c ${LDFLAGS} -o mycode
}

# Along with the compilation, let copying the readme.txt with the docdir
# make sure that in to the image, we need to install mypackages and mypackages-doc to get copy the doc folder in to the actual image

do_install() {
    install -d ${D}${bindir}
    install -m 0755 mycode ${D}${bindir}
    install -d ${D}${docdir}
    install -m 0644 readme.txt ${D}${docdir}
}

