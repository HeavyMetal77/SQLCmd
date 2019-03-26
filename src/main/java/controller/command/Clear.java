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
        try {
            if (commandWithParam.length == 2) {
                dbManager.clear(commandWithParam[1]);
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
