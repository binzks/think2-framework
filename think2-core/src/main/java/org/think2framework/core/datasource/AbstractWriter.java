package org.think2framework.core.datasource;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.think2framework.core.bean.SqlObject;

/**
 * 抽象写入生成器
 */
public abstract class AbstractWriter implements Writer {

	private static final Logger logger = LogManager.getLogger(AbstractWriter.class); // log4j日志对象

	protected String table; // 主表名称

	protected String pk; // 主键名称

	protected Boolean autoIncrement; // 是否自增长

	private JdbcTemplate jdbcTemplate; // spring jdbcTemplate

	public AbstractWriter(String table, String pk, Boolean autoIncrement, JdbcTemplate jdbcTemplate) {
		this.table = table;
		this.pk = pk;
		this.autoIncrement = autoIncrement;
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 生成实体对应的表创建sql
	 *
	 * @return 创建sql
	 */
	public abstract String createSql();

	/**
	 * 根据待新增的对象生成新增sql和值，如果是map类型特殊处理
	 *
	 * @param instance
	 *            对象数据
	 * @param id
	 *            如果不是自增长，则是guid，否则为null
	 * @return 新增sql和值
	 */
	public abstract SqlObject createInsert(Object instance, Object id);

	/**
	 * 根据待修改的数据生成修改sql和值，如果是map类型特殊处理
	 *
	 * @param instance
	 *            对象数据
	 * @param keys
	 *            作为修改条件的关键字段
	 * @return 修改sql和值
	 */
	public abstract SqlObject createUpdate(Object instance, String... keys);

	@Override
	public boolean create() {
		Boolean result = false;
		try {
			jdbcTemplate.execute(String.format("SELECT 1 FROM %s WHERE 1=2", table));
		} catch (Exception e) {
			if (null != e.getCause()) {
				String msg = e.getCause().getMessage();
				if (msg.contains("no such table") || (msg.contains("Table") && msg.contains("doesn't exist"))) {
					jdbcTemplate.execute(createSql());
					result = true;
				} else {
					throw new RuntimeException(e);
				}
			} else {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	@Override
	public Object insert(Object instance) {
		Object id = null;
		if (autoIncrement) {
			id = UUID.randomUUID().toString();
		}
		SqlObject sqlObject = createInsert(instance, id);
		return insert(sqlObject.getSql(), autoIncrement, sqlObject.getValues());
	}

	@Override
	public <T> int[] insert(List<T> list) {
		String sql = null;
		List<Object[]> batchArgs = new ArrayList<>();
		for (Object object : list) {
			Object id = null;
			if (autoIncrement) {
				id = UUID.randomUUID().toString();
			}
			SqlObject sqlObject = createInsert(object, id);
			if (StringUtils.isBlank(sql)) {
				sql = sqlObject.getSql();
			}
			batchArgs.addAll(Collections.singleton(sqlObject.getValues().toArray()));
		}
		return batchUpdate(sql, batchArgs);
	}

	@Override
	public int update(Object instance, String... keys) {
		SqlObject sqlObject = createUpdate(instance, keys);
		return update(sqlObject.getSql(), sqlObject.getValues());
	}

	@Override
	public <T> int[] update(List<T> list, String... keys) {
		String sql = null;
		List<Object[]> batchArgs = new ArrayList<>();
		for (Object object : list) {
			SqlObject sqlObject = createUpdate(object, keys);
			if (StringUtils.isBlank(sql)) {
				sql = sqlObject.getSql();
			}
			batchArgs.addAll(Collections.singleton(sqlObject.getValues().toArray()));
		}
		return batchUpdate(sql, batchArgs);
	}

	@Override
	public Object save(Object instance, String... keys) {
		if (update(instance, keys) == 1) {
			return -1;
		} else {
			return insert(instance);
		}
	}

	@Override
	public <T> String[] save(List<T> list, String... keys) {
		String[] result = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = save(list.get(i), keys).toString();
		}
		return result;
	}

	@Override
	public int delete(Object id) {
		return delete(pk, id);
	}

	@Override
	public Object insert(String sql, Boolean autoIncrement, Object... args) {
		logger.debug("Insert sql: {} values: {} autoIncrement: {}", sql, args, autoIncrement);
		if (autoIncrement) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i + 1, ClassUtils.getDatabaseValue(args[i]));
				}
				return ps;
			}, keyHolder);
			return keyHolder.getKey().intValue();
		} else {
			jdbcTemplate.update(sql, args);
			return "";
		}
	}

	@Override
	public int update(String sql, Object... args) {
		logger.debug("Update sql: {} values: {}", sql, args);
		return jdbcTemplate.update(sql, args);
	}

	@Override
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		logger.debug("batchUpdate sql: {} values: {}", sql, batchArgs);
		return jdbcTemplate.batchUpdate(sql, batchArgs);
	}
}
