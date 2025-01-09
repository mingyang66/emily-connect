package com.emily.connect.datasource.type;

import com.emily.connect.core.db.DbModelItem;
import com.emily.connect.core.db.JdbcType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description :  大整数数处理器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/10 3:41 PM
 */
public class BigIntegerTypeHandler implements TypeHandler {
    @Override
    public DbModelItem getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        DbModelItem item = new DbModelItem();
        item.valueType = JdbcType.BigInteger;
        item.name = rs.getMetaData().getColumnName(columnIndex);
        item.value = rs.getString(columnIndex);
        return item;
    }
}
