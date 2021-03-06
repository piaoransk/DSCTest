/**
 * Copyright (c) (2010-2013),Deep Sky Century and/or its affiliates.All rights
 * reserved.
 * DSC PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
package com.dsc.db;

import java.sql.ResultSet;

/**
 * The Interface Table.
 *
 * @Author alex
 * @CreateTime Aug 4, 2016 5:54:15 PM
 * @Version 1.0
 * @Since 1.0
 */
public interface Table
{

	/**
	 * Char typed columns.
	 *
	 * @return the columns
	 */
	Columns charTypedColumns();

	/**
	 * Column.
	 *
	 * @param column
	 *            the column
	 * @return the column
	 */
	Column column(String column);

	/**
	 * Columns.
	 *
	 * @return the columns
	 */
	Columns columns();

	/**
	 * Columns.
	 *
	 * @param columns
	 *            the columns
	 * @return the columns
	 */
	Columns columns(String... columns);

	/**
	 * Columns except.
	 *
	 * @param columns
	 *            the columns
	 * @return the columns
	 */
	Columns columnsExcept(String... columns);

	/**
	 * Delete by.
	 *
	 * @param sql
	 *            the sql
	 * @return the count of deleted row
	 */
	int deleteBy(String sql);

	/**
	 * Delete by.
	 *
	 * @param column
	 *            the column
	 * @param cellValue
	 *            the cell value
	 * @return the count of deleted row
	 */
	int deleteBy(String column, Object cellValue);

	/**
	 * Delete by id.
	 *
	 * @param id
	 *            the id
	 * @return the count of deleted row
	 */
	int deleteById(Object id);

	/**
	 * Ensure schema retrieved.
	 */
	void ensureSchemaRetrieved();

	/**
	 * Existed by.
	 *
	 * @param query
	 *            the query
	 * @return true, if successful
	 */
	boolean existedBy(String query);

	/**
	 * Existed by.
	 *
	 * @param column
	 *            the column
	 * @param cellValue
	 *            the cell value
	 * @return true, if successful
	 */
	boolean existedBy(String column, Object cellValue);

	/**
	 * Existed by id.
	 *
	 * @param id
	 *            the id
	 * @return true, if successful
	 */
	boolean existedById(Object id);

	/**
	 * Checks for reference columns.
	 *
	 * @return the columns
	 */
	Columns hasReferenceColumns();

	/**
	 * Insert.
	 *
	 * @param sql
	 *            the sql
	 * @param values
	 *            the values
	 */
	void insert(String sql, Object[][] values);

	/**
	 * Name.
	 *
	 * @return the string
	 */
	String name();

	/**
	 * Primary key.
	 *
	 * @return primary key
	 */
	String primaryKey();

	/**
	 * set primary key.
	 *
	 * @param primaryKey
	 *            the primary key
	 * @return the table
	 */
	Table primaryKey(String primaryKey);

	/**
	 * Select by.
	 *
	 * @param sql
	 *            the sql
	 * @return the result set
	 */
	ResultSet selectBy(String sql);

	/**
	 * Select by.
	 *
	 * @param column
	 *            the column
	 * @param cellValue
	 *            the cell value
	 * @return the result set
	 */
	ResultSet selectBy(String column, Object cellValue);

	/**
	 * Select by id.
	 *
	 * @param id
	 *            the id
	 * @return the result set
	 */
	ResultSet selectById(Object id);
}