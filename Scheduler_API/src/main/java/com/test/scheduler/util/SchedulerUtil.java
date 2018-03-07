package com.test.scheduler.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.test.scheduler.model.TokenValues;
import com.test.scheduler.row.mapper.SchedulerRowMapper;

@Component
public class SchedulerUtil {

	@Value("folder.location")
	private String folderLocation;

	@Value("folder.processed.location")
	private String processedLocation;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	@Scheduled(cron = "0 */15 * * * *")
	public void checkFolder() {

		File folder = new File(folderLocation);
		for (File f : folder.listFiles()) {
			String tokenName = f.getName();
			List<TokenValues> tokens = jdbcTemplate.query("Select * from t1 where token_name=?",
					new Object[] { tokenName }, new SchedulerRowMapper());
			try {
				String s1=tokens.get(0).getTokenName();
				int s2=tokens.get(0).getTokenId();
				RestTemplate rt = new RestTemplate();
				ResponseEntity<String> response = rt.exchange("http://localhost:8080/", HttpMethod.GET, null,
						String.class);
				if (response != null && response.getStatusCode() == HttpStatus.OK) {
					String responseValues = response.getBody();
					
					Files.move(Paths.get(f.getAbsolutePath()), Paths.get(processedLocation),
							StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
