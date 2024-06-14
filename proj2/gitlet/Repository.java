package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static gitlet.Utils.*;



/** Represents a gitlet repository.
 *  In this repository, commits and blobs are both in the object folder,
 *  the pointers are in the refs folder, and
 *  does at a high level.
 *
 *  @author dyc
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory.
     * .gitlet
     *    |- object
     *    |     |- ...
     *    |- refs
     *    |    |- heads
     *    |         |- branches ... (One of them is called master.)
     *    |- area
     *    |    |- staging files ...
     *    |- HEAD   (indicate the current commit)
     *    |- TABLE  (the set of delete)
     *    */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The object directory: contains all the commits and bolbs. */
    public static final File OBJECT_DIR = join(GITLET_DIR, "object");
    /** The refs directory: contains all the pointers. */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    /** The heads directory: sub dir of refs, and it stores all the heads of each branch. */
    public static final File HEADS_DIR = join(REFS_DIR, "heads");
    /** The area directory: contains staging files and ChangeTable file */
    public static final File AREA_DIR = join(GITLET_DIR, "area");
    /** The Table file record the list of delete */
    public static final File TABLE = join(GITLET_DIR, "Table");
    /** The HEAD file record the current working commit. */
    public static final File HEAD = join(GITLET_DIR, "HEAD");


    /** Init the repository. */
    public static void initRepo() throws IOException { // @source IntelliJ's help
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        // Create all the dirs and files.
        GITLET_DIR.mkdir();
        OBJECT_DIR.mkdir();
        REFS_DIR.mkdir();
        HEADS_DIR.mkdir();
        AREA_DIR.mkdir();
        TABLE.createNewFile();
        HEAD.createNewFile();

        // Init the table.
        clearTable();

        // Save this init commit object.
        Commit initCommit = new Commit();
        initCommit.saveCommit();

        // Change the pointer and HEAD.
        File master = join(HEADS_DIR, "master");
        master.createNewFile();
        writeContents(master, initCommit.commitHash());
        writeContents(HEAD,"master");
    }

    /** -----------------------------------------------------------------------------------------
     * helper method
     */

    /** The object directory has special structure,
     *
     * use this method to find the file.
     */
    public static File findObject(String hash) {
        File subdir = join(OBJECT_DIR, hash.substring(0,2));
        File result = join(subdir, hash.substring(2));
        return result;
    }
    /** use this method to store an object. */
    public static void storeObject(Serializable storeObj) throws IOException {
        String hash = sha1(serialize(storeObj));
        File subDir = join(Repository.OBJECT_DIR, hash.substring(0, 2));
        File commitFile = join(subDir, hash.substring(2));
        if (!subDir.exists()) {
            subDir.mkdir();
        }
        commitFile.createNewFile();
        writeObject(commitFile, storeObj);
    }

    /** Return the current commit object. */
    public static Commit currentCommit() {
        String head = readContentsAsString(HEAD);
        String commitHash = readContentsAsString(join(HEADS_DIR, head));
        return readObject(findObject(commitHash), Commit.class);
    }

    /** Return the delete list (aka Table). */
    public static ArrayList<String> deleteList() {
        return readObject(TABLE, ArrayList.class);
    }

    /** Clear the delete list (aka Table). */
    private static void clearTable() {
        ArrayList<String> emptyTable = new ArrayList<>();
        writeObject(TABLE, emptyTable);
    }

    /** -----------------------------------------------------------------------------------------
     * main part
     */

    /** Add file to the staging area. */
    public static void addFile(String name) throws IOException {
        File theFile = join(CWD,name);
        // Failure cases
        if (!theFile.exists()) {
            System.out.println("File does not exist");
            System.exit(0);
        }

        String fileHash = sha1(serialize(theFile));
        File areaFile = join(AREA_DIR, name);
        Commit currentCommit = currentCommit();

        // if the current working file == the current commit file, delete areaFile if it exists,
        if (currentCommit.blobs.containsKey(name) && currentCommit.blobs.get(name).equals(fileHash)) {
            restrictedDelete(areaFile);
            return;
        }

        // write the areaFile.
        if (!areaFile.exists()) {
            areaFile.createNewFile();
        }
        //Files.copy(theFile.toPath(), areaFile.toPath());
        writeObject(areaFile, theFile);
    }

    /** Make a commit. */
    public static void makeCommit (String msg) throws IOException {
        Commit prevCommit = currentCommit();
        Commit theCommit = new Commit(msg, prevCommit);

        List<String> addNames = plainFilenamesIn(AREA_DIR);
        List<String> deleteList = deleteList();

        // add or update tracking file
        for (int i = 0; i < addNames.size(); i++) {
            String name = addNames.get(i);
            storeObject(join(AREA_DIR, name));
            theCommit.addBlobRecord(name);
            restrictedDelete(join(AREA_DIR, name));
        }
        // delete tracking
        for (int i = 0; i < deleteList.size(); i++) {
            String name = deleteList.get(i);
            theCommit.deleteBlobRecord(name);
        }
        clearTable();
    }

}
