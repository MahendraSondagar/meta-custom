
---

## Example: Yocto example to add the readme.md file in to the docdir

### File Structure
```bash
mypackages$ tree
.
├── files
│   ├── mycode.c
│   └── readme.txt
└── mypackages_1.0.0.bb
```

### Recipe File: `mypackages_1.0.0.bb`
Below is the content of the `mypackages_1.0.0.bb` recipe file, with detailed comments:

```bash
DESCRIPTION = "Yocto recipe to copy the readme.txt into docdir"
# Short description of the recipe

SUMMARY = "${DESCRIPTION}"
# A summary, typically the same as DESCRIPTION

AUTHOR = "Mahendra Sondagar (mahendrasondagar08@gmail.com)"
# Author information

LICENSE = "MIT"
# License type for the recipe

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
# Checksum of the license file to ensure license integrity

SRC_URI = "file://mycode.c \
           file://readme.txt"
# Specify the source files for the recipe

S = "${WORKDIR}"
# Define the source directory, in this case, the working directory

# Compile the application
# This step uses GCC to compile the mycode.c file into an executable

do_compile() {
    ${CC} ${CFLAGS} mycode.c ${LDFLAGS} -o mycode
}

# Install the compiled binary and the README file into appropriate directories

do_install() {
    install -d ${D}${bindir}
    install -m 0755 mycode ${D}${bindir}
    install -d ${D}${docdir}
    install -m 0644 readme.txt ${D}${docdir}
}
```

### Source Code: `mycode.c`
Below is the content of the `mycode.c` file:

```c
#include <stdio.h>
int main(int argc, char *argv[])
{
    printf("Hello world, This is Yocto packages test\r\n");
    return 0;
}
```

### README File: `readme.txt`
The `readme.txt` file can contain any project-specific documentation. For example:

```
This is a sample README file.
It is installed into the documentation directory (/usr/share/doc) during the build process.
```

---

## Steps to Build and Test

1. **Add the Recipe to Your Layer**:
   Place the `mypackages_1.0.0.bb` recipe file in your custom layer under `recipes-example/mypackages/`.

2. **Add the Recipe to Your Build Configuration**:
   Add the recipe name to your `local.conf` or include it in an image recipe:
   ```bash
   IMAGE_INSTALL:append = " mypackages"
   ```

3. **Build the Image**:
   Run the bitbake command to build the package:
   ```bash
   bitbake mypackages
   ```

4. **Test the Installation**:
   Once the image is built and flashed onto the target device, check the following:
   - The `mycode` binary is located in `/usr/bin/`.
   - The `readme.txt` file is located in `/usr/share/doc/`.

5. **Run the Program**:
   Execute the installed binary:
   ```bash
   /usr/bin/mycode
   ```
   Expected Output:
   ```
   Hello world, This is Yocto packages test
   ```

---

## Author: Mahendra Sondagar


