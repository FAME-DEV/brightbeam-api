# brightbeam-api
# Brightbeam API Program

## 1. Introduction

This API program processes two JSON files and calculates the average cost of a property. The program is written in Java and is executed via shell (.sh) and batch (.bat) scripts.

There is no graphical user interface (GUI) or API endpoint for accessing the results. Instead, running the provided `.sh` script with the necessary input files processes the data and displays the output directly in the console.

## 2. How to Run

Download or unzip the Brightbeam API files and navigate to the `brightbeam-api` folder. The `brightbeam_api_run.sh` file is located inside this folder.

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

```sh
./brightbeam_api_run.sh <file1_path> <file2_path>
```

#### Example:

```sh
./brightbeam_api_run.sh /home/user/dublin_trees.json /home/user/dublin-property.csv"
```

### Option 2: Use Default (Hardcoded) File Paths

If no arguments are provided, the script uses predefined file paths:

```sh
./brightbeam_api_run.sh
```

To modify the default file paths, open the `brightbeam_api_run.sh` file in a text editor and update the file path variables with your desired values before running the script.

## 5. Script Behavior

- If two arguments are passed, they are used as file paths.
- If no arguments are provided, the script defaults to predefined file paths.
- The script sets the necessary environment variables and executes the Java application to process the input files.

This program provides a straightforward way to compute the average property cost by simply running a script with or without file path arguments.

output on console like this 
Properties with tall trees 41: average cost : €1095486.4668292683
Properties with short trees 178: average cost : €543757.9113483146


Note: I created only 2 junit test cases for cost logic, on file reading and parsing process not wrote any junit test case
