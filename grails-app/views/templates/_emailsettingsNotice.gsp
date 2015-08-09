<g:if test="${grailsApplication.config.grails.mail.disabled}">
	<div class="warnings">
	In deze omgeving worden e-mails niet daadwerkelijk gestuurd.
	</div>
</g:if>
<g:else>
	<g:if test="${grailsApplication.config.grails.mail.overrideAddress}">
		<div class="warnings">
		In deze omgeving worden alle e-mails verstuurd naar ${grailsApplication.config.grails.mail.overrideAddress}
		</div>
	</g:if>
</g:else>
