# SpigotUpdateChecker

[![GitHub Release](https://img.shields.io/github/release/lightlibs/SpigotUpdateChecker.svg?style=flat)]()

## Description
An update checker library for Spigot/Paper plugins. Compatible with Folia.

## How to use
```java
@Override
public void onEnable() {

  ...

  JavaPlugin plugin = this; // The instance of your plugin class
  String prefix = "[MyPlugin] "; // How you want the update checker messages to be prefixed
  int resourceId = 00000; // Get this from the spigot resource URL

  // Will automatically check for updates and inform users
  SimpleUpdateChecker.checkUpdate(plugin, prefix, resourceId);

  // Optional: Folia Compatibility
  // You can optionally supply a Consumer<Runnable>. Here is an example using FoliaLib
  // (https://github.com/TechnicallyCoded/FoliaLib)
  ServerImplementation foliaScheduler = this.foliaLib.getImpl();
  SimpleUpdateChecker.checkUpdate(plugin, prefix, resourceId, runnable -> foliaScheduler.runAsync(wt -> runnable.run());
}
```

## How to add this to your project
### Gradle
```groovy
plugins {
    id 'com.github.johnrengelman.shadow' version '8.1.1'
}

repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.lightlibs:SpigotUpdateChecker:[VERSION-AT-TOP-OF-PAGE]'
}

shadowJar {
    relocate 'com.tcoded.lightlibs.updatechecker', "[YOUR-PLUGIN-PACKAGE-HERE].lib.spigotupdatechecker"
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.lightlibs</groupId>
    <artifactId>SpigotUpdateChecker</artifactId>
    <version>[VERSION-AT-TOP-OF-PAGE]</version>
</dependency>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.4.1</version>
            <configuration>
                <relocations>
                    <relocation>
                        <pattern>com.tcoded.lightlibs.updatechecker</pattern>
                        <shadedPattern>[YOUR-PLUGIN-PACKAGE-HERE].lib.spigotupdatechecker</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
        </plugin>
    </plugins>
</build>
```
