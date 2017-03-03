package othertest;

import with_prototype.easy_lucene.bean.BaseBean;
import with_prototype.easy_lucene.bean.DocBook;

/**
 * Created by chenzhaolei on 2017/2/28.
 */
public class TestInstance {
    public static void main(String[] args){
        DocBook docBook = new DocBook();
        if(docBook instanceof BaseBean){
            System.out.println("docBook instanceof BaseBean");
        }
    }
}
