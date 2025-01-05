# Tutorial: Understanding and Using the `PROVIDES` Variable in Yocto

The `PROVIDES` variable in Yocto is a powerful tool that allows developers to define alternative names (aliases) for a recipe. This tutorial will provide a detailed understanding of the `PROVIDES` variable, its use cases, and a practical example.

---

## **What is the `PROVIDES` Variable?**

In Yocto, each recipe has a unique identifier, typically its filename. However, there are scenarios where you may want a recipe to provide multiple names or aliases for dependency resolution. The `PROVIDES` variable enables this functionality by listing additional aliases that a recipe can supply.

### **Key Points**
- **Default Behavior:** By default, the recipe name (e.g., `myrecipe`) is implicitly added to the `PROVIDES` variable.
- **Custom Aliases:** You can add aliases explicitly using `PROVIDES += "alias_name"`.
- **Dependency Resolution:** Other recipes or images can refer to the recipe by any of its aliases for dependency installation.

---

## **Use Cases for `PROVIDES`**

1. **Virtual Providers:**
   - When multiple recipes can provide the same functionality, `PROVIDES` can define a common name (e.g., `virtual/kernel`).
   - Example:
     ```bitbake
     PROVIDES += "virtual/kernel"
     ```

2. **Multiple Names for a Recipe:**
   - Useful when you want to reference a recipe using different names in various contexts.
   - Example:
     ```bitbake
     PROVIDES += "keyboard"
     ```

3. **Migration or Aliases:**
   - Helps during recipe renaming or when maintaining backward compatibility.

---

## **Practical Example**

### Recipe Structure
Below is an example recipe that demonstrates the usage of the `PROVIDES` variable.

**Directory Structure:**
```
myprovides/
├── files
│   └── hello-test.cpp
└── myprovides_1.0.0.bb
```

### Recipe: `myprovides_1.0.0.bb`

```bitbake
# Package summary
SUMMARY = "Yocto examples for the PROVIDES variable"
# License, for example MIT
LICENSE = "MIT"
# License checksum file is always required
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# hello-test.cpp from local file
SRC_URI = "file://hello-test.cpp"

# Set LDFLAGS options provided by the build system
TARGET_CC_ARCH += "${LDFLAGS}"

# Change source directory to work directory where hello-test.cpp is
S = "${WORKDIR}"

# Compile hello-test from sources, no Makefile
do_compile() {
    ${CXX} -Wall hello-test.cpp -o hello-test
}

# Explicit declaration of the recipe name via PROVIDES variable
PROVIDES += "keyboard"

# Install binary to final directory /usr/bin
do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/hello-test ${D}${bindir}
}
```

### Source File: `files/hello-test.cpp`

```cpp
#include <iostream>

int main() {
    std::cout << "Hello from the PROVIDES example!" << std::endl;
    return 0;
}
```

---

## **Steps to Test `PROVIDES`**

### **1. Build the Recipe**
Run the following command to build the recipe:
```bash
bitbake myprovides
```

### **2. Inspect the `PROVIDES` Variable**
Check the aliases provided by the recipe:
```bash
bitbake -e myprovides | grep ^PROVIDES=
```
**Expected Output:**
```bash
PROVIDES="myprovides keyboard"
```

### **3. Use Alias in `IMAGE_INSTALL`**
To include the alias (`keyboard`) in an image, add it to the `IMAGE_INSTALL` variable in your image recipe (e.g., `custom-image.bb`):

```bitbake
IMAGE_INSTALL_append = " keyboard"
```

### **4. Build the Image**
Build the custom image:
```bash
bitbake custom-image
```

---

## **Important Notes**
- **Alias Resolution:** Although aliases work for dependency resolution, `IMAGE_INSTALL` generally requires the actual recipe name unless the alias is explicitly declared as a runtime provider using `RPROVIDES`.
- **Best Practices:** Use `PROVIDES` judiciously to avoid confusion or unintended conflicts with other recipes.

---

## Author: Mahendra Sondagar (mahendrasondagar08@gmail.com)

