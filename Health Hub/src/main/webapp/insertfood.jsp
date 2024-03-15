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
            margin: 30px auto; /* Center the div horizontally */
            border-radius: 3px;
            border-width: 2px;
            border-color: white;
            border-style: double;
            padding: 30px;
            color: white;
        }
    </style>
    <meta charset="UTF-8">
    <title>Food Information</title>
</head>

<body>
    <h1>Food Information</h1>
    <div class="con">
        <p><span style="color:#cc5500 "><strong>Food Name: </strong></span>${foodName}</p>
        <p><span style="color:#cc5500 "><strong>Protein (g): </strong></span>${protein}</p>
        <p><span style="color:#cc5500 "><strong>Fat (g): </strong></span>${fat}</p>
        <p><span style="color:#cc5500 "><strong>Carbohydrates (g): </strong></span>${carbs}</p>
        <p><span style="color:#cc5500 "><strong>Calories: </strong></span>${calories}</p>

        <p><span style="color:#cc5500 ">Data Inserted Successfully :)</span></p>

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
