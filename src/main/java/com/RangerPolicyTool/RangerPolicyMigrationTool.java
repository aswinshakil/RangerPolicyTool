package com.RangerPolicyTool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.security.UserGroupInformation;

public class RangerPolicyMigrationTool {
  public static String domain;
  public static String uri;
  public static String newLine = System.lineSeparator();
  public static File outputFile;
  public static FileWriter fileWriter;
  public static BufferedWriter bufferedWriter;

  public static void main(String args[]) throws IOException {
    Configuration config = new Configuration();
    domain = args[0];
    uri = "hdfs://" + domain +"/";
    outputFile = new File("RangerPolicy.csv");
    fileWriter = new FileWriter("RangerPolicy.csv");
    bufferedWriter =  new BufferedWriter(fileWriter);
    config.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
    config.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
    config.set("hadoop.security.authentication", "kerberos");
    UserGroupInformation.setConfiguration(config);
    RangerPolicyMigrationTool rangerPolicyMigrationTool = new RangerPolicyMigrationTool();
    rangerPolicyMigrationTool.listFiles(uri, config);
    bufferedWriter.close();
    fileWriter.close();
  }

  private void listFiles(String uri, Configuration config) throws IOException {
    FileSystem fs = FileSystem.get(URI.create(uri), config);
    FileStatus statuses[] = fs.listStatus(new Path(uri));

    for (FileStatus status : statuses) {
      if (!status.isDirectory()) {
        continue;
      }
      String userPermission = getPermission(status.getPermission().getUserAction());
      String groupPermission = getPermission(status.getPermission().getGroupAction());
      String otherPermission = getPermission(status.getPermission().getOtherAction());
      if (userPermission != null) {
        bufferedWriter.write("\"" + getPath(status.getPath()) + "\"|\""
            + status.getOwner() + "\"||\"" + userPermission + "\"" + newLine);
      }
      if (groupPermission != null && groupPermission.equals(otherPermission)) {
        bufferedWriter.write("\"" + getPath(status.getPath()) + "\"||\""
            + status.getGroup() + ", public"+ "\"|\"" + groupPermission + "\"" + newLine);
      } else {
        if (groupPermission != null) {
          bufferedWriter.write("\"" + getPath(status.getPath()) + "\"||\""
              + status.getGroup() + "\"|\"" + groupPermission + "\"" + newLine);
        }
        if (otherPermission != null) {
          bufferedWriter.write("\"" + getPath(status.getPath())
              + "\"||\"public\"|\"" + otherPermission + "\"" + newLine);
        }
      }
      if(status.isDirectory()) {
        listFiles(status.getPath().toString(), config);
      }
    }
  }

  private String getPath(Path path) {
    String pathName = path.toString();
    return pathName.substring(pathName.indexOf(domain) + domain.length());
  }

  private String getPermission(FsAction userAction) {
    String permission;
    switch(userAction.toString()) {
      case "ALL":
        permission = "read, write, execute";
        break;
      case "READ":
        permission = "read";
        break;
      case "WRITE":
        permission = "write";
        break;
      case "EXECUTE":
        permission = "execute";
        break;
      case "READ_WRITE":
        permission = "read, write";
        break;
      case "READ_EXECUTE":
        permission = "read, execute";
        break;
      case "WRITE_EXECUTE":
        permission = "write, execute";
        break;
      case "NONE":
        permission = null;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + userAction);
    }
    return permission;
  }
}
