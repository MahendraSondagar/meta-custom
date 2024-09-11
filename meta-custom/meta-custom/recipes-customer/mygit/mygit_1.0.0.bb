DESCRIPTION = "Yocto recipe to fetch the file from Git"

SUMMARY = "${DESCRIPTION}"

AUTHOR = "Mahendra_Sondagar (mahendrasondagar08@gmail.com)"

LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Fetching the source code from GitHub with the correct protocol
SRC_URI = "git://github.com/MahendraSondagar/Yocto-Test.git;protocol=https;branch=main"

S = "${WORKDIR}/git"

# Pick up the latest file from the Git repository
SRCREV = "${AUTOREV}"

do_compile() {
    ${CC} test.c ${LDFLAGS} -o test
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 test ${D}${bindir}
}

