DESCRIPTION = "Yocto recipe to show-case the use of the RDEPENDS"

SUMMARY = "${DESCRIPTION}"

AUTHOR = "Mahendra sondagar (mahendrasondagar08@gmail.com)"

LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://my_userprog.c"

S= "${WORKDIR}"

RDEPENDS_${PN} = "pciutils"

do_compile() {
	${CC} my_userprog.c ${LDFLAGS} -o my_userprog
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 my_userprog ${D}${bindir}
}


