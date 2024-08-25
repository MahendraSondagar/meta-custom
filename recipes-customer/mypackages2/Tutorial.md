Here is the updated guide with the example recipe to add a `readme.txt` file to the documentation directory and create a custom package `${PN}-mydoc`.

---

## Yocto: Detailed Guide on Package Splitting During the `do_package()` Stage Using `PACKAGES` and `FILES` Variables

When building packages with Yocto, especially with the **Kirkstone** release (or any other release), managing how files are split into different packages is essential. The **`PACKAGES`** and **`FILES`** variables are used to control which files go into which packages.

Here is a step-by-step tutorial that will provide you with in-depth knowledge about package splitting during the `do_package()` stage.

### 1. **The `do_package()` Task**

Yocto's `do_package()` stage is responsible for taking the compiled files from the `WORKDIR` and splitting them into multiple output packages. The splitting is governed by two key variables:
- **`PACKAGES`**: Defines the list of output packages.
- **`FILES`**: Specifies which files go into which package.

These packages can then be installed on a target system, and each one can contain specific files, libraries, or binaries.

---

### 2. **Understanding the `PACKAGES` Variable**

The `PACKAGES` variable lists all the packages that will be created by your recipe. If you don’t explicitly define `PACKAGES`, Yocto uses defaults.

#### Default Packages
Yocto provides default package names if you don’t define them:
- **`${PN}`**: The main package (package name comes from the recipe name)
- **`${PN}-dev`**: Development files (headers, etc.)
- **`${PN}-dbg`**: Debug symbols
- **`${PN}-doc`**: Documentation files
- **`${PN}-staticdev`**: Static libraries

#### Customizing `PACKAGES`
You can override the default and create custom packages:
```bash
PACKAGES = "${PN}-bin ${PN}-lib ${PN}-doc ${PN}-config"
```

Here, `${PN}-bin`, `${PN}-lib`, `${PN}-doc`, and `${PN}-config` are custom packages. You will define what files go into each of these packages using the `FILES` variable.

---

### 3. **Understanding the `FILES` Variable**

The `FILES` variable is used to specify what files go into each package listed in `PACKAGES`. Each package must have an associated `FILES_<package>` variable, which contains the list of paths or files to be included in that package.

#### Example:

```bash
FILES_${PN}-bin = "${bindir}/*"
FILES_${PN}-lib = "${libdir}/*.so*"
FILES_${PN}-doc = "${docdir}/*"
FILES_${PN}-config = "${sysconfdir}/*"
```

Here’s what these variables do:
- **`${bindir}`**: The directory where binaries go (typically `/usr/bin`)
- **`${libdir}`**: The directory for libraries (usually `/usr/lib`)
- **`${docdir}`**: The directory for documentation (typically `/usr/share/doc`)
- **`${sysconfdir}`**: The directory for configuration files (usually `/etc`)

Each `FILES_<package>` entry specifies the file path patterns to include in the respective package.

---

### 4. **File Locations and Variables**

Yocto uses a number of predefined variables to refer to specific directories in the root filesystem. Here’s a list of commonly used ones:
- **`${bindir}`**: `/usr/bin` — Binary executables
- **`${sbindir}`**: `/usr/sbin` — System binaries
- **`${libdir}`**: `/usr/lib` — Libraries
- **`${includedir}`**: `/usr/include` — Header files
- **`${datadir}`**: `/usr/share` — Architecture-independent data
- **`${docdir}`**: `/usr/share/doc` — Documentation
- **`${mandir}`**: `/usr/share/man` — Manual pages
- **`${sysconfdir}`**: `/etc` — System configuration files

You can use these variables to specify file locations in your packages.

---

### 5. **Example: Creating a Custom Package with a README File**

In this example, we will create a simple recipe that adds a `readme.txt` file to the documentation directory and splits the package into a custom package named `${PN}-mydoc`.

#### Step 1: Create a `readme.txt` File
Add a `readme.txt` file to your recipe source folder:
```txt
This is the readme for my custom Yocto package.
```

#### Step 2: Write the Recipe (`mypackage_1.0.0.bb`)

```bash
SUMMARY = "My Custom Yocto Package"
LICENSE = "CLOSED"
SRC_URI = "file://readme.txt"

# Define the list of packages
PACKAGES = "${PN} ${PN}-mydoc"

# Assign files to the packages
FILES_${PN} = "${bindir}/*"
FILES_${PN}-mydoc = "${docdir}/readme.txt"

do_install() {
    # Install the readme.txt into the documentation directory
    install -d ${D}${docdir}
    install -m 0644 ${WORKDIR}/readme.txt ${D}${docdir}/readme.txt
}
```

#### Explanation:
- **`PACKAGES`**: Specifies two packages: `${PN}` (default) and `${PN}-mydoc` (custom package).
- **`FILES_${PN}-mydoc`**: Specifies that the `readme.txt` file will go into the `${docdir}` (usually `/usr/share/doc`).
- **`do_install()`**: Copies `readme.txt` to the documentation directory during the install step.

#### Step 3: Build the Recipe
To build and test the recipe:
```bash
bitbake mypackage
```

Once built, the resulting packages will include:
- **`mypackage`**: The default package.
- **`mypackage-mydoc`**: The custom package containing the `readme.txt` file.

#### Step 4: Verify Package Contents
You can inspect the contents of the package using:
```bash
oe-pkgdata-util list-pkg-files mypackage-mydoc
```

This will list all files in the `mypackage-mydoc` package to verify that `readme.txt` has been placed correctly in the documentation directory.

---

### 6. **Fine-Tuning with `RDEPENDS` and `RRECOMMENDS`**

After defining packages, you may want to specify dependencies:
- **`RDEPENDS_<package>`**: Specifies runtime dependencies of a package.
- **`RRECOMMENDS_<package>`**: Specifies recommended runtime dependencies (optional but suggested).

For example, if the `${PN}-config` package needs to depend on `${PN}-bin`, you can do:
```bash
RDEPENDS_${PN}-config = "${PN}-bin"
```

---

### 7. **Splitting Static and Shared Libraries**

To split libraries into static and shared, you could write:

```bash
PACKAGES = "${PN}-staticdev ${PN}-lib ${PN}-dev"
FILES_${PN}-lib = "${libdir}/*.so*"
FILES_${PN}-staticdev = "${libdir}/*.a"
FILES_${PN}-dev = "${includedir}/*"
```

This setup ensures:
- **`${PN}-lib`** contains shared libraries.
- **`${PN}-staticdev`** contains static libraries.
- **`${PN}-dev`** contains header files.

---

### 8. **Testing and Debugging Package Splitting**

Once your recipe is written, you can test and verify the package splitting by building the recipe and inspecting the contents of each package.

Run:
```bash
bitbake <recipe-name>
```

Then inspect the generated packages using:
```bash
oe-pkgdata-util list-pkg-files <package-name>
```

This command lists all the files contained in a particular package and ensures they are correctly assigned.

---

### Conclusion

This tutorial covers the process of package splitting during the `do_package()` stage in Yocto using the `PACKAGES` and `FILES` variables. With this knowledge, you can efficiently manage how your recipe’s files are divided into packages, ensuring that each package contains the right components for deployment on the target system.

In the example above, we created a custom package `${PN}-mydoc` that includes a `readme.txt` file in the documentation directory. Feel free to customize further for your specific project needs. Let me know if you have more questions related to Yocto Kirkstone!

---


