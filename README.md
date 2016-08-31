LomLib
======

Just a set of utilities that I use often. A prerequisite for most my mods.

## Website and Download

[http://lomeli12.net/minecraft-mods/lomlib](http://lomeli12.net/minecraft-mods/lomlib)

## Version

**Current Stable Version:** 5.0.0

**Dev Version:** 5.0.0

## Compiling your mod with LomLib

Open up your *build.gradle* file with your favorite text editor and add this to it:

    repositories {
	    maven {
        	name = 'Lomeli Repo'
        	url = "http://maven.lomeli12.net/"
    	}
	}
	
	dependencies {
    //example:  compile "net.lomeli:LomLib:{MCVersion}-{LomLibVersion}:dev"
    	compile "net.lomeli:LomLib:1.7.10-3.0.0:dev"
	}

Remember to modify the version number to match the version of LomLib you're using. Then just run `gradlew build` like usual and your mod should compile like normal.