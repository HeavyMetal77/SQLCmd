package controller.command;

import model.DBManager;
import view.View;

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
        int countCommandParameters = formatCommand().split("\\|").length;
        if (commandWithParam.length != countCommandParameters) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
        String nameTable = commandWithParam[1];
        dbManager.clear(nameTable);
        view.write("Таблица " + nameTable + " была успешно очищена!");
    }

    @Override
    public String formatCommand() {
        return "clear|tableName";
    }

    @Override
    public String describeCommand() {
        return "Очистка содержимого таблицы 'tableName'";
    }
}
