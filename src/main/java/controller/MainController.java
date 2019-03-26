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
                new Find(dbManager, view)};
    }

    public void run() {
        connectToDB();
        view.write("Введи команду или 'help' для помощи:");
        while (true) {
            String command = view.read();
            String[] commandWithParam = command.split("[|]");

            if (commands[0].canProcess(command)) {
                commands[0].process(command);
            }if (commands[1].canProcess(command)) {
                commands[1].process(command);
            }if (commands[2].canProcess(command)) {
                commands[2].process(command);
            }if (commands[3].canProcess(command)) {
                commands[3].process(command);
            }

            switch (commandWithParam[0]) {
                case "createTable": doCreateTable(commandWithParam); break;
                case "drop": doDrop(commandWithParam); break;
                case "clear": doClear(commandWithParam); break;
                default: System.out.println("Команды '" + command + "' не существует!"); break;
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



//    private void doTables() {
//        view.write(dbManager.getTables().toString());
//    }

    private void doCreateTable(String[] commandWithParam) {
        try {
            //commandWithParam содержит: 1-й аргумент - команда создать таблицу,
            // 2-й название таблицы, 3-й и последующие - названия столбцов
            //проверяем есть ли хотя-бы название таблицы
            if (commandWithParam.length > 2) {
                String nameTable = commandWithParam[1];
                //создаем массив названий столбцов (короче на 2 параметра - минус команда и название табл
                String[] nameColumns = new String[commandWithParam.length-2];
                for (int i = 2, j = 0; i < commandWithParam.length; i++) {
                    nameColumns[j++] = commandWithParam[i];
                }
                dbManager.createTable(nameTable, nameColumns);
            } else {
                throw new IllegalArgumentException("Количество параметров не соответствует шаблону!");
            }
        } catch (Exception e) {
            printError(e);
        }
    }

    private void doDrop(String[] commandWithParam) {
        try {
            if (commandWithParam.length == 2) {
                dbManager.drop(commandWithParam[1]);
            } else {
                throw new IllegalArgumentException("Количество параметров не соответствует шаблону!");
            }
        } catch (Exception e) {
            printError(e);
        }
    }

    private void doClear(String[] commandWithParam) {
        try {
            if (commandWithParam.length == 2) {
                dbManager.clear(commandWithParam[1]);
            } else {
                throw new IllegalArgumentException("Количество параметров не соответствует шаблону!");
            }
        } catch (Exception e) {
            printError(e);
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
