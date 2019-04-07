package controller.command;

import model.DBManager;
import model.DataSet;
import view.View;

import java.sql.SQLException;

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
        try {
            if (commandWithParam.length == 2) {
                //имя таблицы
                String nameTable = commandWithParam[1];
                //массив с размерами (шириной) каждого атрибута таблицы
                int[] arrWidthAttribute = dbManager.getWidthAtribute(nameTable);
                //количество кортежей таблицы //TODO потом посмотреть - возможно достаточно датасетов
                int tableSize = dbManager.getSize(nameTable);
                //массив датасетов таблицы
                DataSet[] dataSets = dbManager.getDataSetTable(nameTable);

                String[] atributes = dbManager.getAtribute(nameTable);

                //вывод всей таблицы
                printTable(nameTable, arrWidthAttribute, tableSize, atributes, dataSets);

            } else {
                throw new IllegalArgumentException("Количество параметров не соответствует шаблону!");
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при работе с БД!");
        }
    }

    //вывод всей таблицы
    private void printTable(String nameTable, int[] arrWidthAttribute, int tableSize, String[] atributes, DataSet[] dataSets) throws SQLException {
        //рисуем верхнюю границу таблицы(+--+--+)
        printLineTable(nameTable, arrWidthAttribute);

        //рисуем заглавие таблицы
        printTitleTable(nameTable, arrWidthAttribute, atributes, dataSets);

        //рисуем нижнюю границу заглавия таблицы (+--+--+)
        printLineTable(nameTable, arrWidthAttribute);

        //выводим содержимое кортежей таблицы
        dataCortage(nameTable, arrWidthAttribute, tableSize, dataSets);

        //рисуем нижнюю границу всей таблицы (+--+--+)
        printLineTable(nameTable, arrWidthAttribute);
    }

    //рисуем верхнюю/нижнюю границу таблицы (+--+--+)
    private void printLineTable(String nameTable, int [] arrWidthAttribute) throws SQLException {
        String str = "+";
        for (int i = 0; i < arrWidthAttribute.length; i++) {
            //ширина колонки
            int lengthColumn = arrWidthAttribute[i];
            if (lengthColumn > 0) {
                str += String.format("%0" + lengthColumn + "d", 0).replace("0", "-") + "+";
            }
        }
        view.write(str);
    }

    //рисуем заглавие таблицы
    private void printTitleTable(String nameTable, int [] arrWidthAttribute, String[] atributes, DataSet[] dataSets) throws SQLException {
        String str = "+";
        //итерируемся по списку названий таблиц (i)
        for (int i = 0; i < arrWidthAttribute.length; i++) {
            //ширина колонки
            int lengthColumn = arrWidthAttribute[i];
            str += atributes[i];
            //остаток пробелов
            int countSpace = lengthColumn - atributes[i].length();
            //если кол-во пробелов больше 0
            if (countSpace > 0) {
                str += String.format("%0" + countSpace + "d", 0).replace("0", " ");
            }
            str += "+";
        }
        view.write(str);
    }

    //выводим содержимое кортежей таблицы
    private void dataCortage(String nameTable, int [] arrWidthAttribute, int tableSize, DataSet[] dataSets) throws SQLException {
        for (int j = 0; j < tableSize; j++) {
            String str = "+";
            int indexColumn = 0;

            for (int i = 0; i < arrWidthAttribute.length; i++) {

                str += dataSets[j].getValues()[i];
                //ширина колонки
                int lengthColumn = arrWidthAttribute[i];
                Object valueData = dataSets[j].getValues()[i];

                int countSpace;//кол-во пробелов
                //если значение в таблице не null
                if (valueData != null) {
                    //кол-во пробелов
                    countSpace = lengthColumn - valueData.toString().length();
                } else {//4 - длинна слова 'null'
                    countSpace = lengthColumn - 4;
                }
                //если в ячейке булевое значение (занимает 1 позицию)- кол-во пробелов изменяем
                if (dataSets[0].getNames()[i].equals("bool")) {
                    countSpace = valueData.toString().length() - lengthColumn;
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
