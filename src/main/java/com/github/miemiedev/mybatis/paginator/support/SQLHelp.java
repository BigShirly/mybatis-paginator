/*
 * Copyright (c) 2012-2013, Poplar Yfyang 杨友峰 (poplar1123@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.miemiedev.mybatis.paginator.support;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author poplar.yfyang
 * @author miemiedev
 */
public class SQLHelp {

	/**
	 * 查询总纪录数
	 *
	 * @param countSql             SQL语句
	 * @param mappedStatement mapped
	 * @param parameterObject 参数
	 * @param boundSql        boundSql
	 * @return 总记录数
	 * @throws java.sql.SQLException sql查询错误
	 */
	public static int getCount(final String countSql,
							   final MappedStatement mappedStatement, final Object parameterObject,
							   final BoundSql boundSql) throws SQLException {

        Connection connection = null;
		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try {
            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
			countStmt = connection.prepareStatement(countSql);
			final BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
					boundSql.getParameterMappings(), parameterObject);

            DefaultParameterHandler handler = new DefaultParameterHandler(mappedStatement,parameterObject,countBS);
            handler.setParameters(countStmt);

			rs = countStmt.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			return count;
		} finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } finally {
                try {
                    if (countStmt != null) {
                        countStmt.close();
                    }
                } finally {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                }
            }
		}
	}

}
