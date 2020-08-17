package com.weds.core.db;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;

public class RowComparator implements Comparator {
    private int[][] sortType;
    private int[] columnType;

    public RowComparator(int[][] sortType, int[] columnType) {
        this.sortType = sortType;
        this.columnType = columnType;
    }

    public int compare(Object objA, Object objB) {
        int result = 0;
        for (int[] aSortType : sortType) {
            Object obj1 = ((ArrayList) objA).get(aSortType[0]);
            Object obj2 = ((ArrayList) objB).get(aSortType[0]);
            switch (this.columnType[aSortType[0]]) {
                case 0:
                    result = ((String) obj1).compareTo((String) obj2);
                    break;
                case 1:
                    result = ((String) obj1).compareTo((String) obj2);
                    break;
                case 2:
                    result = ((Integer) obj1).compareTo((Integer) obj2);
                    break;
                case 3:
                    result = ((Double) obj1).compareTo((Double) obj2);
                    break;
                case 4:
                    result = ((Float) obj1).compareTo((Float) obj2);
                    break;
                case 5:
                    result = ((Long) obj1).compareTo((Long) obj2);
                    break;
                case 6:
                    result = ((Byte) obj1).compareTo((Byte) obj2);
                    break;
                case 7:
                    result = ((Short) obj1).compareTo((Short) obj2);
                    break;
                case 8:
                    result = ((BigInteger) obj1).compareTo((BigInteger) obj2);
                    break;
                case 9:
                    result = ((BigDecimal) obj1).compareTo((BigDecimal) obj2);
                    break;
                default:
                    break;
            }
            if (result == 0) {
                continue;
            }
            if (aSortType[1] == 1) {
                return -result;
            } else {
                return result;
            }
        }
        return result;
    }
}
