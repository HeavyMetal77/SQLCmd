package controller.command;

import model.DBManager;
import view.View;

public class Connect implements Command {
    private DBManager dbManager;
    private View view;

    public Connect(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {
        String[] commandWithParam = command.split("[|]");
        int countCommandParameters = formatCommand().split("\\|").length;
        if (commandWithParam.length != countCommandParameters) {
            view.write(String.format("Неверное количество параметров: " +
                    "ожидается: %s, введено: %s", countCommandParameters, commandWithParam.length));
            return;
        }
        String database = commandWithParam[1];
        String user = commandWithParam[2];
        String password = commandWithParam[3];
        try {
            dbManager.connect(database, user, password);
            view.write("Подключение к базе выполнено успешно!");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка подключения к базе данных!");
        }
    }

    @Override
    public String formatCommand() {
        return "connect|database|user|password";
    }

    @Override
    public String describeCommand() {
        return "Соединение с базой данных";
    }
}
