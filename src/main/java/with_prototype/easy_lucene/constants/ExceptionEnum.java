package with_prototype.easy_lucene.constants;

public enum ExceptionEnum {
    LOAD_CONFIG_EXCEPTION("ERROR_CODE_00001","加载配置失败")
    ,ILLEGAL_BEAN_ARGUMENT_EXCEPTION("ERROR_CODE_00002","无效Bean参数")
    ,ILLEGAL_TRANSTYPE_ARGUMENT_EXCEPTION("ERROR_CODE_00003","无效transType参数")
    ,ILLEGAL_DOCTYPE_ARGUMENT_EXCEPTION("ERROR_CODE_00004","docType为空")
    ,ILLEGAL_BEAN_MAPPING_EXCEPTION("ERROR_CODE_00005","无效BeanMapping,beanClass:")
    ,TRANS_NONE_MAPPING_BEAN_EXCEPTION("ERROR_CODE_00006","无法转换未配置映射的Bean:")
    ,ILLEGAL_BUSBEANCLASS_ARGUMENT_EXCEPTION("ERROR_CODE_00007","参数为空:busBeanClass")
    ,ILLEGAL_BUSBEANARGUMENT_EXCEPTION("ERROR_CODE_00008","参数为空:busBean")
    ,NONE_CONFIG_BUSBEAN_ID_MAPPING_EXCEPTION("ERROR_CODE_00009","BusBean未配置ID映射")
    ,NULL_DOCBEAN_ID_EXCEPTION("ERROR_CODE_00010","docBean中docId为空")
    ,BEAN_FIELD_ALL_NULL_EXCEPTION("ERROR_CODE_00011","查询条件的Bean，属性均为空")
    ,SCAN_BUSBEAN_ID_NULL_EXCEPTION("ERROR_CODE_00012","BusBeanId检测为空（删除index）");

    private String code;
    private String message;

    ExceptionEnum(String code, String message) {
        setCode(code);
        setMessage(message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAllInfo() {
        return code + ":" + message;
    }
}
