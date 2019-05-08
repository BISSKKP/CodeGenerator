package com.ACID.core.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.code.utils.StringUtils;




public class BaseExample {
	
	 /**
     * am_staff
     */
    protected String orderByClause;
    
    /**
     * 默认删除标记
     */
    protected String delFlag="0";

    public BaseExample() {
    	oredCriteria = new ArrayList<Criteria>();
	}
//    /**
//     * 默认设置del_flag =1
//     * @param delFlag
//     */
//    public BaseExample(String delFlag) {
//    	oredCriteria = new ArrayList<Criteria>();
//    	//oredCriteria.add(createCriteria().andColumnEqualTo(BaseColumns.delFlag.getValue(), delFlag==null?"0":delFlag));
//	}
    
    /**
     * am_staff
     */
    protected boolean distinct;

    /**
     * am_staff
     */
    protected List<Criteria> oredCriteria;
    
    
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }
    
    
    public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}


	protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }
        
        
        public Criteria andColumnIsNull(String column) {
            addCriterion(column+" is null");
            return (Criteria) this;
        }

        public Criteria andColumnIsNotNull(String column) {
            addCriterion(column +" is not null");
            return (Criteria) this;
        }

        public Criteria andColumnEqualTo(String column,String value) {
            addCriterion(column+" =", value, column);
            return (Criteria) this;
        }

        public Criteria andColumnNotEqualTo(String column,String value) {
            addCriterion(column+" <>", value, column);
            return (Criteria) this;
        }

        public Criteria andColumnGreaterThan(String column,String value) {
            addCriterion(column+" >", value, column);
            return (Criteria) this;
        }

        public Criteria andColumnGreaterThanOrEqualTo(String column,String value) {
            addCriterion(column+" >=", value, column);
            return (Criteria) this;
        }

        public Criteria andColumnLessThan(String column,String value) {
            addCriterion(column+" <", value, column);
            return (Criteria) this;
        }

        public Criteria andColumnLessThanOrEqualTo(String column,String value) {
            addCriterion(column+" <=", value, column);
            return (Criteria) this;
        }

        public Criteria andColumnLike(String column,String value) {
            addCriterion(column+" like", value, column);
            return (Criteria) this;
        }

        public Criteria andColumnNotLike(String column,String value) {
            addCriterion(column+" not like", value, column);
            return (Criteria) this;
        }
        public Criteria andColumnIn(String column,List<String> values) {
            addCriterion(column+" in", values, column);
            return (Criteria) this;
        }

        public Criteria andColumnNotIn(String column,List<String> values) {
            addCriterion(column+" not in", values, column);
            return (Criteria) this;
        }

        /**
         * 
         * @param column 字段会被驼峰命名一次
         * @param value1
         * @param value2
         * @return
         */
        public Criteria andDateBetween(String column, Date value1, Date value2) {
            addCriterion(column+" between", value1, value2, StringUtils.toCamelCase(column));
            return (Criteria) this;
        }

        
        public Criteria andColumnBetween(String column,String value1, String value2) {
            addCriterion(column+" between", value1, value2, StringUtils.toCamelCase(column));
            return (Criteria) this;
        }

        public Criteria andColumnNotBetween(String column,String value1, String value2) {
            addCriterion(column+" not between", value1, value2, StringUtils.toCamelCase(column));
            return (Criteria) this;
        }
        
        
    }
    

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * 不自带delFlag
     * @return
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }
    /**
     * 兼容delFlag
     * @param delFlag
     * @return
     */
    public Criteria createCriteria(String delFlag) {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
        	criteria.andColumnEqualTo(BaseColumns.delFlag.getValue(), delFlag!=null?delFlag:"0");
            oredCriteria.add(criteria);
        }
        return criteria;
    }
    /**
     * 默认del_flag=0
     * @return
     */
    public Criteria createDelFlagCriteria() {
        
        return createCriteria(delFlag);
    }
    

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }
    
    /**
     *  * am_staff
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
    
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
    
    

}
