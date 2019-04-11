package controller.command;

import model.DBManager;
import view.View;

import java.sql.SQLException;

public class Clear implements Command {
    private DBManager dbManager;
    private View view;

    public Clear(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        String[] commandWithParam = command.split("[|]");
        if (commandWithParam.length != 2) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
        String nameTable = commandWithParam[1];
        try {
            dbManager.clear(nameTable);
            view.write("TABLE " + nameTable + " was successfully clear!");
        } catch (SQLException e) {
            view.write(String.format("Ошибка очистки таблицы %s, по причине: %s", nameTable, e.getMessage()));
        }
    }
}
