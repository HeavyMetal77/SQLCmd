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
        //commandWithParam содержит: 1-й аргумент - команда создать таблицу,
        // 2-й название таблицы, 3-й и последующие - названия столбцов
        //проверяем есть ли хотя-бы название таблицы
        if (commandWithParam.length < 2) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
        String nameTable = commandWithParam[1];
        //создаем массив названий столбцов (короче на 2 параметра - минус команда и название табл
        String[] nameColumns = new String[commandWithParam.length - 2];
        for (int i = 2, j = 0; i < commandWithParam.length; i++) {
            nameColumns[j++] = commandWithParam[i];
        }
        String requestSql = getRequest(nameTable, nameColumns);
        try {
            dbManager.createTable(requestSql);
            view.write("TABLE " + nameTable + " was successfully created!");
        } catch (Exception e) {
            view.write(String.format("Ошибка создания таблицы %s, по причине: %s", nameTable, e.getMessage()));
        }
    }

    private String getRequest(String nameTable, String[] nameColumns) {
        String requestSql = "CREATE TABLE IF NOT EXISTS " +
                nameTable + " (ID INT PRIMARY KEY NOT NULL,";
        String textNameColumn = "";
        for (String text : nameColumns) {
            textNameColumn += " " + text + " TEXT NOT NULL,";
        }
        textNameColumn = textNameColumn.substring(0, (textNameColumn.length() - 1));
        requestSql += textNameColumn + ")";
        return requestSql;
    }
}
