# Building a Yocto Recipe with Makefile

This tutorial guides you through creating a Yocto recipe to build a simple "Hello World" application using a Makefile.

---

## Directory Structure

Ensure your directory structure is as follows:

```plaintext
mymake/
├── files
│   ├── hello.c
│   └── Makefile
└── mymake_1.0.0.bb
```

### Explanation:
- `files/hello.c`: Contains the source code for the "Hello World" application.
- `files/Makefile`: Contains instructions to compile and clean the application.
- `mymake_1.0.0.bb`: The Yocto recipe for building and installing the application.

---

## Contents of Files

### `files/hello.c`

The `hello.c` file contains the following code:

```c
#include<stdio.h>

int main() {
    printf("Hello, World!\n");
    return 0;
}
```

### `files/Makefile`

The `Makefile` provides build and clean rules:

```make
all:
	$(CC) -o hello ${LDFLAGS} hello.c

clean:
	rm -f hello
```

---

## Recipe: `mymake_1.0.0.bb`

Below is the content of the Yocto recipe file:

```bitbake
SUMMARY = "Hello World application using Makefile"
DESCRIPTION = "A simple Hello World application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://hello.c \
           file://Makefile"

S = "${WORKDIR}"

# Compile the application using the Makefile
do_compile() {
    oe_runmake
}

# Install the application to /usr/bin
do_install() {
    install -d ${D}${bindir}
    install -m 0755 hello ${D}${bindir}/hello
}
```

---

## Step-by-Step Guide

### 1. Add Recipe and Files to Your Yocto Layer

Place the recipe and files in your custom layer under `recipes-example/mymake/`:

```plaintext
<your-layer>/recipes-example/mymake/
    ├── mymake_1.0.0.bb
    └── files/
        ├── hello.c
        └── Makefile
```

### 2. Add the Layer to Your Build Configuration

Include your custom layer in `bblayers.conf`:

```plaintext
BBLAYERS += "<path-to-your-layer>"
```

### 3. Build the Recipe

Run the following command to build the recipe:

```bash
bitbake mymake
```

### 4. Verify the Build Output

After a successful build, you can find the compiled binary in the Yocto build directory:

```plaintext
<build-dir>/tmp/work/<machine>/mymake/1.0.0-r0/image/usr/bin/hello
```

### 5. Add Recipe to Image

To include the `mymake` program in your image, add the following to your `local.conf` or custom image recipe:

```plaintext
IMAGE_INSTALL:append = " mymake"
```

Rebuild the image:

```bash
bitbake <image-name>
```

### 6. Deploy and Test

Flash the built image to your device and run the `hello` program:

```bash
/usr/bin/hello
```

You should see the output:

```plaintext
Hello, World!
```

---

### Author: Mahendra Sondagar



