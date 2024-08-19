### Yocto: Detailed Guide on Package Splitting During the `do_package()` Stage Using `PACKAGES` and `FILES` Variables

When building packages with Yocto, especially with the **Kirkstone** release (or any other release), managing how files are split into different packages is essential. The **`PACKAGES`** and **`FILES`** variables are used to control which files go into which packages.

Here is a step-by-step tutorial that will provide you with in-depth knowledge about package splitting during the `do_package()` stage.

---

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

### 5. **Example: Creating a Custom Package**

Let’s go through a practical example of splitting files into multiple packages.

#### Step 1: Define `PACKAGES`
```bash
PACKAGES = "${PN}-bin ${PN}-lib ${PN}-doc ${PN}-config"
```

- **`${PN}-bin`**: Will contain binary files.
- **`${PN}-lib`**: Will contain shared libraries.
- **`${PN}-doc`**: Will contain documentation.
- **`${PN}-config`**: Will contain configuration files.

#### Step 2: Define `FILES`
```bash
FILES_${PN}-bin = "${bindir}/*"
FILES_${PN}-lib = "${libdir}/*.so*"
FILES_${PN}-doc = "${docdir}/*"
FILES_${PN}-config = "${sysconfdir}/*"
```

- **`${PN}-bin`**: All binaries in `/usr/bin/`.
- **`${PN}-lib`**: All shared libraries (`.so` files) in `/usr/lib/`.
- **`${PN}-doc`**: All files in the documentation directory `/usr/share/doc/`.
- **`${PN}-config`**: All configuration files in `/etc/`.

#### Step 3: Additional Customizations
You can include more specific patterns in `FILES` if needed. For instance:
```bash
FILES_${PN}-lib = "${libdir}/libexample.so.*"
```
This example ensures that only certain shared libraries are included.

#### Step 4: Other Package Types

You can also include other package types like:
- **Development Files**: `${PN}-dev`
- **Debug Symbols**: `${PN}-dbg`

```bash
FILES_${PN}-dev = "${includedir}/* ${libdir}/*.a ${libdir}/*.la"
FILES_${PN}-dbg = "${bindir}/.debug/* ${libdir}/.debug/*"
```

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

### 9. **Best Practices**

- Ensure that development and runtime files are properly separated to avoid installing unnecessary files on production systems.
- Use proper directory variables like `${bindir}`, `${libdir}`, etc., to make recipes portable across different platforms.
- Regularly verify the package contents with `oe-pkgdata-util` to prevent any files from being misplaced.
- Manage dependencies carefully using `RDEPENDS` and `RRECOMMENDS`.

---

### Conclusion

This tutorial covers the process of package splitting during the `do_package()` stage in Yocto using the `PACKAGES` and `FILES` variables. With this knowledge, you can efficiently manage how your recipe’s files are divided into packages, ensuring that each package contains the right components for deployment on the target system.

Feel free to customize further for your specific project needs. Let me know if you have more questions related to Yocto Kirkstone!
