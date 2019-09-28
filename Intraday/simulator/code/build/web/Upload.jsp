<%-- 
    Document   : Upload
    Created on : Sep 24, 2019, 5:56:08 PM
    Author     : nishant.vibhute
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
  
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-datetimepicker/2.5.20/jquery.datetimepicker.min.css"/>
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-datetimepicker/2.5.20/jquery.datetimepicker.full.min.js"></script>

        <title>Upload</title>
        <script>
            $(function () {
//                $("#datepickerCall").datepicker({dateFormat: "dd-MM-yy", changeMonth: false, changeYear: false, maxDate: "now", minDate: "now"});
                $('#datetimepicker').datepicker({ dateFormat: 'yy-mm-dd' });
            });
        </script>
        
        
    </head>
    <body>
        <s:property value="message"/>
        <s:form action="UploadData" method="post" enctype="multipart/form-data">
             <s:textfield  name="date" label="Date" id="datetimepicker"/>
             <s:file name="zipFile" label="Daily Data"/>
             <s:submit value="submit" name="Upload File" />
        </s:form>
    </body>
</html>
