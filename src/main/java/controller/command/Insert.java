package controller.command;

import model.DBManager;
import model.DataSet;
import view.View;

public class Insert implements Command  {


    private DBManager dbManager;
    private View view;

    public Insert(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("insert|");
    }

    @Override
    public void process(String command) {
        //получаем массив параметров команды
        String[] commandWithParam = command.split("[|]");
        //0-й элемент - непосредственно команда
        //1-й элемент - имя таблицы nameTable
        String nameTable = commandWithParam[1];
        //рассчитываем длинну массива DataSet из полученных параметров (минус 2 элемента - команда и имя таблицы)
        int lengthData = (commandWithParam.length-2)/2;
        DataSet[] dataSets = new DataSet[lengthData];
        for (int i = 0, j = 2; i < dataSets.length; i++, j+=2) {
            dataSets[i] = new DataSet(commandWithParam[j], commandWithParam[j+1]);
        }
        if (commandWithParam.length >= 4) {
            dbManager.insert(nameTable, dataSets);
        } else {
            view.write("Недостаточно параметров!");
        }
    }
}