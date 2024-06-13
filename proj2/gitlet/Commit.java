package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
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
        blobs = (HashMap<String, String>) prev.blobs.clone();
    }

    /** Save this commit. */
    public void saveCommit() {
        String hashcode = commitHash();
        File subDir = join(Repository.OBJECT_DIR, hashcode.substring(0, 2));
        File commitFile = join(subDir, hashcode.substring(2));
        if (!subDir.exists()) {
            subDir.mkdir();
        }
        try {
            commitFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e); // @source IntelliJ's help
        }
        writeObject(commitFile, this);
    }

    /** Return the Hash of this commit. */
    public String commitHash() {
        return sha1(serialize(this));
    }
}
