
---

# Yocto Tutorial: Creating and Using Static Libraries

In this tutorial, we will walk through the process of creating a static library using a Yocto recipe and then creating an application that uses this static library.

## Part 1: Creating a Static Library

### Step 1: Create the Static Library Source Files

Let's create the source files for our static library. This library will have a simple function defined in `test.c` and its corresponding header file `header.h`.

**Directory Structure:**
```
mystaticlib/
└── files/
    ├── test.c
    └── header.h
```

**Content of `test.c`:**
```c
#include "header.h"
#include <stdio.h>

void my_function() {
    printf("Hello from my_function in the static library!\n");
}
```

**Content of `header.h`:**
```c
#ifndef HEADER_H
#define HEADER_H

void my_function();

#endif
```

### Step 2: Create the Yocto Recipe for the Static Library

Now, let's create the Yocto recipe to compile and package this static library.

**Recipe Location:**
```
meta-custom/recipes-custom/mystaticlib/mystaticlib_1.0.0.bb
```

**Content of `mystaticlib_1.0.0.bb`:**
```bitbake
DESCRIPTION = "Yocto Static Library Example"
SUMMARY = "${DESCRIPTION}"
AUTHOR = "Your Name (your.email@example.com)"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://test.c \
           file://header.h"

S = "${WORKDIR}"

do_compile() {
    ${CC} -c test.c ${CFLAGS} -o test.o
    ar rcs libmylib.a test.o
}

PACKAGES = "${PN}-dev ${PN}-staticdev"

do_install() {
    install -d ${D}${includedir}
    install -m 0644 header.h ${D}${includedir}/

    install -d ${D}${libdir}
    install -m 0644 libmylib.a ${D}${libdir}/
}

FILES:${PN}-staticdev += "${libdir}/libmylib.a"
FILES:${PN}-dev += "${includedir}/header.h"
```

### Step 3: Build the Static Library

To build the static library, navigate to your Yocto build directory and run:

```bash
bitbake mystaticlib
```

This command will compile the source code and generate the static library (`libmylib.a`), which will be installed in the appropriate directories.

## Part 2: Creating an Application that Uses the Static Library

### Step 1: Create the Application Source File

Next, we'll create an application that links to our static library.

**Directory Structure:**
```
myapp/
└── files/
    └── app.c
```

**Content of `app.c`:**
```c
#include "header.h"

int main() {
    my_function();
    return 0;
}
```

### Step 2: Create the Yocto Recipe for the Application

Now, let's create the Yocto recipe for our application.

**Recipe Location:**
```
meta-custom/recipes-custom/myapp/myapp_1.0.0.bb
```

**Content of `myapp_1.0.0.bb`:**
```bitbake
DESCRIPTION = "A simple application that uses the static library"
SUMMARY = "${DESCRIPTION}"
AUTHOR = "Your Name (your.email@example.com)"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://app.c"

S = "${WORKDIR}"

# Add mystaticlib as a dependency
DEPENDS = "mystaticlib"

do_compile() {
    ${CC} app.c -o app ${LDFLAGS} -L${STAGING_LIBDIR} -l:mylib.a
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 app ${D}${bindir}/
}
```

### Step 3: Build the Application

To build the application, run the following command in your Yocto build directory:

```bash
bitbake myapp
```

This command will compile the application, linking it against the static library (`libmylib.a`), and install the executable in the appropriate directory.

## Part 3: Creating an Image that Includes the Application

Finally, to create an image that includes the application, modify your `custom-image.bb` file:

**Example `custom-image.bb`:**
```bitbake
DESCRIPTION = "Custom Yocto Image with Static Library Application"
LICENSE = "MIT"

IMAGE_INSTALL:append = " mystaticlib-dev mystaticlib-staticdev myapp"
```

Build the image using:

```bash
bitbake custom-image
```


