<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <style>
        button {
          background-color: #cc5500;
          color: white;
          padding: 10px 15px;
          border: none;
          border-radius: 4px;
          cursor: pointer;
        }
        button:hover {
          background-color: #45a049;
        }
        h1 {
          background-color: black;
          color: #cc5500;
          text-align: center;
          padding-top: 5px;
          padding-bottom: 5px;
        }
        body {
          font-family: Arial, sans-serif;
          margin: 20px;
          background-color: #333232;
          color: #f2f2f2;
        }
        .con {
          max-width: 400px;
          margin-left: auto;
          margin-right: auto;
          border-radius: 3px;
          border-width: 2px;
          border-color: white;
          border-style: double;
          margin-top: 30px;
          padding-top: 30px;
          padding-bottom: 30px;
          color: white;
          text-align: center; /* Center text within the container */
        }
    </style>
    <meta charset="UTF-8">
    <title>Display Workout Data</title>
</head>
<body>
    <h1>Workout Data</h1>
    <div class="con">
        <p><span style="color:#cc5500 "><strong>Name: </strong></span><%= request.getAttribute("name") %></p>
        <p><span style="color:#cc5500 "><strong>Workout Date: </strong></span><%= request.getAttribute("workoutDate") %></p>
        <p><span style="color:#cc5500 "><strong>Exercise Type: </strong></span><%= request.getAttribute("exerciseType") %></p>
        <p><span style="color:#cc5500 "><strong>Sets: </strong></span><%= request.getAttribute("sets") %></p>
        <p><span style="color:#cc5500 "><strong>rep1: </strong></span><%= request.getAttribute("rep1") %></p>
        <p><span style="color:#cc5500 "><strong>weight1 : </strong></span><%= request.getAttribute("weight1") %></p>
        <p><span style="color:#cc5500 "><strong>rep2 : </strong></span><%= request.getAttribute("rep2") %></p>
        <p><span style="color:#cc5500 "><strong>weight2 : </strong></span><%= request.getAttribute("weight2") %></p>
        <p><span style="color:#cc5500 "><strong>rep3 : </strong></span><%= request.getAttribute("rep3") %></p>
        <p><span style="color:#cc5500 "><strong>weight3 : </strong></span><%= request.getAttribute("weight3") %></p>
    
        <p><span style="color:#cc5500 ">Following details are added successfully into the database :)</span></p>

        <button onclick="goBack()">Go Back</button>

        <script>
            function goBack() {
                // Use the history object to navigate back
                window.history.back();
            }
        </script>
    </div>
</body>
</html>
