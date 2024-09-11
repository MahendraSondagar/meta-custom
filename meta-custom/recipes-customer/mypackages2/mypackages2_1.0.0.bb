DESCRIPTION = "Yocto recipe to create the custom package and copying readme.txt in it"

SUMMARY = "${DESCRIPTION}"

AUTHOR = "Mahendra Sondagar (mahendrasondagar08@gmail.com)"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://myreadme.txt"

S = "${WORKDIR}"


# Define the list of packages
PACKAGES = "${PN}-test"



# Assign files to the custom doc package
FILES:${PN}-test += "${docdir}/myreadme.txt"

do_install() {
    # Install the readme.txt into the documentation directory
    install -d ${D}${docdir}
    install -m 0644 ${WORKDIR}/myreadme.txt ${D}${docdir}/myreadme.txt
}

