package controller;

import controller.command.*;
import model.DBManager;
import view.View;

public class MainController {
    private View view;
    private Command[] commands;

    public MainController(View view, DBManager dbManager) {
        this.view = view;
        this.commands = new Command[]{
                new Connect(dbManager, view),
                new Help(view),
                new Exit(view),
                new IsConnected(dbManager, view),
                new Tables(dbManager, view),
                new Find(dbManager, view),
                new CreateTable(dbManager, view),
                new Insert(dbManager, view),
                new Drop(dbManager, view),
                new Clear(dbManager, view),
                new Unsupported(view)};
    }

    public void run() {
        try {
            doWork();
        } catch (ExitException e) {
            //do nothing
        }
    }

    private void doWork() {
        view.write("Привет пользователь!");
        view.write("Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password");
        while (true) {
            String input = view.read();
            if (input == null) {
                new Exit(view).process(input);
                break;
            }
            try {
                for (Command command : commands) {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                }
                view.write("Введи команду или 'help' для помощи:");
            } catch (Exception e) {
                if (e instanceof ExitException) {
                    throw new ExitException();
                }
                printError(e);
            }
        }
    }
    private void printError(Exception e) {
        String massage = e.getMessage();
        if (e.getCause() != null) {
            massage += " " + e.getCause().getMessage();
        }
        view.write("Ошибка! Причина: " + massage);
        view.write("Повтори попытку!");
    }
}




