package com.test.scheduler.row.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.test.scheduler.model.TokenValues;

public class SchedulerRowMapper implements RowMapper<TokenValues> {

	@Override
	public TokenValues mapRow(ResultSet rs, int arg1) throws SQLException {

		TokenValues tokenValue = new TokenValues();
		tokenValue.setTokenId(rs.getInt("1"));
		tokenValue.setTokenName(rs.getString("2"));
		return tokenValue;
	}

}
