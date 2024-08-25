

---

# Understanding `RDEPENDS` in Yocto with an Example

## Introduction

In Yocto, managing dependencies between packages is crucial to ensure that all necessary components are available at runtime. The `RDEPENDS` variable is used to specify runtime dependencies for a package, meaning that the listed packages must be installed when your package is installed on the target system.

This tutorial demonstrates how to use `RDEPENDS` by creating a simple Yocto recipe, `mydepends_1.0.0`, that includes a runtime dependency on the `pciutils` package.

## Recipe Structure

Let's start with the directory structure for our recipe:

```plaintext
mydepends/
├── files
│   └── my_userprog.c
└── mydepends_1.0.0.bb
```

### `my_userprog.c` Source Code

The C program `my_userprog.c` uses a system call to execute the `lspci` command, which lists PCI devices. The `lspci` command is provided by the `pciutils` package, creating a runtime dependency:

```c
#include <stdio.h>

int main(void)
{
    printf("Hello world, This is lspci example\n");
    system("lspci");

    return 0;
}
```

### The `mydepends_1.0.0.bb` Recipe

Here's the content of the `mydepends_1.0.0.bb` recipe:

```bash
DESCRIPTION = "Yocto recipe to show-case the use of the RDEPENDS"

SUMMARY = "${DESCRIPTION}"

AUTHOR = "Mahendra Sondagar (mahendrasondagar08@gmail.com)"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://my_userprog.c"

S= "${WORKDIR}"

RDEPENDS_${PN} = "pciutils"

do_compile() {
    ${CC} my_userprog.c ${LDFLAGS} -o my_userprog
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 my_userprog ${D}${bindir}
}
```

### Key Sections Explained

- **`RDEPENDS_${PN}`**: This is the critical part of the recipe that declares `pciutils` as a runtime dependency. When this recipe is included in an image, Yocto ensures that `pciutils` is also installed on the target device.

- **`do_compile()`**: This function compiles the C program `my_userprog.c` into an executable named `my_userprog`.

- **`do_install()`**: This function installs the `my_userprog` binary into the `${bindir}` directory (usually `/usr/bin`).

## Building the Recipe

To build this recipe, you would typically run:

```bash
bitbake mydepends
```

Once the recipe is successfully built, you can include it in your image by adding it to the `IMAGE_INSTALL` variable in your image recipe:

```bash
IMAGE_INSTALL:append = " mydepends"
```

Yocto will automatically handle the installation of `pciutils` on the target system, satisfying the runtime dependency declared by `RDEPENDS_${PN}`.

## Testing the Dependency

After the image is built and deployed to the target device, you can test the `my_userprog` program:

```bash
my_userprog
```

This should print "Hello world, This is lspci example" and then list the PCI devices on the system, confirming that `pciutils` was correctly installed.

## Conclusion

This example demonstrates the use of the `RDEPENDS` variable in Yocto recipes. By declaring runtime dependencies with `RDEPENDS`, you ensure that all necessary packages are available when your program runs on the target system. This approach simplifies dependency management and ensures a smoother deployment process.


---


