class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		//"/"(view:"/index")
		"/"(controller:"index")
		"500"(view:'/error')
		
		"/klantinfo/$id"(controller:"klantinfo", action:"klantInfo", id: $id)
		"/klantmutaties"(controller:"klantinfo", action:"mutaties")
		"/deblokkeer"(controller:"deblokkeer", action:"deblokkeer")
	}
}
