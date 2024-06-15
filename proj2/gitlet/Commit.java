package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
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
    public HashMap<String, String> blobs;

    /** Print out this commit. */
    public void printout() {
        System.out.println("===");
        System.out.println("commit " + hash());
        if (parent2 != null) {
            System.out.println("Merged " + parent.substring(0, 7) + " " + parent2.substring(0, 7));
        }
        TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyy Z"); //@source https://tongyi.aliyun.com/qianwen/
        sdf.setTimeZone(timeZone);
        System.out.println("Date: " + sdf.format(time));
        System.out.println(message);
        System.out.println();

    }

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
    public void save() throws IOException {
        Repository.storeObject(this, hash());
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
    /** Return true if commit contain this file. */
    public Boolean containsFile(String name) {
        return blobs.containsKey(name);
    }
    /** Return hash of file in the commit. */
    public String fileHash(String name) {
        return blobs.get(name);
    }

    /** Return parent commit. */
    public Commit prev() {
        if (parent != null) {
            return readObject(Repository.findCommits(parent), Commit.class);
        }
        return null;
    }

}
