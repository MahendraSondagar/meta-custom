# Creating a Hello World Application with Yocto

This tutorial explains how to create a simple `hello-world` Yocto recipe for a C++ program. The recipe compiles and installs the binary into the root filesystem of your Yocto image.

---

## Directory Structure

Ensure your directory structure is as follows:

```plaintext
hello-world/
├── hello-world
│   └── hello-world.cpp
└── hello-world_1.0.0.bb
```

### Explanation:

- `hello-world/hello-world.cpp`: Contains the source code for your Hello World application.
- `hello-world_1.0.0.bb`: The Yocto recipe for building and installing the application.

---

## Contents of `hello-world.cpp`

This is the simple C++ program to be compiled:

```cpp
#include <iostream>

using namespace std;

int main(int argc, char *argv[])
{
    cout << "Hello world, This is Yocto test" << endl;
    return 0;
}
```

---

## Recipe: `hello-world_1.0.0.bb`

Below is the content of the Yocto recipe file:

```bitbake
# Package summary
SUMMARY = "Hello World"
# License, for example MIT
LICENSE = "MIT"
# License checksum file is always required
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# hello-world.cpp from local file
SRC_URI = "file://hello-world.cpp"

# Set LDFLAGS options provided by the build system
TARGET_CC_ARCH += "${LDFLAGS}"

# Change source directory to work directory where hello-world.cpp is
S = "${WORKDIR}"

# Compile hello-world from sources, no Makefile
do_compile() {
    ${CXX} -Wall hello-world.cpp -o hello-world
}

# Install binary to final directory /usr/bin
do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/hello-world ${D}${bindir}
}
```

---

## Step-by-Step Guide

### 1. Add Recipe to Your Yocto Layer

Place the `hello-world_1.0.0.bb` file in your custom layer under `recipes-example/hello-world/` directory.

```plaintext
<your-layer>/recipes-example/hello-world/
    ├── hello-world_1.0.0.bb
    └── hello-world/
        └── hello-world.cpp
```

### 2. Add the Layer to Your Build Configuration

Ensure your layer is included in the build by adding it to the `bblayers.conf` file:

```plaintext
BBLAYERS += "<path-to-your-layer>"
```

### 3. Build the Recipe

Run the following commands to build the recipe:

```bash
bitbake hello-world
```

### 4. Verify the Build Output

After a successful build, you can find the compiled binary in the Yocto build directory:

```plaintext
<build-dir>/tmp/work/<machine>/hello-world/1.0.0-r0/image/usr/bin/hello-world
```

### 5. Add Recipe to Image

To include the `hello-world` program in your image, add the following to your `local.conf` or custom image recipe:

```plaintext
IMAGE_INSTALL:append = " hello-world"
```

Rebuild the image:

```bash
bitbake <image-name>
```

### 6. Deploy and Test

Flash the built image to your device and run the `hello-world` program:

```bash
/usr/bin/hello-world
```

You should see the output:

```plaintext
Hello world, This is Yocto test
```

---

### Author: Mahendra Sondagar


