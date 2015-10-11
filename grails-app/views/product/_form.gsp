<%@ page import="stampos.Product" %>



<div class="fieldcontain ${hasErrors(bean: productInstance, field: 'naam', 'error')} required">
	<label for="naam">
		<g:message code="product.naam.label" default="Naam" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="naam" required="" value="${productInstance?.naam}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: productInstance, field: 'colliGrootte', 'error')} required">
	<label for="colliGrootte">
		<g:message code="product.colliGrootte.label" default="Colli Grootte" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="colliGrootte" type="number" min="1" value="${productInstance.colliGrootte}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: productInstance, field: 'price', 'error')} ">
	<label for="naam">
		<g:message code="productprijs.prijs.label" default="Prijs" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="price" required="" value="${productInstance?.price}"/>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: productInstance, field: 'sortering', 'error')} required">
	<label for="sortering">
		<g:message code="product.sortering.label" default="Sortering" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="sortering" type="number" value="${productInstance.sortering}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: productInstance, field: 'zichtbaar', 'error')} ">
	<label for="zichtbaar">
		<g:message code="product.zichtbaar.label" default="Zichtbaar" />
		
	</label>
	<g:checkBox name="zichtbaar" value="${productInstance?.zichtbaar}" />

</div>

