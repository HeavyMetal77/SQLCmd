package controller.command;

import model.DBManager;
import view.View;

public class Drop implements Command {
    private DBManager dbManager;
    private View view;

    public Drop(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("drop|");
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
            dbManager.drop(nameTable);
            view.write("TABLE " + nameTable + " was successfully deleted!");
        } catch (Exception e) {
            throw new RuntimeException(String.format("Ошибка удаления таблицы %s, по причине: %s", nameTable, e.getMessage()));
        }
    }

    @Override
    public String formatCommand() {
        return "drop|tableName";
    }

    @Override
    public String describeCommand() {
        return "Удалить таблицу 'tableName'";
    }
}
