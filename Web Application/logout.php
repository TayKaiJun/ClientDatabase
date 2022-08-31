<?php
	mysql_close() ;
	session_start();
	unset($_SESSION['count']);
	header('Location: '."loginpage.html");
?>