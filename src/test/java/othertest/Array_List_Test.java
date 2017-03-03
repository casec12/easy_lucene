package othertest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhaolei on 2017/3/1.
 */
public class Array_List_Test {
    public static void main(String[] args){
        String[] arr1 = new String[2];
        String[] arr2 = new String[2];

        List<String> testList = new ArrayList();
        testList.add("1");
        String[] array = testList.toArray(arr1);
        for(String a : array){
            System.out.println(a);
        }

        System.out.println("=============");

        List<String> testList2 = new ArrayList();
        testList2.add("1");
        testList2.add("2");
        testList2.add("3");
        String[] array2 = testList2.toArray(arr2);
        for(String a : array2){
            System.out.println(a);
        }
    }
}
