# RangerPolicyTool
This tool is used to convert HDFS Native Permissions to Ranger accepted permission format.

## Requirements inorder run the tool
1. Download the krb5.conf from the cluster's /etc/krb5.conf and place it in local machine /etc/krb5.conf
2. Download the keytab file from the cluster to the local machine. Run this as a super-user so you will require a super-user keytab. Kinit using the keytab in the local machine.

## Steps
Build the project
``` 
mvn clean install 
```
Once the build is complete under `target` there will be a jar `RangerPolicyTool-1.0-SNAPSHOT.jar`. Run it using the following command,
``` 
java -jar ./target/RangerPolicyTool-1.0-SNAPSHOT.jar hdfs_namenode:hdfs_port source_directory

(or)

hadoop jar ./target/RangerPolicyTool-1.0-SNAPSHOT.jar hdfs_namenode:hdfs_port source_directory


hdfs_namenode:hdfs_port - The namenode uri and the hdfs port, Eg. abc.com:8020
source_directory - The root directory from where the permission scan begins, Eg "/dir1" or "/"
```
This will generate `RangerPolicy.csv` in the required Ranger format.
