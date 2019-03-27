package controller.command;

import model.DBManager;
import view.View;

public class CreateTable implements Command {
    private DBManager dbManager;
    private View view;

    public CreateTable(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("createTable|");
    }

    @Override
    public void process(String command) {
        String[] commandWithParam = command.split("[|]");
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

    private void printError(Exception e) {
        String massage = e.getMessage();
        if (e.getCause() != null) {
            massage += " " + e.getCause().getMessage();
        }
        view.write("Ошибка! Причина: " + massage);
        view.write("Повтори попытку!");
    }
}
