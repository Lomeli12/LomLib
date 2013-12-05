LomLib
======

Just a set of utilities that I use often. A prerequisite for most my mods.

## Website and Download

http://anthony-lomeli.net/?page_id=24

## Version

**Current Stable Version:** 1.0.7

**Dev Version:** 1.0.8

## How to develop with LomLib

### Setting up your project to use LomLib
***

#### Easy Way

1. Setup your Forge Development Environment like normal
2. Download the latest Dev Version from http://anthony-lomeli.net/MinecraftMods/LomLib/dev/
3. Place the development jar in `jars/mods`(or equivalent).
4. Add the jar to your project's build path. In eclipse, this can be done in the package exploerer by *Right-Clicking your project->Properties->Java Build Path->Libraries->Add External JARs...*

#### Hard Way

1. Setup your Forge Development Environment like normal
2. Download LomLib's source
3. Create a new Project in Eclipse called LomLib
4. Place LomLib's source in the appropriate folders
5. Add the LomLib project to your project's build path. In eclipse, this can be done in the package exploerer by *Right-Clicking your project->Properties->Java Build Path->Projects->Add...*

### Compiling your mod with LomLib
***

Depending on whether your using ForgeGradle or not will effect how you compile your mod. 

#### If you're using pre-ForgeGradle enviroment

**If using Easy Way:** Just drop a copy of the dev jar in your `/mcp/lib` folder and compile like normal.

**If using Hard Way:** Make sure to include LomLib's source with your mod when compiling and not to include the class files when packaging your mod. I recommend using Apache Ant to automate this and make your life easier.

#### If you're using a ForgeGradle Enviroment

Open up your *build.gradle* file with your favorite text editor and add this to it:

    repositories {
	    ivy {
        	name 'LomLib'
        	artifactPattern "http://anthony-lomeli.net/MinecraftMods/LomLib/dev/[module]-dev-[revision].[ext]"
    	}
	}
	
	dependencies {
    	compile name: 'LomLib, version: '16-1.0.8', ext: 'jar'
	}

Remember to modify the version number to match the version of LomLib you're using. Then just run `gradlew build` like usual and your mod should compile like normal.


