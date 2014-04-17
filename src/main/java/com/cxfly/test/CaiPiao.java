package com.cxfly.test;

public class CaiPiao {
    public static void main(String[] args) {
        String[] datas = generateData();
        System.out.println(datas.length);
        int length = datas.length;
        for (int i = 0; i < 100; i++) {
            int idx = Math.round(length - 1);
            System.out.println(idx);
            System.out.println(datas[idx]);
        }
    }

    private static String[] generateData() {
        final String split = "\t";
        int count = 0;
        String[] datas = new String[17721088];

        for (int a = 1; a <= 33 - 5; a++) {
            for (int b = a + 1; b <= 33 - 4; b++) {
                for (int c = b + 1; c <= 33 - 3; c++) {
                    for (int d = c + 1; d <= 33 - 2; d++) {
                        for (int e = d + 1; e <= 33 - 1; e++) {
                            for (int f = e + 1; f <= 33; f++) {
                                for (int l = 1; l <= 16; l++) {

                                    datas[count++] = a + split + b + split + c + split + d + split
                                            + e + split + f + split + l;
                                }
                            }
                        }
                    }
                }
            }
        }
        return datas;
    }

}
