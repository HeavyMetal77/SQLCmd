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
        if (commandWithParam.length < 3 || commandWithParam.length % 2 != 0) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
        if (!commandWithParam[1].matches("[a-zA-Z]{3,}")) {
            view.write("Таблица не может называться " + commandWithParam[1] + "!");
            view.write("Имя таблицы должно начинаться только с буквы, длинной не меньше 3 символов!");
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
            view.write("Таблица " + nameTable + " была успешно создана!");
        } catch (Exception e) {
            throw new RuntimeException(String.format("Ошибка создания таблицы %s, по причине: %s", nameTable, e.getMessage()));
        }
    }

    @Override
    public String formatCommand() {
        return "createTable|tableName|column1|column2|...|columnN";
    }

    @Override
    public String describeCommand() {
        return "Создать таблицу 'tableName' с колонками 'column1'...'columnN', " +
                "при этом автоматически создается колонка id с автоинкрементом";
    }

    private String getRequest(String nameTable, String[] nameColumns) {
        String requestSql = "CREATE TABLE IF NOT EXISTS " +
                nameTable + " (ID SERIAL PRIMARY KEY,";
        String textNameColumn = "";
        for (String text : nameColumns) {
            textNameColumn += " " + text + " TEXT NOT NULL,";
        }
        textNameColumn = textNameColumn.substring(0, (textNameColumn.length() - 1));
        requestSql += textNameColumn + ")";
        return requestSql;
    }
}