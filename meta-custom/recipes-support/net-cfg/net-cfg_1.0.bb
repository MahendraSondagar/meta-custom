SUMMARY= "Script files for the testing"
SRC_URI = " file://test.sh\
            file://system_info.py\
"

LICENSE = "CLOSED"

do_install() {
	install -d ${D}${bindir} 
        install -m 0755 ${WORKDIR}/test.sh ${D}${bindir} 
        install -m 0755 ${WORKDIR}/system_info.py ${D}${bindir}
}

FILES_${PN} += "${bindir}/test.sh"
FILES_${PN} += "${bindir}/system_info.py"

