package controller;

import controller.command.*;
import model.DBManager;
import model.configuration.ConnectionManager;
import view.View;

import java.sql.Connection;
import java.util.ArrayList;

public class MainController {
    private View view;
    private ArrayList<Command> commands;
    private ConnectionManager connectionManager;
    private Connection connection;
    private DBManager dbManager;

    public MainController(View view, DBManager dbManager) {
        this.view = view;
        this.dbManager = dbManager;
        this.commands = new ArrayList<>();
        createCommandList();
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
                    "\n'connect|database|user|password' \nили 'help' для получения помощи");
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

    private void createCommandList() {
        commands.add(new Connect(dbManager, view));
        commands.add(new Help(view, commands));
        commands.add(new Exit(view));
        commands.add(new IsConnected(dbManager, view));
        commands.add(new Tables(dbManager, view));
        commands.add(new Find(dbManager, view));
        commands.add(new CreateTable(dbManager, view));
        commands.add(new Insert(dbManager, view));
        commands.add(new Update(dbManager, view));
        commands.add(new Drop(dbManager, view));
        commands.add(new Clear(dbManager, view));
        commands.add(new Delete(dbManager, view));
        commands.add(new Unsupported(view));
    }

    private void printError(Exception e) {
        String massage = e.getMessage();
        if (e.getCause() != null) {
            massage += " " + e.getCause().getMessage();
        }
        view.write("Ошибка! Причина: " + massage);
        view.write("Введи команду или 'help' для помощи:");
    }
}




