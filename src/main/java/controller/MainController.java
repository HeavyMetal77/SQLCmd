package controller;

import controller.command.*;
import model.DBManager;
import view.View;

public class MainController {
    private View view;
    private DBManager dbManager;
    private Command[] commands;

    public MainController(View view, DBManager dbManager) {
        this.view = view;
        this.dbManager = dbManager;
        this.commands = new Command[]{new Exit(view), new Help(view), new Tables(dbManager, view),
                new Find(dbManager, view), new CreateTable(dbManager, view), new Drop(dbManager, view),
                new Clear(dbManager, view), new Unsupported(view)};
    }

    public void run() {
        connectToDB();
        while (true) {
            view.write("Введи команду или 'help' для помощи:");
            String input = view.read();
            for (Command command: commands) {
                if (command.canProcess(input)) {
                    command.process(input);
                    break;
                }
            }
        }
    }

    private void connectToDB() {
        view.write("Привет пользователь!");
        view.write("Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "database|user|password");

        while (true) {
            try {
                String string = view.read();
                String[] data = string.split("[|]");
                if (data.length != 3) {
                    throw new IllegalArgumentException ("Неверное количество параметров: ожидается 3, введено " + data.length);
                }
                String database = data[0];
                String user = data[1];
                String password = data[2];

                dbManager.connect(database, user, password);
                view.write("Подключение к базе выполнено успешно!");
                break;
            } catch (Exception e) {
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
