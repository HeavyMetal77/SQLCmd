package controller.command;

import model.DBManager;
import model.DataSet;
import view.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Find implements Command {
    private DBManager dbManager;
    private View view;

    public Find(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
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
            //список с размерами (шириной) каждого атрибута таблицы
            ArrayList<Integer> arrWidthAttribute = dbManager.getWidthAtribute(nameTable);
            //количество кортежей таблицы //TODO потом посмотреть - возможно достаточно датасетов
            int tableSize = dbManager.getSize(nameTable);
            //массив датасетов таблицы
            List<DataSet> dataSets = dbManager.getDataSetTable(nameTable);
            //массив атрибутов (названий колонок) таблицы
            Set<String> atributes = dbManager.getAtribute(nameTable);
            //вывод всей таблицы
            printTable(nameTable, arrWidthAttribute, tableSize, atributes, dataSets);
        } catch (SQLException e) {
            view.write(String.format(e.getMessage()));
        }
    }

    //вывод всей таблицы
    private void printTable(String nameTable, ArrayList<Integer> arrWidthAttribute, int tableSize, Set<String> atributes, List<DataSet> dataSets) throws SQLException {
        //рисуем верхнюю границу таблицы(+--+--+)
        printLineTable(nameTable, arrWidthAttribute);

        //рисуем заглавие таблицы
        printTitleTable(nameTable, arrWidthAttribute, atributes);

        //рисуем нижнюю границу заглавия таблицы (+--+--+)
        printLineTable(nameTable, arrWidthAttribute);

        //выводим содержимое кортежей таблицы
        dataCortage(nameTable, arrWidthAttribute, tableSize, atributes, dataSets);

        //рисуем нижнюю границу всей таблицы (+--+--+)
        printLineTable(nameTable, arrWidthAttribute);
    }

    //рисуем верхнюю/нижнюю границу таблицы (+--+--+)
    private void printLineTable(String nameTable, ArrayList<Integer> arrWidthAttribute) throws SQLException {
        String str = "+";
        for (int i = 0; i < arrWidthAttribute.size(); i++) {
            //ширина колонки
            int lengthColumn = arrWidthAttribute.get(i);
            if (lengthColumn > 0) {
                str += String.format("%0" + lengthColumn + "d", 0).replace("0", "-") + "+";
            }
        }
        view.write(str);
    }

    //рисуем заглавие таблицы
    private void printTitleTable(String nameTable, ArrayList<Integer> arrWidthAttribute, Set<String> atributes) throws SQLException {
        String str = "+";
        int count = 0;
        for (String stringIterator : atributes) {
            str += stringIterator;
            //ширина колонки
            int lengthColumn = arrWidthAttribute.get(count++);
            //остаток пробелов
            int countSpace = lengthColumn - stringIterator.length();
            //если кол-во пробелов больше 0
            if (countSpace > 0) {
                str += String.format("%0" + countSpace + "d", 0).replace("0", " ");
            }
            str += "+";
        }
        view.write(str);
    }

    //выводим содержимое кортежей таблицы
    private void dataCortage(String nameTable, ArrayList<Integer> arrWidthAttribute, int tableSize, Set<String> atributes, List<DataSet> dataSets) throws SQLException {
        for (int j = 0; j < tableSize; j++) {
            String str = "+";
            Object valueData = new Object();
            for (int i = 0; i < arrWidthAttribute.size(); i++) {
                String temp = "";
                if (dataSets.size() != 0) {
                    temp = "" + dataSets.get(j).getValues()[i];
                    valueData = dataSets.get(j).getValues()[i];
                }
                str += temp;
                //ширина колонки
                int lengthColumn = arrWidthAttribute.get(i);
                int countSpace;//кол-во пробелов
                //если значение в таблице не null
                if (valueData != null) {
                    //кол-во пробелов
                    countSpace = lengthColumn - valueData.toString().length();
                } else {//4 - длинна слова 'null'
                    countSpace = lengthColumn - 4;
                }
                //если в ячейке булевое значение (занимает 1 позицию)- кол-во пробелов изменяем
                Iterator<String> iterator = atributes.iterator();
                String currentIterator = "";
                for (int k = 0; k < i; k++) {
                    if (iterator.hasNext()) {
                        currentIterator = iterator.next();
                    }
                }
                if (currentIterator.equals("bool")) {
                    countSpace = lengthColumn - valueData.toString().length();
                }
                //если кол-во пробелов больше 0
                if (countSpace > 0) {
                    str += String.format("%0" + countSpace + "d", 0).replace("0", " ");
                }
                str += "+";
            }
            view.write(str);
        }
    }
}
