# RangerPolicyTool
This tool is used to convert HDFS Native Permissions to Ranger accepted permission format.

## Requirements inorder run the tool
1. Download the keytab file in the local machine. Run this as a super-user so you will require a super-user keytab. Kinit using the keytab in the local machine.
2. Download the krb5.conf from the cluster's /etc/krb5.conf and place it in local machine /etc/krb5.conf

## Steps
Build the project
``` 
mvn clean install 
```
Once the build is complete under `target` there will be a jar `RangerPolicyTool-1.0-SNAPSHOT-jar-with-dependencies.jar`
``` 
java -cp ./target/RangerPolicyTool-1.0-SNAPSHOT-jar-with-dependencies.jar com.RangerPolicyTool.RangerPolicyMigrationTool 
namenode:port

namenode:port - The namenode uri and the hdfs port, Eg. abc.com:8020
```
This will generate `RangerPolicy.csv` in the required Ranger format.
