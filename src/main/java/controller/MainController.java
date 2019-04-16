package controller;

import controller.command.*;
import model.DBManager;
import model.configuration.ConnectionManager;
import view.View;

import java.sql.Connection;

public class MainController {
    private View view;
    private Command[] commands;
    private ConnectionManager connectionManager;
    private Connection connection;
    private DBManager dbManager;

    public MainController(View view, DBManager dbManager) {
        this.view = view;
        this.dbManager = dbManager;
        this.commands = new Command[12];
        createCommandBlock();
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
        connectionManager = new ConnectionManager();
        try {
            connection = connectionManager.getConnection();
        } catch (Exception e) {
            view.write("Файл конфигурации не загружен!");
        }
        if (connection == null) {
            view.write("Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                    "connect|database|user|password");
        } else {
            view.write("Введи команду или 'help' для помощи:");
        }

        while (true) {
            String input = view.read();
            if (input == null) {
                new Exit(view).process(input);
            }
            try {
                if (connection != null) {
                    dbManager.setConnection(connectionManager);
                }
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

    private void createCommandBlock() {
        commands[0] = new Connect(dbManager, view);
        commands[1] = new Help(view);
        commands[2] = new Exit(view);
        commands[3] = new IsConnected(dbManager, view);
        commands[4] = new Tables(dbManager, view);
        commands[5] = new Find(dbManager, view);
        commands[6] = new CreateTable(dbManager, view);
        commands[7] = new Insert(dbManager, view);
        commands[8] = new Update(dbManager, view);
        commands[9] = new Drop(dbManager, view);
        commands[10] = new Clear(dbManager, view);
        commands[11] = new Unsupported(view);
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




