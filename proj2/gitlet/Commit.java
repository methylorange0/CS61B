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
    public HashMap<String, String> blobs;

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
        if (!prev.blobs.isEmpty()) {
            blobs.putAll(prev.blobs);
        } else {
            blobs = new HashMap<>();
        }

    }

    /** Save this commit. */
    public void save() throws IOException {
        Repository.storeObject(this);
    }

    /** Return the Hash of this commit. */
    public String hash() {
        return sha1(serialize(this));
    }

    /** Add a record to the commit blobs from the staging area. */
    public void addBlobRecord(String name) {
        String hash = sha1(serialize(join(Repository.AREA_DIR, name)));
        if (blobs.containsKey(name)) {
            blobs.put(name, hash);
        } else {
            blobs.replace(name, hash);
        }
    }
    /** Delete a record of the commit's blobs. */
    public void deleteBlobRecord(String name) {
        if (blobs.containsKey(name)) {
            String hash = blobs.get(name);
            blobs.remove(name, hash);
        }
    }
}
