package with_prototype.easy_lucene.tools;

import org.apache.commons.beanutils.PropertyUtils;
import with_prototype.easy_lucene.config.Config;
import with_prototype.easy_lucene.config.Constants;
import with_prototype.easy_lucene.exception.EasyLuceneException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static with_prototype.easy_lucene.constants.ExceptionEnum.*;

public class TransferUtils {

    private static final String datePattern="yyyyMMdd";

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
            Class busBeanClass = null;
            Class docBeanClass = null;
            if (transType == Constants.TRANS_TO_DOC_BEAN) {
                srcBeanClass = entry.getKey();
                destBeanClass = entry.getValue();
                busBeanClass = entry.getKey();
                docBeanClass =  entry.getValue();
            } else {
                srcBeanClass = entry.getValue();
                destBeanClass = entry.getKey();
                busBeanClass = entry.getKey();
                docBeanClass = entry.getValue();
            }

            if (srcBeanClass == null || destBeanClass == null)
                throw new EasyLuceneException(ILLEGAL_BEAN_MAPPING_EXCEPTION.getAllInfo() + srcBeanClass + "," + destBeanClass);

            if (srcBeanClass.equals(srcBean.getClass())) {
                Object destBeanInstance = destBeanClass.newInstance();
                //这里是需要处理的字段Descriptor数组
                PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(docBeanClass.newInstance());
                out:
                for(PropertyDescriptor propertyDescriptor :propertyDescriptors){
                    String propertyName = propertyDescriptor.getName();
                    if(propertyName.equals(Constants.DOC_ID_FIELD) || propertyName.equals(Constants.DOC_TYPE_FIELD) || propertyName.equals("class"))
                        continue;

                    //数字类型mapping的处理
                    for(Map.Entry<Class,String[]> entry1 : Config.getNumber_transfer_mapping().entrySet()){
                        if(!busBeanClass.equals(entry1.getKey()))
                            continue;
                        if(!Arrays.asList(entry1.getValue()).contains(propertyName))
                            continue;
                        //转Docbean的处理
//                        Class propertyType = PropertyUtils.getPropertyDescriptor(busBeanClass,propertyName).getPropertyType();
                        Class propertyType = PropertyUtils.getPropertyType(busBeanClass.newInstance(),propertyName);
                        if(transType == Constants.TRANS_TO_DOC_BEAN){
                            if(Integer.class.equals(propertyType) || int.class.equals(propertyType)){
                                //TODO ,添加转换，并对document赋值
                                Object propertyValue = PropertyUtils.getProperty(srcBean,propertyName);
                                //如果为空也continue out;
                                if(propertyValue==null)
                                    continue out;
                                else if(new Integer(0).equals(propertyValue))
                                    continue out;
                                else
                                    PropertyUtils.setProperty(destBeanInstance,propertyName,propertyValue.toString());

                                continue out;
                            }else if(Double.class.equals(propertyType) || double.class.equals(propertyType)){
                                //TODO ,添加转换，并对document赋值
                                Object propertyValue = PropertyUtils.getProperty(srcBean,propertyName);
                                //如果为空也continue out;
                                if(propertyValue==null)
                                    continue out;
                                else if(new Double(0).equals(propertyValue))
                                    continue out;
                                else
                                    PropertyUtils.setProperty(destBeanInstance,propertyName,propertyValue.toString());

                                continue out;
                            }else if (Float.class.equals(propertyType) || float.class.equals(propertyType)){
                                //TODO ,添加转换，并对document赋值
                                Object propertyValue = PropertyUtils.getProperty(srcBean,propertyName);
                                //如果为空也continue out;
                                if(propertyValue==null)
                                    continue out;
                                else if(new Float(0).equals(propertyValue))
                                    continue out;
                                else
                                    PropertyUtils.setProperty(destBeanInstance,propertyName,propertyValue.toString());

                                continue out;
                            }
                        }else{//转Busbean的处理
                            if(Integer.class.equals(propertyType) || int.class.equals(propertyType)){
                                //TODO ,添加转换，并对document赋值
                                //如果为空也continue out;
                                Object propertyValue = PropertyUtils.getProperty(srcBean,propertyName);
                                if(propertyValue == null)
                                    continue out;
                                else
                                    PropertyUtils.setProperty(destBeanInstance,propertyName,Integer.parseInt(propertyValue.toString()));

                                continue out;
                            }else if(Double.class.equals(propertyType) || double.class.equals(propertyType)){
                                //TODO ,添加转换，并对document赋值
                                //如果为空也continue out;
                                Object propertyValue = PropertyUtils.getProperty(srcBean,propertyName);
                                if(propertyValue == null)
                                    continue out;
                                else
                                    PropertyUtils.setProperty(destBeanInstance,propertyName,Double.parseDouble(propertyValue.toString()));

                                continue out;
                            }else if (Float.class.equals(propertyType) || float.class.equals(propertyType)){
                                //TODO ,添加转换，并对document赋值
                                //如果为空也continue out;
                                Object propertyValue = PropertyUtils.getProperty(srcBean,propertyName);
                                if(propertyValue == null)
                                    continue out;
                                else
                                    PropertyUtils.setProperty(destBeanInstance,propertyName,Float.parseFloat(propertyValue.toString()));

                                continue out;
                            }
                        }
                    }
                    //日期类型mapping的处理
                    for(Map.Entry<Class,String[]> entry2 : Config.getDate_transfer_mapping().entrySet()){
                        if(!busBeanClass.equals(entry2.getKey()))
                            continue;
                        if(!Arrays.asList(entry2.getValue()).contains(propertyName))
                            continue;
                        //转Docbean的处理
                        if(transType == Constants.TRANS_TO_DOC_BEAN){
                            //TODO ,添加转换，并对document赋值
                            //如果为空也continue out;
                            Object propertyValue = PropertyUtils.getProperty(srcBean,propertyName);
                            if(propertyValue == null)
                                continue out;
                            else
                                PropertyUtils.setProperty(destBeanInstance,propertyName,transDate2String(propertyValue));

                            continue out;
                        }else{//转Busbean的处理
                            //TODO ,添加转换，并对document赋值
                            //如果为空也continue out;
                            Object propertyValue = PropertyUtils.getProperty(srcBean,propertyName);
                            if(propertyValue == null)
                                continue out;
                            else
                                PropertyUtils.setProperty(destBeanInstance,propertyName,transString2Date(propertyValue));

                            continue out;
                        }
                    }
                    //普通类型（字符串、字符串数组）的处理
                    PropertyUtils.setSimpleProperty(destBeanInstance,propertyName,PropertyUtils.getSimpleProperty(srcBean,propertyName));
                }
//oldcode
//                BeanUtils.copyProperties(destBeanInstance, srcBean);
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

    /**
     * 日期转字符串
     */
    private static String transDate2String(Object date){
        if(!(date instanceof Date))
            throw new EasyLuceneException(ILLEGAL_ARGUMENT_DATE_DATE_TYPE_EXCEPTION.getAllInfo());

        if(date==null)
            throw new EasyLuceneException(SYSTEM_INNER_ERROR.getAllInfo());


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        return simpleDateFormat.format(date);
    }

    /**
     * 字符串转日期
     */
    private static Date transString2Date(Object date) {
        if(!(date instanceof String))
            throw new EasyLuceneException(ILLEGAL_ARGUMENT_DATE_STRING_TYPE_EXCEPTION.getAllInfo());

        if(date==null){
            throw new EasyLuceneException(SYSTEM_INNER_ERROR.getAllInfo());
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Date date_ = null;
        try{
            date_ = simpleDateFormat.parse(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new EasyLuceneException(e.getMessage(),e.getCause());
        }
        return date_;
    }
}
