package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /** The structure of the directory.
     * .gitlet
     *    |- object
     *    |     |- commits
     *    |     |     |- ...
     *    |     |- blobs
     *    |           |- ...
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
    /** Contains commits. */
    public static final File COMMITS_DIR = join(OBJECT_DIR, "commits");
    /** Contains blobs. */
    public static final File BLOBS_DIR = join(OBJECT_DIR, "blobs");
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


    /** ---------------------------------------------------------------------------------------------------
     * main part
     */

    /** Init the repository. */
    public static void initRepo() throws IOException { // @source IntelliJ's help
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        // Create all the dirs and files.
        GITLET_DIR.mkdir();
        OBJECT_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
        REFS_DIR.mkdir();
        HEADS_DIR.mkdir();
        AREA_DIR.mkdir();
        TABLE.createNewFile();
        HEAD.createNewFile();

        // Init the table.
        clearTable();

        // Save this init commit object.
        Commit initCommit = new Commit();
        initCommit.save();

        // Change the pointer and HEAD.
        File master = join(HEADS_DIR, "master");
        master.createNewFile();
        writeContents(master, initCommit.hash());
        writeContents(HEAD, "master");
    }

    /** Add file to the staging area. */
    public static void addFile(String name) throws IOException {
        File theFile = join(CWD, name);
        // Failure cases
        if (!theFile.exists()) {
            System.out.println("File does not exist");
            System.exit(0);
        }

        String fileHash = sha1(readContents(theFile));
        File areaFile = join(AREA_DIR, name);
        Commit currentCommit = currentCommit();

        // if the current working file == the current commit file, delete areaFile if it exists,
        if (currentCommit.containsFile(name) && currentCommit.fileHash(name).equals(fileHash)) {
            areaFile.delete();
            return;
        }

        // write the areaFile.
        if (!areaFile.exists()) {
            areaFile.createNewFile();
        }
        writeContents(areaFile, readContents(theFile));
    }

    /** Make a commit. */
    public static void makeCommit(String msg) throws IOException {
        Commit prevCommit = currentCommit();
        Commit theCommit = new Commit(msg, prevCommit);

        // add or update tracking file
        List<String> addNames = plainFilenamesIn(AREA_DIR);
        for (int i = 0; i < addNames.size(); i++) {
            String name = addNames.get(i);
            File areaFile = join(AREA_DIR, name);
            String hash = sha1(readContents(areaFile));
            storeBlobs(areaFile, hash);
            theCommit.addBlobRecord(name, hash);
            areaFile.delete();
        }

        // delete tracking
        ArrayList<String> deleteList = readTable();
        for (int i = 0; i < deleteList.size(); i++) {
            String name = deleteList.get(i);
            theCommit.deleteBlobRecord(name);
        }
        clearTable();

        // save commit and update pointer
        theCommit.save();
        String head = readContentsAsString(HEAD);
        File pointer = join(HEADS_DIR, head);
        writeContents(pointer, theCommit.hash());
    }

    /** Delete tracking a file in the next commit. */
    public static void removeFile(String name) {
        Boolean changed = false;
        File areaFile = join(AREA_DIR, name);
        // If this file is in the staging area, remove the staging file.
        if (areaFile.exists()) {
            areaFile.delete();
            changed = true;
        }
        // If this file is tracked, add it to the TABLE,
        Commit theCommit = currentCommit();
        if (theCommit.containsFile(name)) {
            changed = true;
            ArrayList<String> deleteList = readTable();
            // It's a set.
            if (!deleteList.contains(name)) {
                deleteList.add(name);
            }
            writeObject(TABLE, deleteList);
            // if this file is still in the working dir, remove it.
            File theFile = join(CWD, name);
            restrictedDelete(theFile);
        }
        if (!changed) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

    /** Print out the commit log. */
    public static void printLog() {
        String head = readContentsAsString(HEAD);
        String crusorHash = readContentsAsString(join(HEADS_DIR, head));
        while (crusorHash != null) {
            Commit crusor = readObject(findCommits(crusorHash), Commit.class);
            crusor.printout(crusorHash);
            crusorHash = crusor.prev();
        }
    }

    /** Print out all the commits in the uncertain way. */
    public static void globalPrint() {
        List<String> commitNames = plainFilenamesIn(COMMITS_DIR);
        for (int i = 0; i < commitNames.size(); i++) {
            String name = commitNames.get(i);
            File thisFile = join(COMMITS_DIR, name);
            Commit thisCommit = readObject(thisFile, Commit.class);
            thisCommit.printout(name);
        }
    }

    /** Print out the ids of all commits that have the given commit message. */
    public static void printIdWithGivenMessage(String givenMsg) {
        List<String> commitNames = plainFilenamesIn(COMMITS_DIR);
        Boolean have = false;
        for (int i = 0; i < commitNames.size(); i++) {
            String name = commitNames.get(i);
            File thisFile = join(COMMITS_DIR, name);
            Commit thisCommit = readObject(thisFile, Commit.class);
            if (thisCommit.getMessage().equals(givenMsg)) {
                have = true;
                System.out.println(thisCommit.hash());
            }
        }
        if (!have) {
            System.out.println("Found no commit with that message.");
        }
    }

    /** Print out the status of this Repository. */
    public static void printRepoStatus() {
        System.out.println("=== Branches ===");
        List<String> branchNames = plainFilenamesIn(HEADS_DIR);
        String head = readContentsAsString(HEAD);
        for (int i = 0; i < branchNames.size(); i++) {
            String name = branchNames.get(i);
            if (name.equals(head)) {
                System.out.println("*" + name);
            } else {
                System.out.println(name);
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        List<String> stagingName = plainFilenamesIn(AREA_DIR);
        for (int i = 0; i < stagingName.size(); i++) {
            String name = stagingName.get(i);
            System.out.println(name);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        ArrayList<String> deleteList = readTable();
        for (int i = 0; i < deleteList.size(); i++) {
            String name = deleteList.get(i);
            System.out.println(name);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    /** Restore a file of HEAD version. */
    public static void restoreFileInHead(String fileName) throws IOException {
        Commit headCommit = currentCommit();
        headCommit.restoreFile(CWD, fileName);
    }

    /** Restore a file of GIVEN version. */
    public static void restoreFileGivenVersion(String hash, String fileName) throws IOException {
        if (hash.length() == 40) {
            File commitFile = findCommits(hash);
            if (!commitFile.exists()) {
                System.out.println("No commit with that id exists.");
                System.exit(0);
            }
            Commit givenVersion = readObject(commitFile, Commit.class);
            givenVersion.restoreFile(CWD, fileName);
            return;
        } else {
            List<String> commitNames = plainFilenamesIn(COMMITS_DIR);
            for (int i = 0; i < commitNames.size(); i++) {
                String commitName = commitNames.get(i);
                if (matchPrefixHash(hash, commitName)) {
                    Commit givenVersion = readObject(findCommits(commitName), Commit.class);
                    givenVersion.restoreFile(CWD, fileName);
                    return;
                }
            }
        }
        System.out.println("No commit with that id exists.");
        System.exit(0);
    }

    /** Helper method to match a prefix hash. */
    private static boolean matchPrefixHash(String prefix, String hash) {
        if (prefix.length() > 40) {
            return false;
        }
        for (int i = 0; i < prefix.length(); i++) {
            if (prefix.charAt(i) != hash.charAt(i)) {
                return false;
            }
        }
        return true;
    }




    /** ---------------------------------------------------------------------------------------------
     * helper method
     */

    /** The object directory has special structure,
     *
     * use this method to find the file.
     */
    public static File findCommits(String hash) {
        return join(COMMITS_DIR, hash);
    }

    public static File findBlobs(String hash) {
        return join(BLOBS_DIR, hash);
    }

    /** use this method to store a commit. */
    public static void storeCommit(Commit storeCommit, String hash) throws IOException {
        File storeFile = join(COMMITS_DIR, hash);
        if (!storeFile.exists()) {
            storeFile.createNewFile();
        }
        writeObject(storeFile, storeCommit);
    }

    /** use this method to store a file. */
    public static void storeBlobs(File storeBlobs, String hash) throws IOException {
        File storeFile = join(BLOBS_DIR, hash);
        if (!storeFile.exists()) {
            storeFile.exists();
        }
        writeContents(storeFile, readContents(storeBlobs));
    }

    /** Return the current commit object. */
    public static Commit currentCommit() {
        String head = readContentsAsString(HEAD);
        String commitHash = readContentsAsString(join(HEADS_DIR, head));
        return readObject(findCommits(commitHash), Commit.class);
    }

    /** Return the delete list (aka Table). */
    public static ArrayList<String> readTable() {
        return readObject(TABLE, ArrayList.class);
    }

    /** Clear the delete list (aka Table). */
    private static void clearTable() {
        ArrayList<String> emptyTable = new ArrayList<>();
        writeObject(TABLE, emptyTable);
    }

}
