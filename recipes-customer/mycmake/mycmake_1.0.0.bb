SUMMARY = "Simple Hello World Cmake application"
SECTION = "examples"
LICENSE = "MIT"
AUTHOR  = "Mahendra Sondagar (mahendrasondagar08@gmail.com)"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
            file://CMakeLists.txt \
            file://cmake_test.c \
        "

S = "${WORKDIR}"

inherit cmake

EXTRA_OECMAKE = ""
