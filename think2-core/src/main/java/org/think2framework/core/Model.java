package org.think2framework.core;

import org.think2framework.core.bean.Column;
import org.think2framework.core.bean.Filter;
import org.think2framework.core.bean.Join;
import org.think2framework.core.bean.Order;
import org.think2framework.core.orm.Query;
import org.think2framework.core.orm.Writer;
import org.think2framework.core.orm.database.Database;
import org.think2framework.core.orm.database.Redis;

import java.util.List;
import java.util.Map;

public class Model {

    private String table;

    private String pk;

    private Boolean autoIncrement;

    private List<String> uniques;

    private List<String> indexes;

    private String comment;

    private Map<String, Column> columns;

    private List<Join> joins;

    private List<Filter> filters;

    private List<String> groups;

    private List<Order> orders;

    private Redis redis;

    private Database database;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public Boolean getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public List<String> getUniques() {
        return uniques;
    }

    public void setUniques(List<String> uniques) {
        this.uniques = uniques;
    }

    public List<String> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<String> indexes) {
        this.indexes = indexes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Map<String, Column> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, Column> columns) {
        this.columns = columns;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public void setJoins(List<Join> joins) {
        this.joins = joins;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Redis getRedis() {
        return redis;
    }

    public void setRedis(Redis redis) {
        this.redis = redis;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Query createQuery() {
        return new Query(table, pk, columns, filters, groups, orders, joins, redis, database);
    }

    public Writer createWriter() {
        return new Writer(table, pk, autoIncrement, uniques, indexes, comment, columns, database);
    }
}
