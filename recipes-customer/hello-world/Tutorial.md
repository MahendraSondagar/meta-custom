
---

# Creating a Yocto Recipe for a Simple C++ Program: Hello World

This tutorial explains how to create a Yocto recipe to compile and install a simple "Hello World" C++ program. The steps include setting up the recipe, compiling the code, and installing the compiled binary into the root filesystem.

## 1. Directory Structure

First, ensure that your files are organized as follows:

```
hello-world
├── hello-world
│   └── hello-world.cpp
└── hello-world_1.0.0.bb
```

- `hello-world.cpp`: The C++ source file.
- `hello-world_1.0.0.bb`: The Yocto recipe for compiling and installing the program.

### `hello-world.cpp` Example

Here's an example of a simple "Hello World" program:

```cpp
#include <iostream>

int main() {
    std::cout << "Hello, World!" << std::endl;
    return 0;
}
```

Save this as `hello-world/hello-world.cpp`.

## 2. Writing the Recipe

The recipe file `hello-world_1.0.0.bb` is responsible for defining how Yocto will compile and install the C++ program. Below is the content of the recipe.

### `hello-world_1.0.0.bb`

```bash
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

### Key Elements of the Recipe

- **SUMMARY**: A brief description of the package.
- **LICENSE**: Defines the license for the package. Here, we're using the MIT license.
- **LIC_FILES_CHKSUM**: This line checks the license file checksum for integrity.
- **SRC_URI**: Points to the `hello-world.cpp` file, which is located in the same directory.
- **TARGET_CC_ARCH**: Ensures that the necessary compile-time flags are included.
- **do_compile**: A custom task that compiles the C++ source file using `${CXX}` (the C++ compiler) and outputs the binary `hello-world`.
- **do_install**: Installs the `hello-world` binary into the `${bindir}` directory (usually `/usr/bin`).

## 3. Build the Recipe

Now that your recipe and source files are ready, follow these steps to build and test your recipe.

### 3.1 Add Recipe to Your Layer

Make sure the recipe is part of your custom layer. If you haven't added it already:

1. Place the recipe under `meta-custom/recipes-example/hello-world/`.
2. Update your layer's `conf/layer.conf` to include this directory if needed.

### 3.2 Build the Package

Run the following command to build your package:

```bash
bitbake hello-world
```

If the build is successful, Yocto will compile the `hello-world` binary and install it in the final image.

### 3.3 Test the Package

Once the image is built, you can run the `hello-world` program on the target device:

```bash
hello-world
```

The expected output should be:

```
Hello, World!
```

## Conclusion

You have successfully created a simple Yocto recipe to compile and install a "Hello World" C++ program. This process can be extended to more complex programs by adding additional source files and build steps.

--- 
# Author: Mahendra Sondagar (mahendrasondagar08@gmail.com)
