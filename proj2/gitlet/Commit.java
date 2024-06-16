package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *
 *  does at a high level.
 *
 *  @author dyc
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The time of this Commit. */
    private Date time;
    /** The parent of this Commit. */
    private String parent;
    /** The merge Commit has two parents. */
    private String parent2;
    /** The blobs of this Commit. */
    private HashMap<String, String> blobs;


    /** Initial commit. */
    public Commit() {
        message = "initial commit";
        time = new Date(0); //@source https://www.runoob.com/java/java-date-time.html
        parent = null;
        parent2 = null;
        blobs = new HashMap<>();
    }
    /** Normal commit. */
    public Commit(String msg, Commit prev) {
        message = msg;
        time = new Date();
        parent = sha1(serialize(prev));
        parent2 = null;
        blobs = new HashMap<>();
        if (!prev.blobs.isEmpty()) {
            blobs.putAll(prev.blobs);
        }
    }

    /** Save this commit. */
    public void save() {
        Repository.storeCommit(this, hash());
    }

    /** Return the Hash of this commit. */
    public String hash() {
        return sha1(serialize(this));
    }

    /** Return the message of this commit. */
    public String getMessage() {
        return message;
    }

    /** Add a record to the commit blobs from the staging area. */
    public void addBlobRecord(String name, String hash) {
        if (blobs.containsKey(name)) {
            blobs.replace(name, hash);
        } else {
            blobs.put(name, hash);
        }
    }
    /** Delete a record of the commit's blobs. */
    public void deleteBlobRecord(String name) {
        if (blobs.containsKey(name)) {
            String hash = blobs.get(name);
            blobs.remove(name, hash);
        }
    }

    /** Restore a file of the given version. */
    public void restoreFile(File workDir, String fileName) {
        if (!workDir.isDirectory()) {
            System.out.println("It's not a working directory!");
            System.exit(0);
        }
        for (String name : blobs.keySet()) {  //@source https://www.runoob.com/java/java-hashmap.html
            if (name.equals(fileName)) {
                File workFile = join(workDir, name);
                if (!workFile.exists()) {
                    try {
                        workFile.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                String hash = blobs.get(name);
                writeContents(workFile, readContents(Repository.findBlobs(hash)));
                return;
            }
        }
        System.out.println("File does not exist in that commit.");
    }

    /** Restore the whole version */
    public void restoreVersion(File workDir) {
        if (!workDir.isDirectory()) {
            System.out.println("It's not a working directory!");
            System.exit(0);
        }
        for (String name : blobs.keySet()) {  //@source https://www.runoob.com/java/java-hashmap.html
            File workFile = join(workDir, name);
            if (!workFile.exists()) {
                try {
                    workFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            String hash = blobs.get(name);
            writeContents(workFile, readContents(Repository.findBlobs(hash)));
        }
    }


    /** Return true if commit contains this file. */
    public Boolean isContainFile(String name) {
        return blobs.containsKey(name);
    }
    /** Return hash of file in the commit. */
    public String fileHash(String name) {
        return blobs.get(name);
    }

    /** Return parent commit hash. */
    public String prev() {
        return parent;
    }

    /** Print out this commit. */
    public void printout(String hash) {
        System.out.println("===");
        System.out.println("commit " + hash);
        if (parent2 != null) {
            System.out.println("Merged " + parent.substring(0, 7) + " " + parent2.substring(0, 7));
        }
        TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00"); //@source https://tongyi.aliyun.com/qianwen/
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyy Z");
        sdf.setTimeZone(timeZone);
        System.out.println("Date: " + sdf.format(time));
        System.out.println(message);
        System.out.println();

    }

}
