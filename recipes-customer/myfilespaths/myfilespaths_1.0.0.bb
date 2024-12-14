DESCRIPTION = "Yocto FILESPATHS variable demonstartion recipe"
SUMMARY = "${DESCRIPTION}"
AUTHOR  = "Mahendra Sondagar (mahendra sondagar08@gmail.com)"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://dummy_app.c"
S = "${WORKDIR}"

do_compile() {
	${CC} dummy_app.c ${LDFLAGS} -o dummy_app 
} 

do_install() {
	install -d ${D}${bindir}
        install -m 0755 dummy_app ${D}${bindir}
}


