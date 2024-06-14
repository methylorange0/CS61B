package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author dyc
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException { // @source IntelliJ's help
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArgs(args, 1);
                Repository.initRepo();
                break;
            case "add":
                validateNumArgs(args, 2);
                Repository.addFile(args[1]);
                break;
            case "commit":
                validateNumArgs(args,2);
                Repository.makeCommit(args[1]);
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
