<%-- Provides schedule adherence data in JSON format. Provides for
     the specified route the number arrivals/departures that
     are early, number late, number on time, and number total for each
     direction for each stop.
     Request parameters are:
       a - agency ID
       r - route ID or route short name.
       dateRange - in format "xx/xx/xx to yy/yy/yy"
       beginDate - date to begin query. For if dateRange not used.
       numDays - number of days can do query. Limited to 31 days. For if dateRange not used.
       beginTime - for optionally specifying time of day for query for each day
       endTime - for optionally specifying time of day for query for each day
       allowableEarlyMinutes - how early vehicle can be and still be OK.  Decimal format OK.
       allowableLateMinutes - how early vehicle can be and still be OK. Decimal format OK.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="org.transitime.reports.GenericJsonQuery" %>
<%@ page import="org.transitime.reports.SqlUtils" %>
<%
int k = 0;
try {
  k = 1;
String allowableEarlyStr = request.getParameter("allowableEarly");
if (allowableEarlyStr == null || allowableEarlyStr.isEmpty())
	allowableEarlyStr = "1.0";
  k = 2;
String allowableEarlyMinutesStr = "'" + SqlUtils.convertMinutesToSecs(allowableEarlyStr) + " seconds'";
k = 3;
String allowableLateStr = request.getParameter("allowableLate");
if (allowableLateStr == null || allowableLateStr.isEmpty())
	allowableLateStr = "4.0";
  k = 4;
String allowableLateMinutesStr = "'" + SqlUtils.convertMinutesToSecs(allowableLateStr) + " seconds'";
k = 5;
String sql =
	"SELECT "
	+ "     COUNT(CASE WHEN scheduledtime-time > " + allowableEarlyMinutesStr + " THEN 1 ELSE null END) as early, \n"
	+ "     COUNT(CASE WHEN scheduledtime-time <= " + allowableEarlyMinutesStr + " AND time-scheduledtime <= "
				+ allowableLateMinutesStr + " THEN 1 ELSE null END) AS ontime, \n"
    + "     COUNT(CASE WHEN time-scheduledtime > " + allowableLateMinutesStr + " THEN 1 ELSE null END) AS late, \n"
    + "     COUNT(*) AS total, \n"
    + "     s.name AS stop_name, \n"
    + "     ad.directionid AS direction_id \n"
    + "FROM arrivalsdepartures ad, stops s \n"
    + "WHERE "
    // To get stop name
    + " ad.configrev = s.configrev \n"
    + " AND ad.stopid = s.id \n"
    // Only need arrivals/departures that have a schedule time
    + " AND ad.scheduledtime IS NOT NULL \n"
    // Specifies which routes to provide data for
    + SqlUtils.routeClause(request, "ad") + "\n"
    + SqlUtils.timeRangeClause(request, "ad.time", 7) + "\n"
    // Grouping and ordering is a bit complicated since might also be looking
    // at old arrival/departure data that doen't have stoporder defined. Also,
    // when configuration changes happen then the stop order can change.
    // Therefore want to group by directionId and stop name. Need to also
    // group by stop order so that can output it, which can be useful for
    // debugging, plus need to order by stop order. For the ORDER BY clause
    // need to order by direction id and stop order, but also the stop name
    // as a backup for if stoporder not defined for data and is therefore
    // always the same and doesn't provide any ordering info.
    + " GROUP BY directionid, s.name, ad.stoporder \n"
    + " ORDER BY directionid, ad.stoporder, s.name";
k = 6;
// Just for debugging
System.out.println("\nFor schedule adherence by stop query sql=\n" + sql);

// Do the query and return result in JSON format
String agencyId = request.getParameter("a");
k = 7;
String jsonString = GenericJsonQuery.getJsonString(agencyId, sql);
k = 8;
response.setContentType("application/json");
k = 9;
response.setHeader("Access-Control-Allow-Origin", "*");
k = 10;
response.getWriter().write(jsonString);
k = 11;
} catch (Exception e) {
	response.setStatus(400);
	response.getWriter().write(k+e;
	return;
}%>
