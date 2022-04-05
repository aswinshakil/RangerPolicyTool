# HDFSPermissionsExportTool
This tool is used to export HDFS Native Permissions to a file in a specific csv format.

## Requirements
1. Download the keytab file from the cluster to the local machine. Run this as a super-user so you will require a super-user keytab. 
2. Kinit using the keytab in the local machine.

## Steps
Build the project
``` 
mvn clean install 
```
Once the build is complete under `target` there will be a jar `HDFSPermissionsExportTool-1.0-SNAPSHOT.jar`. Run it using the following command,
``` 
hadoop jar ./target/HDFSPermissionsExportTool-1.0-SNAPSHOT.jar hdfs_namenode:hdfs_port source_directory


hdfs_namenode:hdfs_port - The namenode uri and the hdfs port, Eg. abc.com:8020
source_directory - The root directory from where the permission scan begins, Eg "/dir1" or "/"
```
This will generate `HDFS_Permissions_Export_timestamp.csv` in the required Ranger format. The permission are sorted in ascending order. 

## Sample Output
```
"/dir1"|"hdfs"|"supergroup, public"|"read, execute"
"/dir1/dir1"||"supergroup, public"|"read, execute"
"/dir1/dir1"|"hdfs"||"read, write, execute"
```
