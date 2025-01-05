# Tutorial on the `PREFERRED_PROVIDER` Variable in Yocto

The `PREFERRED_PROVIDER` variable in Yocto is a powerful mechanism that allows developers to control which provider recipe should be selected when multiple recipes provide the same functionality. This is particularly useful in cases where multiple recipes use the `PROVIDES` variable to declare that they can fulfill a particular dependency.

## Definition of `PREFERRED_PROVIDER`

`PREFERRED_PROVIDER` specifies the preferred provider of a particular functionality or virtual package when multiple recipes declare themselves as providers. It helps Yocto resolve conflicts by explicitly choosing one recipe over others.

## Use Cases

1. **Resolving Conflicts Between Recipes**: When multiple recipes provide the same functionality (via `PROVIDES`), the `PREFERRED_PROVIDER` variable ensures that a specific recipe is selected.
2. **Customizing Builds**: Allows developers to customize their builds by selecting specific implementations of a functionality.
3. **Streamlining Development**: Helps in development environments where specific versions or implementations are preferred.

## Example: Using `PREFERRED_PROVIDER` with `myhello` and `mygit`

### Scenario

- Two recipes, `myhello` and `mygit`, both declare `PROVIDES += "mylwl"`.
- You want to prefer the `myhello` recipe over `mygit` as the provider for `mylwl`.

### Recipe 1: `myhello`

#### File: `recipes-example/myhello/myhello_1.0.0.bb`
```bash
SUMMARY = "Hello World Provider"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://hello-world.c"
S = "${WORKDIR}"

PROVIDES += "mylwl"

do_compile() {
    ${CC} ${CFLAGS} -o hello-world hello-world.c
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 hello-world ${D}${bindir}
}
```

### Recipe 2: `mygit`

#### File: `recipes-example/mygit/mygit_1.0.0.bb`
```bash
SUMMARY = "Git-Based Provider"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://example.com/repo.git;branch=main"
S = "${WORKDIR}/git"

PROVIDES += "mylwl"

do_compile() {
    ${CC} ${CFLAGS} -o git-provider main.c
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 git-provider ${D}${bindir}
}
```

### Configuration in `local.conf`

To ensure that `myhello` is the preferred provider for `mylwl`, add the following line to your `local.conf`:

```bash
PREFERRED_PROVIDER_mylwl = "myhello"
```

### How It Works

1. Both `myhello` and `mygit` declare `PROVIDES += "mylwl"`.
2. When Yocto encounters a dependency on `mylwl`, it consults the `PREFERRED_PROVIDER_mylwl` variable.
3. Since `PREFERRED_PROVIDER_mylwl` is set to `myhello`, the build system uses `myhello` as the provider for `mylwl`.

### Verification

To verify that the correct provider is selected:

1. Run the following command to check the selected provider:
   ```bash
   bitbake -e | grep ^PREFERRED_PROVIDER_mylwl
   ```
2. Ensure the output matches:
   ```bash
   PREFERRED_PROVIDER_mylwl="myhello"
   ```

### File Structure

```plaintext
recipes-example/
├── myhello/
│   ├── files/
│   │   └── hello-world.c
│   └── myhello_1.0.0.bb
├── mygit/
│   ├── files/
│   │   └── main.c
│   └── mygit_1.0.0.bb
```

## Key Points

- The `PREFERRED_PROVIDER` variable is critical for resolving conflicts when multiple recipes provide the same functionality.
- It allows developers to control and customize the build system's behavior explicitly.
- Always verify the selected provider using bitbake commands to ensure the desired configuration is applied.

---

## AUTHOR : Mahendra Sondagar (mahendrasondagar0@gmail.com)
