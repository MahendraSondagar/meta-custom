# Yocto Tutorial: Understanding the PREFERRED_VERSION Variable

The `PREFERRED_VERSION` variable in Yocto allows you to specify which version of a package should be used when multiple versions of a recipe are available. This tutorial explains its definition, usage, and demonstrates its application with examples, including the use of wildcard characters.

## Definition of PREFERRED_VERSION

In Yocto, the `PREFERRED_VERSION` variable is used to instruct the build system to select a specific version of a recipe when multiple versions exist. By setting this variable, you can control which version is included in the final build.

## Use Case Scenario

### Example Setup

Suppose you have the following two recipes:

#### Recipe 1: `mypreferred-ver_1.0.0.bb`
```sh
SUMMARY = "Example recipe version 1.0.0"
DESCRIPTION = "This is version 1.0.0 of mypreferred-ver."
LICENSE = "MIT"
PR = "r0"
```

#### Recipe 2: `mypreferred-ver_2.0.0.bb`
```sh
SUMMARY = "Example recipe version 2.0.0"
DESCRIPTION = "This is version 2.0.0 of mypreferred-ver."
LICENSE = "MIT"
PR = "r1"
```

When building the image, Yocto needs to decide which version to include. By default, the build system may select the latest version (e.g., `2.0.0`).

### Using PREFERRED_VERSION

To explicitly select version `2.0.0` of `mypreferred-ver`, you can use the `PREFERRED_VERSION` variable in your `local.conf` file as follows:

```sh
PREFERRED_VERSION_mypreferred-ver = "2.0.0"
```

With this setting, Yocto will prefer `mypreferred-ver_2.0.0.bb` over `mypreferred-ver_1.0.0.bb`.

### Verification

Run the following command to confirm the version selection:
```sh
bitbake -e mypreferred-ver | grep ^PREFERRED_VERSION
```

You should see the output reflecting the selected version.

## Using Wildcard Characters with PREFERRED_VERSION

Wildcard characters (`%`) can be used in the `PREFERRED_VERSION` variable to specify a range or pattern for version selection.

### Example

#### Recipes
- `mypreferred-ver_1.0.0.bb`
- `mypreferred-ver_1.1.0.bb`
- `mypreferred-ver_2.0.0.bb`

#### Configuration
To select any version starting with `1.`, you can set:
```sh
PREFERRED_VERSION_mypreferred-ver = "1.%"
```

### Behavior
In this case, Yocto will prefer the highest version that matches the pattern `1.%`. For the example above, it will select `mypreferred-ver_1.1.0.bb`.

### Example with Exact Match Override
If you want to ensure a specific version, such as `1.0.0`, you can explicitly set:
```sh
PREFERRED_VERSION_mypreferred-ver = "1.0.0"
```
This will override any wildcard matching.

## Key Points to Remember

1. **Version Selection**: Use `PREFERRED_VERSION` to select a specific version of a recipe.
2. **Flexibility with Wildcards**: `%` allows flexible matching for version patterns.
3. **Configuration Location**: Add the `PREFERRED_VERSION` setting in your `local.conf` or a similar configuration file.
4. **Verification**: Always verify your settings using `bitbake -e` to ensure the correct version is selected.

## Summary
The `PREFERRED_VERSION` variable is a powerful tool for managing multiple recipe versions in Yocto. With proper use, including wildcard characters, you can ensure your build system selects the most appropriate version for your needs.

---

AUTHOR : Mahendra Sondagar (mahendraSondagar08@gmail.com)
