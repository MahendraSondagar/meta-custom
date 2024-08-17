DESCRIPTION = "Yocto recipe to create the custom package and copying readme.txt in it"

SUMMARY = "${DESCRIPTION}"

AUTHOR = "Mahendra Sondagar (mahendrasondagar08@gmail.com)"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://readme.txt"

S = "${WORKDIR}"

# Specify the custom package name
PACKAGES = "${PN} ${PN}-test"

# Define the installation steps
do_install() {
    install -d ${D}${datadir}/mydir
    install -m 0644 readme.txt ${D}${datadir}/mydir
}

# Ensure the files are split correctly between packages
FILES:${PN} += "${datadir}/mydir/readme.txt"
FILES:${PN}-test += "${datadir}/mydir/readme.txt"

