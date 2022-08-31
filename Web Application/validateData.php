<?php

	$user;
	$password;
	$i = 0;
    foreach ($_POST as $key => $value) {
		if($i==0){
			$user = $value;
			$i++;
		}
		/*
		elseif($i==1){
			if(is_null($value))
				$value = "";
			$password = $value;
			$i++;
		}*/
	}
	$db = new mysqli('localhost','root','','clients');
	if ($db->connect_error) {
		header('Location: '."loginpage.php?error=connection_error");
        die('Connect Error (' . $db->connect_errno . ') '
             . $mysqli->connect_error);
	} elseif ($result = $db->query("SELECT DATABASE()")) {
		session_start();
		if (preg_match("/^[\d]{3}-[\d]{2}-[\d]{4}$/", $user, $match)){
			$sql = "SELECT clientid FROM individual WHERE ic = '".$user."'";
			$result = $db->query($sql);
			if ($result->num_rows > 0) {
				// output data of each row
				while($row = $result->fetch_assoc()) {
					$_SESSION['clientid']=$row["clientid"];
				}
				header('Location: '."individualAcc.html");
			}
			else{
				header('Location: '."loginpage.php?error=incorrect_login_details");
			}
		} else if(preg_match("/^[\d]{6}-[\d]{4}$/", $user, $match)){
			$sql = "SELECT clientid FROM commercial WHERE roc = '".$user."';";
			$result = $db->query($sql);
			if ($result->num_rows > 0) {
				// output data of each row
				while($row = $result->fetch_assoc()) {
					$_SESSION['clientid']=$row["clientid"];
				}
				header('Location: '."commercialAcc.html");
			}
			else{
				header('Location: '."loginpage.php?error=incorrect_login_details");
			}
		} else{
			header('Location: '."loginpage.php?error=incorrect_login_details");
		}
	}
?>