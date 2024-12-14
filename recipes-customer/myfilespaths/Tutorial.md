### Tutorial: Understanding and Using the `FILESPATHS` Variable in Yocto

#### Introduction

Yocto provides several mechanisms to control where the build system looks for files. One important variable is `FILESPATHS`, which defines the search paths for fetching source files specified in the `SRC_URI` variable. In this tutorial, we will explore how `FILESPATHS` is used by creating a simple Yocto recipe called `myfilespaths`.

---

#### What is `FILESPATHS`?

The `FILESPATHS` variable in Yocto controls where the BitBake build system looks for files specified by `SRC_URI`. By default, `FILESPATHS` includes several directories where the build system automatically looks for files:

1. **`${PN}`**: The recipe's name (e.g., `myfilespaths`).
2. **`${PN}-${PV}`**: The recipe's name and version (e.g., `myfilespaths-1.0.0`).
3. **`files/`**: The `files` directory inside the recipe folder.

If files are not found in any of these directories, the build will fail unless the paths are adjusted.

---

#### Recipe Structure

In our demonstration, we will create a basic recipe called `myfilespaths` with the following structure:

```bash
myfilespaths$ tree
.
├── myfilespaths
│   └── dummy_app.c
└── myfilespaths_1.0.0.bb
```

The `dummy_app.c` file contains a simple C program that prints a message, and the `myfilespaths_1.0.0.bb` recipe will compile it.

---

#### Recipe Contents: `myfilespaths_1.0.0.bb`

Below is the content of the recipe file:

```bash
DESCRIPTION = "Yocto FILESPATHS variable demonstration recipe"
SUMMARY = "${DESCRIPTION}"
AUTHOR  = "Mahendra Sondagar (mahendrasondagar08@gmail.com)"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Pointing to the source file located in the <recipename> directory
SRC_URI = "file://dummy_app.c"

S = "${WORKDIR}"

do_compile() {
    ${CC} dummy_app.c ${LDFLAGS} -o dummy_app
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 dummy_app ${D}${bindir}
}
```

#### Explanation of Recipe Components

- **`SRC_URI = "file://dummy_app.c"`**: This line points to the source file. Yocto will search for this file in directories defined by the `FILESPATHS` variable.
- **`S = "${WORKDIR}"`**: This defines where the source code will be unpacked (working directory).
- **`do_compile()`**: The compilation step where the C program is compiled into a binary called `dummy_app`.
- **`do_install()`**: This step installs the compiled binary into the `${bindir}`, which is `/usr/bin` on the target system.

---

#### Understanding the `FILESPATHS` Variable

By default, Yocto automatically looks for files in the following locations:
- `files/`
- `${PN}` (in this case, `myfilespaths/`)
- `${PN}-${PV}` (in this case, `myfilespaths-1.0.0/`)

In our example, the source file `dummy_app.c` is stored in the `myfilespaths/` directory. By defining `SRC_URI = "file://dummy_app.c"`, Yocto will search the directories listed in `FILESPATHS` until it finds the file.

Since our `dummy_app.c` is located in the `myfilespaths/` directory, Yocto will find it without any further configuration because `FILESPATHS` includes this directory by default.

---

#### How to Modify `FILESPATHS`

While in many cases you don't need to manually define `FILESPATHS`, there are situations where you may need to customize it. For example, if your source files are located in a custom directory structure, you can modify `FILESPATHS` like this:

```bash
FILESPATHS_prepend := "${THISDIR}/custom-dir:"
```

This tells Yocto to first look in `custom-dir` inside the recipe directory.

---

#### Step-by-Step: Building the Recipe

1. **Create the Recipe Structure**

   Inside your Yocto workspace, create the directory structure as shown above and add the `dummy_app.c` source file and `myfilespaths_1.0.0.bb` recipe.

2. **Add the Recipe to Your Layer**

   Ensure that the recipe is added to your custom layer. For example, add the following line to `meta-yourlayer/conf/layer.conf`:

   ```bash
   BBFILES += "${LAYERDIR}/recipes-customer/myfilespaths/*.bb"
   ```

3. **Run the BitBake Command**

   Execute the following command to build the recipe:

   ```bash
   bitbake myfilespaths
   ```

   If everything is set up correctly, Yocto will compile and install the `dummy_app` binary.

4. **Verify the Output**

   After a successful build, the `dummy_app` binary will be placed in the `/usr/bin` directory on the target image. You can run it to see the output.

---

#### Summary

In this tutorial, we demonstrated how to use the `FILESPATHS` variable in Yocto through a simple recipe that compiles a C program. The `FILESPATHS` variable is critical for defining where Yocto looks for source files, and by default, it includes directories like `files/`, `${PN}/`, and `${PN}-${PV}/`.

By understanding how `FILESPATHS` works, you can better organize your recipes and source files within Yocto. Customizing `FILESPATHS` is useful when you want Yocto to look in additional directories for source files.

---

#### Example C Program: `dummy_app.c`

For reference, here is the simple C program used in the example:

```c
#include <stdio.h>

int main() {
    printf("Yocto FILESPATHS Demo\n");
    return 0;
}
```

---

With this knowledge, you can now confidently use the `FILESPATHS` variable in your Yocto projects to control how and where files are located for building your recipes.


