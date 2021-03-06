package with_prototype.easy_lucene.config;

import com.cncell.bean.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.store.Directory;
import with_prototype.easy_lucene.bean.DocBook;
import with_prototype.easy_lucene.constants.ExceptionEnum;
import with_prototype.easy_lucene.exception.EasyLuceneException;
import with_prototype.easy_lucene.tools.ConfigTools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 系统配置对象
 */
public final class Config {
    //TODO 这个类不需要单例，因为都使用静态变量常量，所以就是单例，不过都是final方法，意味着不能重新加载,暂时也没想好要改成什么样
    /**
     * 运行模式，内存、文件系统
     */
    private static final String init_param_type;
    /**
     * 文件系统索引存放路径
     */
    private static final String init_param_fs_index_path;
    /**
     * Bean转换映射,格式 key:BusBean,value:DocBean
     */
    private static final Map<Class, Class> busbean_lucenebean_mapping = new HashMap<Class, Class>();
    /**
     * BusBean的Id字段配置，对于多属性标志主键的ID，规定统一用"|"分隔主键属性，拼接城字符串，存入Value
     */
    private static final Map<Class, String> doc_type_mapping = new HashMap<Class, String>();
    /**
     * BusBean的ID配置，用于转换过程中对DocBean的docid进行赋值
     */
    private static final Map<Class, String[]> bean_id_mapping = new HashMap<Class, String[]>();
    /**
     * 需要分词字段Bean-属性名映射,<DocBeanClass,属性名称>
     */
    private static final Map<Class, String[]> need_segment_mapping = new HashMap<Class, String[]>();
    /**
     * 日期类型转换配置
     */
    private static final Map<Class, String[]> date_transfer_mapping = new HashMap<Class, String[]>();
    /**
     * 数字类型转换配置
     */
    private static final Map<Class, String[]> number_transfer_mapping = new HashMap<Class, String[]>();
    /**
     * 将多个beanid组合为docid，使用的分隔符
     */
    private static final String beanIdSeperator = "_";
    /**
     * 数组属性存入Lucene转换的分隔符
     */
    private static final String arrayPropTransSeprator = "_";
    /**
     * 日期范围检索字段分隔符
     */
    private static final String rangeYearConnector = "-";
    /**
     * 年份范围搜索配置，格式Map<Class, String[][]>，<BusBeanClass,{{第一组配置：范围输入字段1，范围搜索字段1},{第二组配置：范围输入字段2，范围搜索字段2}}></>支持格式：1990-2000 | 1985+  | 2000-
     */
    private static final Map<Class, String[][]> range_year_mapping = new HashMap<Class, String[][]>();
    /**
     * 默认分词器
     */
    private static final Class defaultAnalyzerClass = CJKAnalyzer.class;
    private static final Analyzer defaultAnalyzerr;

    /**
     * 默认索引
     */
    private static final Directory defaultDirectory;

    public static String getBeanIdSeperator() {
        return beanIdSeperator;
    }

    public static String getArrayPropTransSeprator() {
        return arrayPropTransSeprator;
    }

    public static String getInit_param_type() {
        return init_param_type;
    }

    public static String getInit_param_fs_index_path() {
        return init_param_fs_index_path;
    }

    public static Map<Class, Class> getTrans_bean_mapping() {
        return busbean_lucenebean_mapping;
    }

    public static Map<Class, String[]> getNeed_segment_mapping() {
        return need_segment_mapping;
    }

    public static Map<Class, String[]> getBean_id_mapping() {
        return bean_id_mapping;
    }

    public static Map<Class, String> getDoc_type_mapping() {
        return doc_type_mapping;
    }

    public static Class getDefaultAnalyzerClass() {
        return defaultAnalyzerClass;
    }

    public static Analyzer getDefaultAnalyzerr() {
        return defaultAnalyzerr;
    }

    public static Directory getDefaultDirectory() {
        return defaultDirectory;
    }

    public static Map<Class, String[]> getDate_transfer_mapping() {
        return date_transfer_mapping;
    }

    public static Map<Class, String[]> getNumber_transfer_mapping() {
        return number_transfer_mapping;
    }

    public static Map<Class, String[][]> getRange_year_mapping() { return range_year_mapping; }

    public static String getRangeYearConnector() {
        return rangeYearConnector;
    }

    static {
        Properties properties = new Properties();
        try {
            properties.load(Config.class.getResourceAsStream("/lucene_config.properties"));
        } catch (IOException e) {
            throw new EasyLuceneException(ExceptionEnum.LOAD_CONFIG_EXCEPTION.getCode(), e.getCause());
        }
        init_param_type = properties.getProperty("init_param_type");
        init_param_fs_index_path = properties.getProperty("init_param_fs_index_path");
        //Bean转换映射,格式 key:BusBean,value:DocBean
        busbean_lucenebean_mapping.put(Book.class, DocBook.class);
        //DocBean非String类型在进出Lucene的转换映射<<Class,String>,Class>，<DocBean的字段名称, DocBean特定字段的Class>>
        //添加BusBean的ID配置
        //TODO 有一些配置错误，程序运行会如何报错，需要考虑，比如id配置成了Id，自省过程可能出现空指针异常
        bean_id_mapping.put(Book.class, new String[]{"id"});
        //分词属性-Bean的映射配置<DocBeanClass,属性名称>
        need_segment_mapping.put(DocBook.class,new String[]{"describs"});
        defaultAnalyzerr = ConfigTools.getDefaultAnalyzer();
        defaultDirectory = ConfigTools.getDefaultFSDirectory();
        //BusBean的Id字段配置，在存入、更新、删除Lucene之前，将docId、docType进行赋值，docType通过调用方法传入
        doc_type_mapping.put(Book.class, "id");
        //日期类型转换配置
        date_transfer_mapping.put(Book.class,new String[]{"printdate"});
        //数字类型转换配置
        number_transfer_mapping.put(Book.class, new String[]{"pages","width","height"});
        //年份范围搜索配置，支持格式：1990-2000 | 1985+  | 2000-
        range_year_mapping.put(Book.class, new String[][]{{"rangedate","printdate"}});
    }

    public static Map<Class, Class> getBusbean_lucenebean_mapping() {
        return busbean_lucenebean_mapping;
    }

    private Config() {
    }

}
