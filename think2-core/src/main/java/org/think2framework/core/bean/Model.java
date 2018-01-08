//package org.think2framework.core.bean;
//
//import org.think2framework.core.orm.datasource.Field;
//import org.think2framework.core.orm.datasource.Redis;
//
//import java.util.List;
//import java.util.Map;
//
//public class Model {
//
//    private String name;
//
//    private String table;
//
//    private String pk;
//
//    private Boolean autoIncrement;
//
//    private List<String> uniques;
//
//    private List<String> indexes;
//
//    private String comment;
//
//    private Map<String, Field> fields;
//
//    private List<Join> joins;
//
//    private List<Filter> filters;
//
//    private List<String> groups;
//
//    private List<Order> orders;
//
//    private Redis redis;
//
//    private Database datasource;
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getTable() {
//        return table;
//    }
//
//    public void setTable(String table) {
//        this.table = table;
//    }
//
//    public String getPk() {
//        return pk;
//    }
//
//    public void setPk(String pk) {
//        this.pk = pk;
//    }
//
//    public Boolean getAutoIncrement() {
//        return autoIncrement;
//    }
//
//    public void setAutoIncrement(Boolean autoIncrement) {
//        this.autoIncrement = autoIncrement;
//    }
//
//    public List<String> getUniques() {
//        return uniques;
//    }
//
//    public void setUniques(List<String> uniques) {
//        this.uniques = uniques;
//    }
//
//    public List<String> getIndexes() {
//        return indexes;
//    }
//
//    public void setIndexes(List<String> indexes) {
//        this.indexes = indexes;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
//
//    public Map<String, Field> getFields() {
//        return fields;
//    }
//
//    public void setFields(Map<String, Field> fields) {
//        this.fields = fields;
//    }
//
//    public List<Join> getJoins() {
//        return joins;
//    }
//
//    public void setJoins(List<Join> joins) {
//        this.joins = joins;
//    }
//
//    public List<Filter> getFilters() {
//        return filters;
//    }
//
//    public void setFilters(List<Filter> filters) {
//        this.filters = filters;
//    }
//
//    public List<String> getGroups() {
//        return groups;
//    }
//
//    public void setGroups(List<String> groups) {
//        this.groups = groups;
//    }
//
//    public List<Order> getOrders() {
//        return orders;
//    }
//
//    public void setOrders(List<Order> orders) {
//        this.orders = orders;
//    }
//
//    public Redis getRedis() {
//        return redis;
//    }
//
//    public void setRedis(Redis redis) {
//        this.redis = redis;
//    }
//
//    public Database getDatabase() {
//        return datasource;
//    }
//
//    public void setDatabase(Database datasource) {
//        this.datasource = datasource;
//    }
//
//    public Query createQuery() {
//        return new Query(table, pk, fields, filters, groups, orders, joins, redis, datasource);
//    }
//
//    public Writer createWriter() {
//        return new Writer(table, pk, autoIncrement, uniques, indexes, comment, fields, datasource);
//    }
//}
