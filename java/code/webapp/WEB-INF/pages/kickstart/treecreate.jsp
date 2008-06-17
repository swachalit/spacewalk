<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://rhn.redhat.com/rhn" prefix="rhn" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>

<html:html xhtml="true">

<head>
<meta http-equiv="Pragma" content="no-cache" />

<script language="javascript" src="/javascript/refresh.js"></script>
</head>

<body>

<html:errors />
<html:messages id="message" message="true">
  <rhn:messages><c:out escapeXml="false" value="${message}" /></rhn:messages>
</html:messages>

<rhn:toolbar base="h1" img="/img/rhn-icon-info.gif" imgAlt="info.alt.img">
  <bean:message key="treecreate.jsp.toolbar"/>
</rhn:toolbar>

<bean:message key="treecreate.jsp.header1"/>

<h2><bean:message key="treecreate.jsp.header2"/></h2>

<div>
    <html:form method="post" action="/kickstart/TreeCreate.do" styleId="dist-tree-form">
      <%@ include file="tree-form.jspf" %>
      <hr/><table align="right">
          <c:if test="${requestScope.hidesubmit != 'true'}">
          <tr>
            <td><html:submit><bean:message key="createtree.jsp.submit"/></html:submit></td>
          </tr>
          </c:if>
		</table>
        <html:hidden property="submitted" value="true"/>
    </html:form>
</div>

</body>
</html:html>

