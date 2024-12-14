# Yocto Tutorial: Fetching and Compiling a Repository from Git

This tutorial explains how to write a Yocto recipe to fetch source code from a Git repository, compile it locally, and install the resulting binary. We'll break down the recipe step by step and provide detailed explanations for each component.

## Project Structure

```
mygit$ tree
.
└── mygit_1.0.0.bb
```

### Recipe File: `mygit_1.0.0.bb`
Below is the complete recipe with detailed comments explaining each line:

```bb
# A short description of what the recipe does.
DESCRIPTION = "Yocto recipe to fetch the file from Git"

# A concise summary of the recipe, typically mirroring the DESCRIPTION.
SUMMARY = "${DESCRIPTION}"

# The author of the recipe, including contact information.
AUTHOR = "Mahendra_Sondagar (mahendrasondagar08@gmail.com)"

# License type under which the source code is distributed.
LICENSE = "MIT"

# Path to the license file and its checksum, required by Yocto for license compliance.
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Source URI specifies the Git repository to fetch the source code from.
# `git://` specifies the protocol, followed by the repository URL.
# `protocol=https` ensures that HTTPS is used for cloning.
# `branch=main` tells Yocto to fetch the `main` branch.
SRC_URI = "git://github.com/MahendraSondagar/Yocto-Test.git;protocol=https;branch=main"

# The directory within the work directory where the Git source will be placed.
S = "${WORKDIR}/git"

# SRCREV specifies the commit or revision to use from the repository.
# `AUTOREV` automatically fetches the latest commit from the specified branch.
SRCREV = "${AUTOREV}"

# Compilation step.
do_compile() {
    # Use the C compiler to compile the `test.c` file into an executable named `test`.
    ${CC} test.c ${LDFLAGS} -o test
}

# Installation step.
do_install() {
    # Create the target directory if it doesn't exist.
    install -d ${D}${bindir}

    # Copy the compiled binary to the target directory with appropriate permissions.
    install -m 0755 test ${D}${bindir}
}
```

---

## Detailed Explanation

### Key Variables
- **DESCRIPTION**: Provides a detailed description of the recipe.
- **SUMMARY**: A brief summary of the recipe's purpose.
- **AUTHOR**: Includes the name and contact information of the recipe's author.
- **LICENSE**: Specifies the license type for the project. Yocto requires all recipes to include license information.
- **LIC_FILES_CHKSUM**: Defines the path and checksum of the license file. This ensures the license file hasn't been altered.

### Fetching Source Code
- **SRC_URI**: Defines the Git repository URL and additional parameters like protocol and branch.
  - `git://`: Indicates that the source is fetched from a Git repository.
  - `protocol=https`: Specifies HTTPS as the protocol.
  - `branch=main`: Fetches the `main` branch of the repository.

- **S**: Specifies the source directory where the fetched files will be located.
  - `${WORKDIR}/git`: The directory where Yocto will place the repository files.

- **SRCREV**: Defines the specific commit or revision to use.
  - `${AUTOREV}`: Automatically fetches the latest commit from the specified branch.

### Compilation and Installation
- **do_compile()**:
  - This function compiles the source code using `${CC}` (the C compiler).
  - `${LDFLAGS}` includes linker flags provided by the build system.

- **do_install()**:
  - Creates the target installation directory using `install -d`.
  - Copies the compiled binary to the `bindir` directory (`/usr/bin` by default) with `install -m 0755`.

---

## Testing the Recipe

1. **Include the Recipe in Your Build**
   Add the recipe to your Yocto build environment by including it in a layer's `recipes-example` directory or another appropriate location.

2. **Build the Recipe**
   Use the `bitbake` command to build the recipe:
   ```bash
   bitbake mygit
   ```

3. **Verify the Output**
   After the build completes, check the contents of the `tmp/work` directory for the compiled binary and ensure it has been installed to the target root filesystem.

4. **Run the Application**
   Boot your target device and execute the `test` binary to verify functionality:
   ```bash
   /usr/bin/test
   ```

---

Author: Mahendra Sondagar


