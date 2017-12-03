<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  <c:set var="language" value="" scope="session" />
  <fmt:setLocale value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" />
  <fmt:setBundle basename="org.transitclock.i18n.text" />
<%@include file="/template/includes.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><fmt:message key="div.specifyparametrs18" /></title>

  <!-- Load in Select2 files so can create fancy route selector -->
  <link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0/css/select2.min.css" rel="stylesheet" />
  <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0/js/select2.min.js"></script>

  <link href="../params/reportParams.css" rel="stylesheet"/>

  <script>
    function execute() {
      var selectedVehicleId = $("#vehicle").val();
      var format = $('input:radio[name=format]:checked').val();
      var url = apiUrlPrefix + "/command/resetVehicle?v=" + selectedVehicleId + "&format=" + format;

   	  // Actually do the API call
   	  location.href = url;
    }
  </script>

</head>
<body>

<%@include file="/template/header.jsp" %>

<div id="title">
   Select Parameters for Reset Vehicle API
</div>

<div id="mainDiv">
   <%-- Create route selector --%>
   <jsp:include page="../params/vehicleSingle.jsp" />

   <%-- Create json/xml format radio buttons --%>
   <jsp:include page="../params/jsonXmlFormat.jsp" />

   <%-- Create submit button --%>
   <jsp:include page="../params/submitApiCall.jsp" />
</div>

</body>
</html>