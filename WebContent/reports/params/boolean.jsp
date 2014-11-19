<% 
// For creating a boolean parameter via jsp include directive.

// Get the parameters for this boolean parameter
String label = request.getParameter("label"); 
String name = request.getParameter("name"); 
String defaultStr = request.getParameter("default");
boolean defaultValue = defaultStr != null && defaultStr.toLowerCase().equals("false") ? 
	false : true;
%>

  <div class="param">
     <label for="<%= name %>"><%= label %>:</label> 
     <select id="<%= name %>" name="<%= name %>">
       <option value="true"  <%=  defaultValue ? "selected=\"selected\"" : ""%>>True</option>
       <option value="false" <%= !defaultValue ? "selected=\"selected\"" : ""%>>False</option>
     </select>
   </div>
