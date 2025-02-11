# Brightbeam API Program

## 1. Introduction

This API program processes two JSON files and calculates the average cost of a property. The program is written in Java and is executed via shell (.sh) and batch (.bat) scripts.

There is no graphical user interface (GUI) or API endpoint for accessing the results. Instead, running the provided `.sh` script with the necessary input files processes the data and displays the output directly in the console.

## 2. How to Run
Dowbload the code from Github 

 ```sh
  git clone git@github.com:FAME-DEV/brightbeam-api.git
  cd brightbeam-api 
  ```

Download the Brightbeam API files and navigate to the `brightbeam-api` folder. The `brightbeam_api_run.sh` file is located inside this folder.

The `brightbeam_api_run.sh` script build and create a jar file and executes the `brightbeam-api-0.0.1.jar` Java application, setting JSON file paths dynamically.

### Features:

- Accepts file paths as command-line arguments.
- Defaults to pre-configured file paths if no arguments are provided.

## 3. Prerequisites

Before running the script, ensure the following:

- Java is installed (`java -version` to check).
- The `brightbeam-api` folder is in the same directory as `brightbeam_api_run.sh`.
- Grant execution permissions to the script:
  ```sh
  chmod +x brightbeam_api_run.sh
  ```

## 4. Running the Script

### Option 1: Provide File Paths Dynamically

You can specify the input files as arguments:
 # file1_path = dublin-trees.json
 # file2_path = dublin-property.csv

```sh
./brightbeam_api_run.sh <file1_path> <file2_path>
```

#### Example:

```sh
./brightbeam_api_run.sh /home/user/dublin_trees.json /home/user/dublin-property.csv"
```
# Make sure file_1 and file_2 is correct, first file should be JSON and second should CSV

### Option 2: Use Default (Hardcoded) File Paths

If no arguments are provided, the script uses predefined file paths:
To modify the default file paths, open the `brightbeam_api_run.sh` file in a text editor and update the file path variables with your desired values before running the script.

```sh
nano brightbeam_api_run.sh
```
Make changes in the file.
Press CTRL + X to exit.
Press Y to save changes.
Press Enter to confirm.

the script uses predefined file paths
```sh
./brightbeam_api_run.sh
```



## 5. Script Behavior

- If two arguments are passed, they are used as file paths.
- If no arguments are provided, the script defaults to predefined file paths.
- The script sets the necessary environment variables and executes the Java application to process the input files.

This program provides a straightforward way to compute the average property cost by simply running a script with or without file path arguments.

output on console like this 
Properties with tall trees 41: average cost : €1095486.4668292683
Properties with short trees 178: average cost : €543757.9113483146

<img width="1480" alt="image" src="https://github.com/user-attachments/assets/e81c91f0-ea55-40b4-a5ae-89706d654ba5" />



Note: I created only 2 junit test cases for cost logic, on file reading and parsing process not wrote any junit test case
