package with_prototype.easy_lucene.service;

import with_prototype.easy_lucene.bean.BaseBean;
import with_prototype.easy_lucene.tools.LuceneUtils;
import with_prototype.easy_lucene.tools.TransferUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyLucene服务类
 */

public class EasyLuceneService {
    /**
     * 更新索引
     */
    public static<T,E extends BaseBean> void updateBeanIndex(T busBean,String docType){
        E docBean = null;
        try {
            docBean = TransferUtils.toDocBean(busBean, docType);
            LuceneUtils.updateBeanIndex(docBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询索引
     */
    public static<T,E extends BaseBean> List<T> searchBean(T busBean,String docType){
        BaseBean docBean = null;
        List<T> list = new ArrayList<T>();
        try {
            docBean = TransferUtils.toDocBean(busBean, docType);
            System.out.println("显示查询结果：");
            List<E> searchedDocBookList = LuceneUtils.searchBean(docBean);
            for(E docBeanElement : searchedDocBookList) {
                list.add(TransferUtils.toBusBean(docBeanElement));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 删除索引
     */
    public static<T,E extends BaseBean> void deleteBeanIndex(T busBean, String docType){
        try {
            TransferUtils.checkBusBeanIdNotNull(busBean);
            E docBean = TransferUtils.toDocBean(busBean, docType);
            LuceneUtils.deleteBeanIndex(docBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
