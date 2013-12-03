LomLib
======

Just a set of utilities that I use often. A prerequisite for most my mods.

## Website and Download

http://anthony-lomeli.net/?page_id=24

## Current Version

1.0.7

## Dev Version

1.0.8

## How to develop with LomLib

### New Forge Gradle way
1. Download and install the JDK and all the class path stuff (not gonna show here cause you should know).

2. Download LomLib's source

**If you want to compile LomLib**

1. Run `gradlew.bat build` (Windows) or `./gradlew build` (Unix)
2. Run `gradlew.bat setupDevWorkspace` (Windows) or `./gradlew setupDevWorkspace` (Unix)

3. A compiled version of LomLib will be located in the *build/libs* folder

**If you want to develop with LomLib**

1. Follow this tutorial on how to setup ForgeGradle http://www.minecraftforge.net/forum/index.php/topic,14048.0.html

2. During the setup a folder called *mods* should've been created. Place LomLib_dummy.jar into that folder.

3. Copy LomLib's source and resources to their repective folders in the ForgeGradle workspace.

### Old Way
1. Download JDK, Scala, and Forge and install each.
2. Download LomLib source
3. Place LomLib_dummy.jar into <development directory>\forge\mcp\jars\mods\
4. Place the contents of *common* into <development directory>\forge\mcp\src\minecraft\
5. Enjoy
