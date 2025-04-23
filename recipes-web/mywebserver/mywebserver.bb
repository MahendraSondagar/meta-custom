DESCRIPTION = "Custom Web Server Content"
LICENSE = "CLOSED"

SRC_URI = "file://index.html"

RDEPENDS:${PN} = "nginx"

do_install() {
    install -d ${D}/var/www/html
    install -m 0644 ${WORKDIR}/index.html ${D}/var/www/html/
}

FILES:${PN} = "/var/www/html/index.html"

