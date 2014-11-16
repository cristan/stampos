<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="beheer">
	<title>Upload financien</title>
</head>
<body>
<g:uploadForm action="doUpload">
	<!-- TODO: only accept CSV files http://www.wufoo.com/html5/attributes/07-accept.html -->
    <input type="file" name="file">
    <g:submitButton name="upload" value="Upload"/>
</g:uploadForm>
</body>
</html>