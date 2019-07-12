package controller.command;

import model.DBManager;
import view.View;

public class Drop implements Command {
    private DBManager dbManager;
    private View view;
    private static final int LENGTH_PARAM = 2;

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
        if (commandWithParam.length != LENGTH_PARAM) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
        String nameTable = commandWithParam[1];
        dbManager.drop(nameTable);
        view.write("Таблица " + nameTable + " была успешно удалена!");
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
