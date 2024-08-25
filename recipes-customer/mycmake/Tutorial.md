## Tutorial on Using CMake with Yocto

### Introduction
CMake is a powerful and versatile build system that is often used in conjunction with Yocto for building software projects. Yocto, a popular build system for creating custom Linux distributions, supports CMake through the `cmake` class, making it easier to integrate and build CMake-based projects.

In this tutorial, we will explore how to create a Yocto recipe to build a simple C program using CMake. We'll cover the following topics:

1. **Setting up the Directory Structure**
2. **Writing the CMakeLists.txt File**
3. **Creating the Yocto Recipe**
4. **Understanding Key Variables and Functions**
5. **Building and Installing the Program**
6. **Advanced Configuration with EXTRA_OECMAKE**
7. **Testing the Integration**

### 1. Setting up the Directory Structure
To begin, you need to create a proper directory structure for your Yocto recipe. This structure typically includes the recipe file and any additional files required for the build process.

```bash
mycmake/
├── files
│   ├── CMakeLists.txt
│   └── userprog.c
└── mycmake_1.0.0.bb
```

### 2. Writing the CMakeLists.txt File
The `CMakeLists.txt` file is essential for defining how the project should be built using CMake. For this example, we'll create a simple C program called `userprog.c` and define its build process.

**CMakeLists.txt**
```cmake
cmake_minimum_required(VERSION 3.16)
project(cmake_test)

# Add executable
add_executable(userprog userprog.c)

# Install the executable to /usr/bin
install(TARGETS userprog DESTINATION bin)
```

### 3. Creating the Yocto Recipe
The Yocto recipe is the key file that tells Yocto how to build and install your project. Here's how you can create the `mycmake_1.0.0.bb` recipe file.

**mycmake_1.0.0.bb**
```bash
DESCRIPTION = "Yocto recipe to build the userprog code using CMake"
SUMMARY = "${DESCRIPTION}"
AUTHOR = "Your Name (your.email@example.com)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://userprog.c \
           file://CMakeLists.txt"

S = "${WORKDIR}"

inherit cmake
```

### 4. Understanding Key Variables and Functions

- **`S = "${WORKDIR}"`**: This variable defines the directory where the source code is located. `${WORKDIR}` is typically where Yocto extracts the source files.

- **`inherit cmake`**: This line tells Yocto to use the `cmake` class, which automatically handles the invocation of CMake commands such as `cmake`, `make`, and `make install`.

### 5. Building and Installing the Program
To build and install the program, you need to run the `bitbake` command:

```bash
bitbake mycmake
```

This command will compile `userprog.c` using CMake, and the resulting binary will be installed into the `bin` directory as defined in the `CMakeLists.txt` file.

### 6. Advanced Configuration with EXTRA_OECMAKE
Sometimes, you may need to pass additional options to CMake. Yocto provides the `EXTRA_OECMAKE` variable, which allows you to append custom CMake options.

**Example:**
Suppose you want to enable a specific feature or set a custom installation path:

```bash
EXTRA_OECMAKE += "-DCUSTOM_FEATURE=ON -DINSTALL_PREFIX=/custom/path"
```

This will pass the options `-DCUSTOM_FEATURE=ON` and `-DINSTALL_PREFIX=/custom/path` to CMake during the configuration stage.

### 7. Testing the Integration
After building the recipe, it's crucial to test the integration. Check that the binary is correctly installed by looking into the target root filesystem.

```bash
ls tmp/work/<arch>/mycmake/1.0-r0/image/usr/bin/
```

Ensure that `userprog` is present in the directory.

### Conclusion
This tutorial has provided an in-depth guide on integrating CMake projects with Yocto. By following these steps, you can efficiently compile and install your CMake-based projects within the Yocto build environment. The use of `EXTRA_OECMAKE` allows for further customization, making the integration even more flexible.

For more complex projects, you can expand on this basic example by adding more targets, handling dependencies, and using advanced CMake features.
