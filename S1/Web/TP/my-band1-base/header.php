<?php
require('band_generators.php');
require('dbconnect.php');


session_start();


if (!isset($_SESSION["bandname"])){
    $_SESSION["bandname"] = generate_bandname() ;
}

if (!isset($_SESSION["bandlogo"])){
    $_SESSION["bandlogo"] = generate_bandlogo() ;
}

?>

<html>
<head>
<title>My Band</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet" type="text/css">

    <!-- Font Awesome CSS -->
    <link rel='stylesheet' href='https://use.fontawesome.com/releases/v5.3.1/css/all.css'>


  <link rel="stylesheet" href="myband.css">

  

  
</head>
<body >
    <div class="navbar">
        <ul>
            <li class="brandlogo"><img height="75" src="logos/<?php echo $_SESSION['bandlogo']  ;?>"/></li>
            <li class="brandtext">&nbsp;&nbsp;<?php echo $_SESSION["bandname"] ;?></li>
        
            <li style="float:right;"><a href="contact.php">CONTACT</a></li>
            
            <li style="float:right;"><a href="setlist.php">SETLIST</a></li>
            
            <li style="float:right"><a href="index.php">HOME</a></li>
        
        </ul>

    </div>

    
