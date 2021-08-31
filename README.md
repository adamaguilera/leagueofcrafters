# league-of-crafters

# Local Setup on IntelliJ
- Ensure java version 16 is installed on your local machine with a JDK
- Ensure you have a server folder in the same directory that holds `somplugin`
    - Preferably, use the somserver as the server accessible [here](https://gitlab.com/adamaguilera/locserver)
- Clone the project and open it in IntelliJ
- Under Project Structure >>> Project >>> Project SDK set java to 16
- Under Project Structure >>> Artifacts, create one for building the JAR plugin file
    - Set the output directory to the server plugin folder you want to test in
    - Possible fix to invalid plugins.yml add `./resources/plugin.yml` in the before build
- Edit Configurations
    - Create JAR Application with the directory set to the server directory, JAR path set to server JAR file
        - Under before build, add build artifact that was added above
    - Create Maven Dependencies with command line setting set to `package`
    - Create Shell Script with directory set to `./scripts` and shell script path set to `./scripts/start-dev.sh`
        - Under before build, add Maven Dependencies and build artifact
    