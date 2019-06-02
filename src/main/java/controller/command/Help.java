package controller.command;

import view.View;

import java.util.ArrayList;

public class Help implements Command {
    private View view;
    private ArrayList<Command> commands;

    public Help(View view, ArrayList<Command> commands) {
        this.view = view;
        this.commands = commands;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("Существующие команды:");
        for (Command c : commands) {
            if (c.describeCommand() != null) {
                view.write("\t" + c.formatCommand());
                view.write("\t\t" + c.describeCommand());
                view.write("");
            }
        }
    }
}
