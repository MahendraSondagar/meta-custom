# Yocto CMake Tutorial

## Introduction
This tutorial demonstrates how to create a Yocto recipe to build and install a C application using CMake. The application simply prints "Cmake test demo" when executed. The tutorial will guide you through the structure of the recipe, its components, and the build process.

---

## File Structure
Below is the file structure for this example:

```
mycmake$ tree
.
├── files
│   ├── CMakeLists.txt
│   └── cmake_test.c
└── mycmake_1.0.0.bb
```

### 1. Source Code
#### cmake_test.c
```c
#include <stdio.h>

int main(void)
{
        printf("Cmake test demo");
        return 0;
}
```
This is the source code of the application, a simple C program that prints a message.

### 2. CMakeLists.txt
```cmake
cmake_minimum_required(VERSION 3.16)
project(cmake_test)

# Add executable
add_executable(cmake_test cmake_test.c)

# Install the executable to /usr/bin
install(TARGETS cmake_test DESTINATION bin)
```
- **cmake_minimum_required**: Specifies the minimum version of CMake required to build the project.
- **project**: Defines the project name.
- **add_executable**: Tells CMake to create an executable named `cmake_test` from the source file `cmake_test.c`.
- **install**: Defines the installation path for the `cmake_test` binary (in this case, `/usr/bin`).

### 3. Yocto Recipe: `mycmake_1.0.0.bb`
```bash
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
```

#### Explanation of Recipe Terms:
- **SUMMARY**: A short description of the application.
- **SECTION**: Categorizes the package for easier navigation (e.g., examples, utilities).
- **LICENSE**: The license under which the code is released.
- **AUTHOR**: The name and email address of the recipe author.
- **LIC_FILES_CHKSUM**: Checksums for license files to ensure compliance.
- **SRC_URI**: Specifies the source files required for building the package. These are stored in the `files` directory.
- **S**: Defines the working directory for the build. `${WORKDIR}` is a standard Yocto variable.
- **inherit cmake**: Instructs Yocto to use the `cmake` class to handle the build process.
- **EXTRA_OECMAKE**: Used to pass additional flags to the `cmake` command during the build process. It is empty in this example.

---

## Steps to Build and Test

### 1. Place Files in Correct Locations
Ensure the directory structure matches the example above. Place the `CMakeLists.txt` and `cmake_test.c` files in the `files` subdirectory.

### 2. Add Recipe to Your Yocto Layer
Add the `mycmake_1.0.0.bb` recipe to your Yocto layer, typically under the `recipes-example/mycmake` directory.

### 3. Build the Recipe
Run the following commands:
```bash
bitbake mycmake
```
This will compile the `cmake_test` application and install the binary into the Yocto root filesystem.

### 4. Test the Application
After flashing the built image onto your target device, execute the following command:
```bash
/usr/bin/cmake_test
```
You should see the output:
```
Cmake test demo
```

---

### Author: Mahendra Sondagar



