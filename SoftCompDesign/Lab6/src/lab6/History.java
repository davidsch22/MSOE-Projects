/*
 * Course:     SE 2811
 * Term:       Winter 2018-19
 * Assignment: Lab 6: Commanding Calculators
 * Author:     Dr. Hasker and David Schulz
 * Date:       6 February 2020
 */
package lab6;

import java.util.Stack;

/**
 * Handles storing 2 stacks, one for done commands and one for undone commands, and
 * performs the undoing and redoing of the commands in each stack.
 */
public class History {
    // done_cmds:   those commands that are currently executed
    // undone_cmds: commands which were undone
    protected Stack<CalculatorCommand> done_cmds = new Stack<>(), undone_cmds = new Stack<>();

    // returns null if no command to undo, the command otherwise
    public CalculatorCommand nextToUndo() {
        if (done_cmds.isEmpty())
            return null;
        else
            return done_cmds.peek();
    }

    // returns null if no command to redo, the command otherwise
    public CalculatorCommand nextToRedo() {
        if (undone_cmds.isEmpty())
            return null;
        else
            return undone_cmds.peek();
    }

    // execute command and store it on the done commands list;
    //   old undone commands are permanently deleted
    public void doCommand(CalculatorCommand new_cmd) {
        new_cmd.execute();
        undone_cmds.clear();
        done_cmds.push(new_cmd);
    }

    // undoes last executed command; precondition: at least one command to undo
    public void undo() {
        CalculatorCommand to_undo = done_cmds.pop();
        to_undo.unexecute();
        undone_cmds.push(to_undo);
    }

    // redoes last undone command; precondition: at least one command to redo
    public void redo() {
        CalculatorCommand todo = undone_cmds.pop();
        todo.execute();
        done_cmds.push(todo);
    }
}
