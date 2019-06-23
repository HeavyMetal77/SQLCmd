package controller.command;

import model.DBManager;
import view.View;

public class CreateDatabase implements Command {
    private DBManager dbManager;
    private View view;

    public CreateDatabase(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("createDB|");
    }

    @Override
    public void process(String command) {
        String[] commandWithParam = command.split("[|]");
        if (commandWithParam.length != 2) {
            view.write("Количество параметров не соответствует шаблону!");
            return;
        }
        if (!commandWithParam[1].matches("[a-zA-Z]{3,}")) {
            view.write("База данных не может называться '" + commandWithParam[1] + "'!");
            view.write("Имя таблицы должно начинаться только с буквы, длинной не меньше 3 символов!");
            return;
        }
        String databaseName = commandWithParam[1];
        dbManager.createDatabase(databaseName);
        view.write(String.format("База данных '%s' успешно создана.", databaseName));
    }

    @Override
    public String formatCommand() {
        return "createDB|databaseName";
    }

    @Override
    public String describeCommand() {
        return "Создание новой базы данных (Имя базы должно начинаться с буквы).";
    }
}
