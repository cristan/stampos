package stampos

class BackupDBController {


    def index() {
		def filename = params.filename;
		if(filename)
		{
			//filename = filename.replace('\\', '/')
		}
		else
		{
			filename = "backup.zip";
		}
		println filename
		println "BACKUP TO '"+filename+"'"
		def conn = new groovy.sql.Sql((java.sql.Connection) grailsApplication.mainContext.sessionFactory.currentSession.connection())
	    conn.execute("BACKUP TO '"+filename+"'")
	    conn.close()
		
		render "Made a backup to "+filename;
    }
}
