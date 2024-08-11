# USB Bluetooth adapter configuration with custom Yocto image 

## Part_1
[![N|Solid](https://cldup.com/dTxpPi9lDf.thumb.png)](https://nodesource.com/products/nsolid)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

### The hardware 
This tutorial intends to dig into the Bluetooth adapter configuration with the  Yocto custom image 
For the instance, I'm using TP-LINK Bluetooth 5.0 USB adapter, called  *"___TP-Link UB-500___"*
One can find the product page here [LINK](doc:https://www.tp-link.com/in/home-networking/adapter/ub500/)

![Image](https://static.tp-link.com/upload/image-line/Overview_UB500_US_2106_01_normal_20210805112046h.jpg)

As the linux board, here I'm using STM32MP1 dk board from the ST-Microelectronics 
However, the steps remain same for any linux SBC 

![Image](https://media.digikey.com/Photos/STMicro%20Photos/MFG_STM32MP157A-DK1.jpg)

------------------------------------
### The problem statement 

 * No Linux compatibility 
 * Lack of the Linux compatible drivers 
 * Lack of the Yocto resources to configure the drivers with the custom image 

After facing such a problem, I decided to write the full tutorial to add the Bluetooth adaptor support with the custom Yocto image 

-------------------------------------
### Steps to follow 

 > [x]  Step_1: Manually activate the Bluetooth & USB support using menuconfig 
 > [x] Step_2: Identifying the Bluetooth chipset of the USB Bluetooth adaptor 
 > [x] Step_3: Patching the drivers to modify it 
 > [x] Step_4: Adding the Firmware support for the required Bluetooth chipset into the recipes 
 > [x] Step_5: Customising the image, and installing it with the board 
 > [x] Step_6: Activating the Bluetooth with the hcitool
 
### Let's start with the step-by-step ✨
-----------------------------------------
### Step_1: 
1.1 To activate the Bluetooth support with the Yocto image, we can use the menuconfig with the bitbake tool
``` sh
    bitbake -c menuconfig virtual/kernel
```

1.2 The following are some of the drivers which need to activate using menuconfig 
``` sh
[*] Networking support --->
     <*> Bluetooth subsystem support --->
        [*]   Bluetooth Classic (BR/EDR) features
        <*>     RFCOMM protocol support
        [*]       RFCOMM TTY support
        <*>     BNEP protocol support
        [*]       Multicast filter support
        [*]       Protocol filter support
        <*>     HIDP protocol support 
        [*]     Bluetooth High Speed (HS) features (NEW) 
        [*]   Bluetooth device drivers
              <*> HCI USB driver        
              
Device Drivers  --->
      [*] USB support  --->
            MUSB Mode Selection (Host only mode)--->
		(X) Host only mode
		( ) Gadget only mode
   		( ) Dual Role only mode
```

1.3 After activating the drivers, now it's time to create the defconfig using the following command 

``` sh
  bitbake -c savedefconfig virtual/kernel
```
1.4 The defconfig file will be found in following directory 

```sh
  build-mp1/tmp/work/stm32mp1-poky-linux-gnueabi/linux-stm32mp/5.10.61-stm32mp-r2-r0/build/defconfig
```
1.5 Now, it's time to create the recipes to customize the Yocto image using defconfig, let's say "recipes-kernel"
```sh
 mkdir -p  recipes-kernel/linux/file
```
1.6 Now, copy the previously created defconfig file into the "*___recipes-kernel/linux/file___*" directory

1.7 Now it's time to create the recipes append files to customize the image into the *"__recipes-kernel/linux__"* directory 

``` sh
 vim linux-stm32mp_%.bbappend
```

1.8 The content of the linux-stm32mp_%.bbappend are as follows

``` sh
 FILESEXTRAPATHS_prepend := "${THISDIR}: ${THISDIR}/files:"

# Apply default configuration
KERNEL_DEFCONFIG_stm32mp1 = "defconfig"

```
*Note: The care should be taken to prepare the "*___linux-stm32mp_%.bbappend___*" file as it's depending on the kernel name*
*To check the kernel name, apply the following command "bitbake -e | grep virtual/kernel"*
*The file prefix would be the kernel name such as "*___xyz-kernel_%.bbappend___*"

1.9 Add the Bluetooth relevant packages in the filesystem, they're enabled in custom-image by default.
    In may case, i have the custom-image.bb file in *"___meta-custom/recipes-core/images/___"* directory
    Here "meta-custom" is the custom layer that i have added before 
    Creating and adding the custom layer is the out of the scope for the instance!
    
``` sh
vim build/conf/local.conf
IMAGE_INSTALL_append = " \
        dbus \
        bluez5 \
        packagegroup-tools-bluetooth \
        expat \
        play "
DISTRO_FEATURES_append = " \
       bluetooth \ 
       "
```

2.0 Now, it's time to bake the image. In my case, my image name is "custom-image"

``` sh
 time bitbake custom-image
```
2.1 The result will be stored in to the *___tmp/deploy/images/stm32mp1___* directory. You need to flash the image with the board 

> After loading the image in to the board, you keen to know wheather the configuration works or not 
> Unfortunately, the above steps are not sufficiant
> Not only that, but bluetooth unable to seems advertise   😭
> To resolve the issue, we need to move forward all the steps that we have discuss at the begining  😀 

