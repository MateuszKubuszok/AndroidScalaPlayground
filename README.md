# Talkie Android client

Work in progress.

## Development

  * Install Android SDK and add its location to env/path variable `ANDROID_HOME`,
  * Install SBT 13.11,
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

 * For watching logs:
 
    * build application in debug mode and deploy: `sbt "project app" android:debug`,
    * within IntelliJ either

      * open *Android* view,
      * navigate into *logcat*,
      * select `com.talkie.client` application,

    * or use *Android Device Monitor* for the same purpose.
    
 * For debugging:
 
    * deploy debug app as above,
    * check the right port for application using *Android Device Monitor* (numbers next to `com.talkie.client` on last
     column),
    * configure *Remote Debug* *Run Configuration*: *Host* -> `localhost`, *Port* -> `port that you found`,
    * run the task and start debugging. 
