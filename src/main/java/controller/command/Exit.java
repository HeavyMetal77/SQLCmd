package controller.command;

import view.View;

public class Exit implements Command {
    private View view;

    public Exit(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(String command) {
        view.write("Программа завершила работу");
        throw new ExitException();
//        System.exit(0);
    }
}
