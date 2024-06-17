package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author dyc
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) { // @source IntelliJ's help
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                validateNumArgs(args, 1);
                Repository.initRepo();
                break;
            case "add":
                validateNumArgs(args, 2);
                Repository.addFile(args[1]);
                break;
            case "commit":
                validateNumArgs(args, 2);
                Repository.makeCommit(args[1]);
                break;
            case "rm":
                validateNumArgs(args, 2);
                Repository.removeFile(args[1]);
                break;
            case "log":
                validateNumArgs(args, 1);
                Repository.printLog();
                break;
            case "global-log":
                validateNumArgs(args, 1);
                Repository.globalPrint();
                break;
            case "find":
                validateNumArgs(args, 2);
                Repository.printIdWithGivenMessage(args[1]);
                break;
            case "status":
                validateNumArgs(args, 1);
                Repository.printRepoStatus();
                break;
            case "checkout":
                if (args.length > 1) {
                    String secondArg = args[1];
                    if (secondArg.equals("--")) {
                        validateNumArgs(args, 3);
                        Repository.restoreFileInHead(args[2]);
                        break;
                    } else if (args.length == 4 && args[2].equals("--")) {
                        Repository.restoreFileGivenVersion(args[1], args[3]);
                        break;
                    } else if (args.length == 2) {
                        Repository.restoreGivenBranch(args[1]);
                        break;
                    } else {
                        System.out.println("Incorrect operands.");
                        System.exit(0);
                    }
                }
                break;
            case "branch":
                validateNumArgs(args, 2);
                Repository.createNewBranch(args[1]);
                break;
            case "rm-branch":
                validateNumArgs(args, 2);
                Repository.deleteBranch(args[1]);
                break;
            case "reset":
                validateNumArgs(args, 2);
                Repository.resetGivenCommit(args[1]);
                break;
            default:
                System.out.println("No command with that name exists");
                System.exit(0);
        }
    }

    /** Check the number of arguments for each command.
     * @source lab6
     */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}
