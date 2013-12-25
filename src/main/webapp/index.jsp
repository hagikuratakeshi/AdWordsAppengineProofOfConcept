<!-- Copyright 2013 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. -->

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>AdWords API on appengine proof of concept</title>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/main.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.13.custom.css" />
</head>
<body>
  <div class="adwords-header">
    <div id="adwords-account">
      <%
      String user = (String) request.getAttribute("user");
      %>
        
        <div id="adwords-account-user" class="adwords-menu-header">
          <span><%=request.getAttribute("user")%></span><span class="dropdown-arrow">▼</span>
          <div id="adwords-account-user-options" class="menu">
            <a href="<%=request.getAttribute("logout_url")%>">Sign out</a>
          </div>
        </div>
      <span>|</span>
    </div>
    <h1 id="adwords-logo">AdWords API on appengine proof of concept</h1>
  </div>
  <hr />
  <div id="adwords-content">
    Listing existing campaigns.
    <ol>
      <%
      for (String campaignName : (List<String>) request.getAttribute("campaignNames")) {
        %><li><%=campaignName%></li>
      <%}%>
    </ol>
  </div>
  <div id="adwords-signin-tooltip">Click "Sign in" to get started.</div>
</body>
</html>
