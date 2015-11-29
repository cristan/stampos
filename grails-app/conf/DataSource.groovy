dataSource {
    pooled = true
	jmxExport = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}

// A fix for Another unnamed CacheManager already exists in the same VM. See http://stackoverflow.com/a/29317238/389649
beans {
	cacheManager {
	   shared = true
   }
 }

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
	singleSession = true // configure OSIV singleSession mode
	flush.mode = 'auto' // OSIV session flush mode outside of transactional context
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
			driverClassName = "org.h2.Driver"
			username = "sa"
			password = ""
			url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
    amazondb {
        dataSource {
            dbCreate = "update"
			driverClassName = "org.postgresql.Driver"
			dialect = "org.hibernate.dialect.PostgreSQLDialect"
			username = "stampos"
			password = "h5XhdZVxFKARcNm7"
			url = "jdbc:postgresql://stampos.c90olqkwa9wi.eu-west-1.rds.amazonaws.com:5432/stampos"
			
			dialect = "org.hibernate.dialect.PostgreSQLDialect"
        }
    }
	test {
		dataSource {
			dbCreate = "update"
            url = "jdbc:h2:stampos;MVCC=TRUE;LOCK_TIMEOUT=10000"
			driverClassName = "org.h2.Driver"
			username = "sa"
			password = ""
			url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
		}
	}
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:stampos;MVCC=TRUE;LOCK_TIMEOUT=10000"
			driverClassName = "org.h2.Driver"
			username = "sa"
			password = ""
            properties {
               // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
               jmxEnabled = true
               initialSize = 5
               maxActive = 50
               minIdle = 5
               maxIdle = 25
               maxWait = 10000
               maxAge = 10 * 60000
               timeBetweenEvictionRunsMillis = 5000
               minEvictableIdleTimeMillis = 60000
               validationQuery = "SELECT 1"
               validationQueryTimeout = 3
               validationInterval = 15000
               testOnBorrow = true
               testWhileIdle = true
               testOnReturn = false
               jdbcInterceptors = "ConnectionState"
               defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
            }
        }
    }
}
