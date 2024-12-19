```markdown
# Yocto: Detailed Guide on Package Splitting During the `do_package()` Stage Using `PACKAGES` and `FILES` Variables

When building packages with Yocto, especially with the **Kirkstone** release (or any other release), managing how files are split into different packages is essential. The **`PACKAGES`** and **`FILES`** variables are used to control which files go into which packages.

Here is a step-by-step tutorial that will provide you with in-depth knowledge about package splitting during the `do_package()` stage.

---

## 1. **The `do_package()` Task**

Yocto's `do_package()` stage is responsible for taking the compiled files from the `WORKDIR` and splitting them into multiple output packages. The splitting is governed by two key variables:

- **`PACKAGES`**: Defines the list of output packages.
- **`FILES`**: Specifies which files go into which package.

These packages can then be installed on a target system, and each one can contain specific files, libraries, or binaries.

---

## 2. **Understanding the `PACKAGES` Variable**

The `PACKAGES` variable lists all the packages that will be created by your recipe. If you don’t explicitly define `PACKAGES`, Yocto uses defaults.

### Default Packages

Yocto provides default package names if you don’t define them:

- **`${PN}`**: The main package (package name comes from the recipe name).
- **`${PN}-dev`**: Development files (headers, etc.).
- **`${PN}-dbg`**: Debug symbols.
- **`${PN}-doc`**: Documentation files.
- **`${PN}-staticdev`**: Static libraries.

### Customizing `PACKAGES`

You can override the default and create custom packages:

```bash
PACKAGES = "${PN}-bin ${PN}-lib ${PN}-doc ${PN}-config"
```

Here, `${PN}-bin`, `${PN}-lib`, `${PN}-doc`, and `${PN}-config` are custom packages. You will define what files go into each of these packages using the `FILES` variable.

---

## 3. **Understanding the `FILES` Variable**

The `FILES` variable is used to specify what files go into each package listed in `PACKAGES`. Each package must have an associated `FILES_` variable, which contains the list of paths or files to be included in that package.

### Example:

```bash
FILES_${PN}-bin = "${bindir}/*"
FILES_${PN}-lib = "${libdir}/*.so*"
FILES_${PN}-doc = "${docdir}/*"
FILES_${PN}-config = "${sysconfdir}/*"
```

Here’s what these variables do:

- **`${bindir}`**: The directory where binaries go (typically `/usr/bin`).
- **`${libdir}`**: The directory for libraries (usually `/usr/lib`).
- **`${docdir}`**: The directory for documentation (typically `/usr/share/doc`).
- **`${sysconfdir}`**: The directory for configuration files (usually `/etc`).

Each `FILES_` entry specifies the file path patterns to include in the respective package.

---

## 4. **File Locations and Variables**

Yocto uses predefined variables to refer to specific directories in the root filesystem. Here’s a list of commonly used ones:

- **`${bindir}`**: `/usr/bin` — Binary executables.
- **`${sbindir}`**: `/usr/sbin` — System binaries.
- **`${libdir}`**: `/usr/lib` — Libraries.
- **`${includedir}`**: `/usr/include` — Header files.
- **`${datadir}`**: `/usr/share` — Architecture-independent data.
- **`${docdir}`**: `/usr/share/doc` — Documentation.
- **`${mandir}`**: `/usr/share/man` — Manual pages.
- **`${sysconfdir}`**: `/etc` — System configuration files.

Use these variables to specify file locations in your packages.

---

## 5. **Example: Creating a Custom Package**

### Step 1: Define `PACKAGES`

```bash
PACKAGES = "${PN}-bin ${PN}-lib ${PN}-doc ${PN}-config"
```

- **`${PN}-bin`**: Will contain binary files.
- **`${PN}-lib`**: Will contain shared libraries.
- **`${PN}-doc`**: Will contain documentation.
- **`${PN}-config`**: Will contain configuration files.

### Step 2: Define `FILES`

```bash
FILES_${PN}-bin = "${bindir}/*"
FILES_${PN}-lib = "${libdir}/*.so*"
FILES_${PN}-doc = "${docdir}/*"
FILES_${PN}-config = "${sysconfdir}/*"
```

### Step 3: Additional Customizations

Include more specific patterns in `FILES` if needed. For instance:

```bash
FILES_${PN}-lib = "${libdir}/libexample.so.*"
```

### Step 4: Other Package Types

For development and debug packages:

```bash
FILES_${PN}-dev = "${includedir}/* ${libdir}/*.a ${libdir}/*.la"
FILES_${PN}-dbg = "${bindir}/.debug/* ${libdir}/.debug/*"
```

---

## 6. **Fine-Tuning with `RDEPENDS` and `RRECOMMENDS`**

- **`RDEPENDS_`**: Specifies runtime dependencies of a package.
- **`RRECOMMENDS_`**: Specifies recommended runtime dependencies (optional but suggested).

Example:

```bash
RDEPENDS_${PN}-config = "${PN}-bin"
```

---

## 7. **Splitting Static and Shared Libraries**

For libraries:

```bash
PACKAGES = "${PN}-staticdev ${PN}-lib ${PN}-dev"
FILES_${PN}-lib = "${libdir}/*.so*"
FILES_${PN}-staticdev = "${libdir}/*.a"
FILES_${PN}-dev = "${includedir}/*"
```

---

## 8. **Testing and Debugging Package Splitting**

After writing your recipe:

1. Build the recipe:
   ```bash
   bitbake <recipe-name>
   ```

2. Inspect generated packages:
   ```bash
   oe-pkgdata-util list-pkg-files <package-name>
   ```

---

## 9. **Best Practices**

- Separate development and runtime files.
- Use directory variables like `${bindir}` and `${libdir}` for portability.
- Verify package contents using `oe-pkgdata-util`.
- Manage dependencies carefully using `RDEPENDS` and `RRECOMMENDS`.

---

## Author: Mahendra Sondagar
