SUMMARY = "The hello-world linux kernel module test with the Yocto"

DESCRIPTION = "${SUMMARY}"

LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

inherit module 

SRC_URI = "file://Makefile \
           file://COPYING  \
           file://hello.c  \
	  " 


S = "${WORKDIR}"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.

RPROVIDES:${PN} += "kernel-module-hello"

