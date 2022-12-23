# File Server
Accepts files with specified extensions.

### Endpoints
```GET api/v1/file```  
```POST api/v1/file```  
```DELETE api/v1/file```  

## Configuration
Open application.yaml file and set up  
 * ``application.file-directory`` - directory of all stored files
 * ``application.allowed-extensions`` - all allowed extensions (for example ``txt,pdf``)

## Compile

1. Go to project directory
```bash
  cd project-folder
```

2. Execute gradle tasks
```bash
  ./gradlew clean
  ./gradlew bootJar
```

3. Jar file will be located in the /build/libs folder

## Run

1. Go to the project directory

```bash
  cd /build/libs
```

2. Start the application

```bash
  java -jar FileServer-1.0.jar
```