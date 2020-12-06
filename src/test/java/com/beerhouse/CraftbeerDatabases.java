package com.beerhouse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.jdbc.Work;

import com.beerhouse.entity.Beer;


public enum CraftbeerDatabases {

	INSTANCE;

	private SessionFactory sessionFactory;

	private CraftbeerDatabases() {
		createMasterDbHibernate();
	}

	private void createMasterDbHibernate() {
		Properties properties = new Properties();
		properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
		properties.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
		properties.put(Environment.URL, "jdbc:mysql://localhost:3306/craftbeer");
		properties.put(Environment.USER, "admin");
		properties.put(Environment.PASS, "admin");
		properties.put(Environment.SHOW_SQL, "false");
		properties.put(Environment.HBM2DDL_AUTO, "validate");
		properties.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, "true");

		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(Beer.class);
		configuration.setProperties(properties);

		sessionFactory = configuration.buildSessionFactory();
	}

	public void clean() {
		Session session = sessionFactory.openSession();
		Work clean = new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				List<String> sqls = new ArrayList<>();
				sqls.add("TRUNCATE beer;");
				for (String sql : sqls) {
					connection.prepareStatement(sql).execute();
				}
			}
		};
		session.doWork(clean);
		session.close();
	}
}
