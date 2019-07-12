package controller.command;

import model.DBManager;
import view.View;

public class DropDatabase implements Command {
    private View view;
    private DBManager dbManager;
    private static final int LENGTH_PARAM = 2;

    public DropDatabase(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("dropDB|");
    }

    @Override
    public void process(String command) {
        String[] commandWithParam = command.split("[|]");
        if (commandWithParam.length != LENGTH_PARAM) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
        String databaseName = commandWithParam[1];
        dbManager.dropDatabase(databaseName);
        view.write(String.format("База данных '%s' успешно удалена.", databaseName));
    }

    @Override
    public String formatCommand() {
        return "dropDB|databaseName";
    }

    @Override
    public String describeCommand() {
        return "Удаление базы данных 'databaseName'. Перед удалением закрыть все соединения!";
    }
}
