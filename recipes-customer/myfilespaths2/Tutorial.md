
---

# Yocto Tutorial: Using `FILESEXTRAPATHS_prepend` to Define Custom Paths

In this tutorial, we will demonstrate how to use the `FILESEXTRAPATHS_prepend` variable in Yocto to search for source files in a custom directory. The example uses a custom Yocto recipe (`myfilespaths2_1.0.0.bb`) and a simple C program (`dummy_app2.c`) to illustrate this.

## 1. Directory Structure

First, let’s set up the directory structure. This is the file hierarchy for the recipe:

```bash
meta-custom/
├── recipes-customer/
│   └── myfilespaths2/
│       ├── myfilespaths2_1.0.0.bb
│       └── lwl/
│           └── dummy_app2.c
```

### Explanation:

- **`meta-custom`**: This is your custom Yocto layer.
- **`recipes-customer`**: Directory where your custom recipes are stored.
- **`myfilespaths2`**: The directory containing your `myfilespaths2_1.0.0.bb` recipe and a subdirectory (`lwl`) that holds the source file (`dummy_app2.c`).

## 2. Source File: `dummy_app2.c`

Create a simple C program named `dummy_app2.c`:

```c
#include <stdio.h>

int main() {
    printf("Hello from dummy_app2.c\n");
    return 0;
}
```

Place this file under the `lwl/` directory (`meta-custom/recipes-customer/myfilespaths2/lwl/dummy_app2.c`).

## 3. Recipe: `myfilespaths2_1.0.0.bb`

Next, create the `myfilespaths2_1.0.0.bb` recipe file in the `meta-custom/recipes-customer/myfilespaths2/` directory:

```bitbake
DESCRIPTION = "Yocto FILESEXTRAPATHS_prepend use case example"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Adding custom file paths using FILESEXTRAPATHS_prepend
FILESEXTRAPATHS_prepend := "${THISDIR}/lwl:"

SRC_URI = "file://dummy_app2.c"

S = "${WORKDIR}"

do_compile() {
    ${CC} ${S}/dummy_app2.c ${LDFLAGS} -o dummy_app2
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 dummy_app2 ${D}${bindir}
}
```

### Key Components:
- **`FILESEXTRAPATHS_prepend`**: This variable prepends the custom path `${THISDIR}/lwl:` to the search path for source files. This ensures Yocto looks in the `lwl/` directory for `dummy_app2.c`.
- **`SRC_URI`**: Specifies that the source file is located in the custom path (`file://dummy_app2.c`).
- **`do_compile`**: Compiles the `dummy_app2.c` file.
- **`do_install`**: Installs the compiled binary into the system's `bindir`.

## 4. Explanation of `FILESEXTRAPATHS_prepend`

The `FILESEXTRAPATHS_prepend` variable modifies the search paths for source files. In this case, we are adding the custom directory `${THISDIR}/lwl` to the path where Yocto looks for files referenced by `SRC_URI`.

The final search paths for source files would include:
1. The custom directory: `meta-custom/recipes-customer/myfilespaths2/lwl/`
2. Other default directories where Yocto normally searches for files (e.g., `${PN}`, `${PN}-${PV}`, etc.).

By using `FILESEXTRAPATHS_prepend`, Yocto will prioritize searching in the `lwl/` directory before searching in any other default locations.

## 5. Building the Recipe

Once you have your recipe and source files in place, you can build the recipe with the following command:

```bash
bitbake myfilespaths2
```

If everything is set up correctly, Yocto will search the custom path specified by `FILESEXTRAPATHS_prepend` for the `dummy_app2.c` file, compile it, and install the resulting binary.

## 6. Verifying the Output

After the build completes successfully, the compiled binary `dummy_app2` will be available in the target’s `bindir`:

```bash
/usr/bin/dummy_app2
```

You can verify the output by running the binary, which should display the following message:

```bash
Hello from dummy_app2.c
```

## 7. Conclusion

In this tutorial, we demonstrated how to use `FILESEXTRAPATHS_prepend` to define a custom search path for source files in Yocto. This method allows you to organize your source files in custom directories and ensure Yocto can find them during the build process.

---

This completes the tutorial for using `FILESEXTRAPATHS_prepend` in Yocto. You can adapt this method for other recipes and customize the paths based on your project’s structure.
