# Talkie Android client

Work in progress.

## Development

  * Install Android SDK and add its location to env/path variable `ANDROID_HOME`,
  * Install SBT 13.9,
  * Install IntelliJ Idea 15,
  * Install Scala and SBT plugins in IntelliJ,
  * For editing sources:
 
    * In IntelliJ select *Import existing project*,
    * Select *SBT*,
    * Select downloading dependencies, documents and sources,
    * Once imported IDE can be used for editing sources, starting Android Emulator and starting Android Device Manager,
    * It might be useful to set filter `com.talkie.client` for ADM,
    
 * For running application:
 
    * Run emulator using *Tools*->*Android*->*ADV Manager* and selecting *Nexus*,
    * IN a similar manner run *Android Device Manager* to monitor device and preview logs,
    * Run `sbt` within project directory and select `project app` and run `android:run` task.
