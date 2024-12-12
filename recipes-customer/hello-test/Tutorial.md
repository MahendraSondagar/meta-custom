# Creating a Multi-File C Application with Yocto

This tutorial explains how to create a `hello-test` Yocto recipe that compiles a C application consisting of multiple source files. The resulting binary is installed into the target root filesystem.

---

## Directory Structure

Ensure your directory structure is as follows:

```plaintext
hello-test/
├── files
│   ├── my_func.c
│   └── mytest.c
└── hello-test_1.0.0.bb
```

### Explanation:
- `files/my_func.c`: Contains a helper function for the application.
- `files/mytest.c`: Contains the main function and calls the helper function.
- `hello-test_1.0.0.bb`: The Yocto recipe for building and installing the application.

---

## Contents of Source Files

### `files/my_func.c`

This file defines a function that prints a formatted string:

```c
#include <stdio.h>

void my_func(const char *data)
{
    printf("function_data: %s", data);
}
```

### `files/mytest.c`

This file contains the `main` function and calls `my_func`:

```c
#include <stdio.h>

void my_func(const char *data);

int main(void)
{
    my_func("Hello world");
    return 0;
}
```

---

## Recipe: `hello-test_1.0.0.bb`

Below is the content of the Yocto recipe file:

```bitbake
DESCRIPTION = "Multiple C file compiling and installation test"

LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://mytest.c      \
           file://my_func.c"

S = "${WORKDIR}"

# Compile multiple source files
do_compile(){
    ${CC} mytest.c my_func.c ${LDFLAGS} -o userprog
}

# Install binary to /usr/bin
do_install(){
    install -d ${D}${bindir}
    install -m 0755 userprog ${D}${bindir}
}
```

---

## Step-by-Step Guide

### 1. Add Recipe and Source Files to Your Yocto Layer

Place the recipe and source files in your custom layer under `recipes-example/hello-test/`:

```plaintext
<your-layer>/recipes-example/hello-test/
    ├── hello-test_1.0.0.bb
    └── files/
        ├── my_func.c
        └── mytest.c
```

### 2. Add the Layer to Your Build Configuration

Ensure your layer is included in the build by adding it to `bblayers.conf`:

```plaintext
BBLAYERS += "<path-to-your-layer>"
```

### 3. Build the Recipe

Run the following command to build the recipe:

```bash
bitbake hello-test
```

### 4. Verify the Build Output

After a successful build, you can find the compiled binary in the Yocto build directory:

```plaintext
<build-dir>/tmp/work/<machine>/hello-test/1.0.0-r0/image/usr/bin/userprog
```

### 5. Add Recipe to Image

To include the `hello-test` program in your image, add the following to your `local.conf` or custom image recipe:

```plaintext
IMAGE_INSTALL:append = " hello-test"
```

Rebuild the image:

```bash
bitbake <image-name>
```

### 6. Deploy and Test

Flash the built image to your device and run the `userprog` program:

```bash
/usr/bin/userprog
```

You should see the output:

```plaintext
function_data: Hello world
```

---

### Author : Mahendra Sondagar

