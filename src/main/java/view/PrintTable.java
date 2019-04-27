package view;

import model.DataSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PrintTable {
    private View view;

    public PrintTable(View view) {
        this.view = view;
    }

    //вывод всей таблицы
    public void printTable(ArrayList<Integer> arrWidthAttribute, int tableSize, Set<String> attributes, List<DataSet> dataSets) {
        //рисуем верхнюю границу таблицы(+--+--+)
        printLineTable(arrWidthAttribute);

        //рисуем заглавие таблицы
        printTitleTable(arrWidthAttribute, attributes);

        //рисуем нижнюю границу заглавия таблицы (+--+--+)
        printLineTable(arrWidthAttribute);

        //выводим содержимое кортежей таблицы
        dataCortege(arrWidthAttribute, tableSize, attributes, dataSets);

        //рисуем нижнюю границу всей таблицы (+--+--+)
        printLineTable(arrWidthAttribute);
    }

    //рисуем верхнюю/нижнюю границу таблицы (+--+--+)
    public void printLineTable(ArrayList<Integer> arrWidthAttribute) {
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
    public void printTitleTable(ArrayList<Integer> arrWidthAttribute, Set<String> attributes) {
        String str = "+";
        int count = 0;
        for (String stringIterator : attributes) {
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
    private void dataCortege(ArrayList<Integer> arrWidthAttribute, int tableSize, Set<String> attributes, List<DataSet> dataSets) {
        for (int j = 0; j < tableSize; j++) {
            String str = "+";
            Object valueData = new Object();
            Iterator<String> iteratorAttributes = attributes.iterator();
            for (int i = 0; i < arrWidthAttribute.size(); i++) {
                if (dataSets.size() != 0 && iteratorAttributes.hasNext()) {
                    valueData = (dataSets.get(j).get(iteratorAttributes.next()));
                }
                str += valueData;
                //ширина колонки
                int lengthColumn = arrWidthAttribute.get(i);
                int countSpace = 0;//кол-во пробелов
                //если значение в таблице не null
                if (valueData != null) {
                    //кол-во пробелов
                    countSpace = lengthColumn - valueData.toString().length();
                } else {//4 - длинна слова 'null'
                    countSpace = lengthColumn - 4;
                }
                //если в ячейке булевое значение (занимает 1 позицию)- кол-во пробелов изменяем
                Iterator<String> iterator = attributes.iterator();
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
