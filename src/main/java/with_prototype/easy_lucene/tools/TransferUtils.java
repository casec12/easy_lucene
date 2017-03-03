package with_prototype.easy_lucene.tools;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import with_prototype.easy_lucene.config.Config;
import with_prototype.easy_lucene.config.Constants;
import with_prototype.easy_lucene.exception.EasyLuceneException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static with_prototype.easy_lucene.constants.ExceptionEnum.*;

public class TransferUtils {

    /**
     * 从业务Bean转换为LuceneBean
     */
    public static <E, T> E toDocBean(T srcBean, String docType) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        return transform(srcBean, Constants.TRANS_TO_DOC_BEAN, docType);
    }

    /**
     * 从业务LuceneBean转换为Bean
     */
    public static <E, T> E toBusBean(T srcBean) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        return transform(srcBean, Constants.TRANS_TO_COMMON_BEAN);
    }

    //docType可变长参数，当被toDocBean调用，会使用，当被toBusBean调用不使用。使用时只用数组的第一个参数
    private static <E, T> E transform(T srcBean, int transType, String... docType) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        E destBean = null;
        Map<Class, Class> beanMapping = Config.getTrans_bean_mapping();

        if (srcBean == null)
            throw new IllegalArgumentException(ILLEGAL_BEAN_ARGUMENT_EXCEPTION.getAllInfo());
        if (transType != Constants.TRANS_TO_COMMON_BEAN && transType != Constants.TRANS_TO_DOC_BEAN)
            throw new IllegalArgumentException(ILLEGAL_TRANSTYPE_ARGUMENT_EXCEPTION.getAllInfo());
        if (transType == Constants.TRANS_TO_DOC_BEAN && docType == null)
            throw new IllegalArgumentException(ILLEGAL_DOCTYPE_ARGUMENT_EXCEPTION.getAllInfo());

        for (Map.Entry<Class, Class> entry : beanMapping.entrySet()) {
            Class srcBeanClass = null;
            Class destBeanClass = null;
            if (transType == Constants.TRANS_TO_DOC_BEAN) {
                srcBeanClass = entry.getKey();
                destBeanClass = entry.getValue();
            } else {
                srcBeanClass = entry.getValue();
                destBeanClass = entry.getKey();
            }

            if (srcBeanClass == null || destBeanClass == null)
                throw new EasyLuceneException(ILLEGAL_BEAN_MAPPING_EXCEPTION.getAllInfo() + srcBeanClass + "," + destBeanClass);

            if (srcBeanClass.equals(srcBean.getClass())) {
                Object destBeanInstance = destBeanClass.newInstance();
                BeanUtils.copyProperties(destBeanInstance, srcBean);
                destBean = (E) destBeanInstance;
                if (transType == Constants.TRANS_TO_DOC_BEAN) {
                    PropertyUtils.getPropertyDescriptor(destBean, Constants.DOC_TYPE_FIELD).getWriteMethod().invoke(destBean, docType[0]);
                    PropertyUtils.getPropertyDescriptor(destBean, Constants.DOC_ID_FIELD).getWriteMethod().invoke(destBean, transBeanIdsToDocId(srcBeanClass, srcBean));
                }
                break;
            }
        }

        if (destBean == null) {
            throw new EasyLuceneException(TRANS_NONE_MAPPING_BEAN_EXCEPTION.getAllInfo() + srcBean.getClass().getCanonicalName());
        }

        return destBean;
    }

    /**
     * 转换BusBean的Id字段为合并的DocId字段的过程
     */
    private static String transBeanIdsToDocId(Class busBeanClass, Object busBean) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (busBeanClass == null)
            throw new IllegalArgumentException(ILLEGAL_BUSBEANCLASS_ARGUMENT_EXCEPTION.getAllInfo());
        if (busBean == null)
            throw new IllegalArgumentException(ILLEGAL_BUSBEANARGUMENT_EXCEPTION.getAllInfo());

        StringBuffer docId = new StringBuffer();

        Map<Class, String[]> mapping = Config.getBean_id_mapping();
        for (Map.Entry<Class, String[]> entry : mapping.entrySet()) {
            Class class_ = entry.getKey();
            String[] value_ = entry.getValue();
            if (busBeanClass.equals(class_)) {
                for (String idProp : value_) {
                    String bean_id = (String) PropertyUtils.getPropertyDescriptor(busBean, idProp).getReadMethod().invoke(busBean);
                    docId.append(bean_id);
                    docId.append(Config.getBeanIdSeperator());
                }
                docId.deleteCharAt(docId.length() - 1);
                break;
            }
        }

        if (docId.toString().equals(""))
            throw new EasyLuceneException(NONE_CONFIG_BUSBEAN_ID_MAPPING_EXCEPTION.getAllInfo());

        return docId.toString();
    }

    /**
     * 供删除索引时使用，检查所有BusBeanId都不能为空
     */
    public static void checkBusBeanIdNotNull(Object busBean) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (busBean == null)
            throw new IllegalArgumentException(ILLEGAL_BEAN_ARGUMENT_EXCEPTION.getAllInfo());

        Map<Class, Class> beanMapping = Config.getTrans_bean_mapping();
        Map<Class, String[]> beanIdMapping = Config.getBean_id_mapping();
        boolean mappingExists = false;
        boolean idMappingExists = false;

        for (Map.Entry<Class, Class> entry : beanMapping.entrySet()) {
            Class busBeanClass = entry.getKey();
            Class docBeanClass = entry.getValue();

            if (busBeanClass == null || docBeanClass == null)
                throw new EasyLuceneException(ILLEGAL_BEAN_MAPPING_EXCEPTION.getAllInfo() + busBeanClass + "," + docBeanClass);

            if (busBeanClass.equals(busBean.getClass())) {
                mappingExists = true;
                for(Map.Entry<Class, String[]> KV :beanIdMapping.entrySet()){
                    Class beanClass = KV.getKey();
                    String[] beanIds = KV.getValue();
                    if(busBeanClass.equals(beanClass)){
                        idMappingExists = true;
                        for(String idFieldName : beanIds){
                            Object idField = PropertyUtils.getPropertyDescriptor(busBean,idFieldName).getReadMethod().invoke(busBean);
                            if(idField == null)
                                throw new EasyLuceneException(SCAN_BUSBEAN_ID_NULL_EXCEPTION.getAllInfo());
                        }
                    }
                }
                break;
            }
        }

        if (mappingExists == false) {
            throw new EasyLuceneException(ILLEGAL_BEAN_MAPPING_EXCEPTION.getAllInfo());
        }

        if (idMappingExists == false) {
            throw new EasyLuceneException(NONE_CONFIG_BUSBEAN_ID_MAPPING_EXCEPTION.getAllInfo());
        }

    }

}
