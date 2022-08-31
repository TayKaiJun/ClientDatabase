<!DOCTYPE html>
<html >
<head>
  <meta charset="UTF-8">
  <title>Sign Up</title>
  
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css">

  
      <link rel="stylesheet" href="css/loginstyle.css">

  
</head>

<body>
	<?php
	// define variables and set to empty values
	$userErr = $comErr = $nameErr = $dobErr = $addErr = $phoneErr = $emailErr = $vehicleErr = $vmkErr = $vmdErr ="";
	$rdTaxErr = $insurerErr = $premiumErr = $effErr = $expErr = "";
	$user = $com = $name = $dob = $add = $phone = $email = $vehicle = $vmake = $vmodel = "";
	$rdTax = $insurer = $premium = $eff = $exp = "";
	$indiv = true;

	if ($_SERVER["REQUEST_METHOD"] == "POST") {
		
	  //IC/ROC - required
	  if (empty($_POST["identifier"])) {
		$userErr = "IC/ROC is required";
	  } else {
		//$user = test_input($_POST["identifier"]);
		$user = $_POST["identifier"];
		if (preg_match("/^[\d]{3}-[\d]{2}-[\d]{4}$/",$user)) {
			$indiv = true;
		} elseif (preg_match("/^[\d]{6}-[\d]{4}$/",$user)){
			$indiv = false;
		} else{
			$userErr = "Invalid identifier format!"; 
		}
		
		$db = new mysqli('localhost','root','','clients');
		if($indiv){
			$sql = "SELECT clientid FROM individual WHERE ic = '".$user."'";
			$result = $db->query($sql);
			if ($result->num_rows > 0) {
				$userErr = "IC already exists";
			}
		} else{
			$sql = "SELECT clientid FROM commercial WHERE roc = '".$user."'";
			$result = $db->query($sql);
			if ($result->num_rows > 0) {
				$userErr = "ROC already exists";
			}
		}
	  }
	  
	  //Com name - required (if comm)
	  if (!$indiv && empty($_POST["com"])) {
		$comErr = "Company name is required";
	  } else {
		  if(!$indiv){
			$com = test_input($_POST["com"]);
		  }
	  }
	  
	  //name - required
	  if (empty($_POST["name"])) {
		if($indiv)
			$nameErr = "Name is required";
		else
			$nameErr = "PIC's Name is required";
	  } else {
		$name = test_input($_POST["name"]);
		// check if name only contains letters and whitespace
		if (!preg_match("/^[a-zA-Z ]*$/",$name)) {
		  $nameErr = "Only letters and white space allowed"; 
		}
	  }
	  
	  //dob
	  if (empty($_POST["dob"])) {
		$dob = "";
	  } else{
		$dob = $_POST["dob"]; 
		//dont strip the /
		if (!preg_match("/^[\d]{4}-[\d]{2}-[\d]{2}$/",$dob)) {
			$dobErr = "Invalid date format!"; 
		}
	  }
	  
	  //address
	  if (empty($_POST["address"])) {
		$add = "";
	  } else{
		$add = test_input($_POST["address"]); 
	  }
	  
	  //phone
	  if (empty($_POST["phoneno"])) {
		$phone = "";
	  } else {
		$phone = test_input($_POST["phoneno"]);
		// check if name only contains letters and whitespace
		if (!preg_match("/^[\d]{8}$/",$phone)) {
			$phoneErr = "Invalid phone no.!"; 
		}
	  }
	  
	  //email
	  if (empty($_POST["email"])) {
		$email = "";
	  } else {
		$email = test_input($_POST["email"]);
		// check if e-mail address is well-formed
		if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
		  $emailErr = "Invalid email format"; 
		}
	  }
		
	  //vehicle - required
	  if (empty($_POST["name"])) {
		$vehicleErr = "Vehicle No. is required";
	  } else {
		$vehicle = test_input($_POST["vehicleno"]);
		// check if name only contains letters and whitespace
		if (!preg_match("/^[A-Z]+[0-9]+[A-Z]+$/",$vehicle)) {
		  $vehicleErr = "Invalid Vehicle No."; 
		}
	  }
	  
	  //vmake
	  if (empty($_POST["vmake"])) {
		$vmake = "";
	  } else{
		$vmake = test_input($_POST["vmake"]); 
	  }
	  
	  //vmodel
	  if (empty($_POST["vmodel"])) {
		$vmodel = "";
	  } else{
		$vmodel = test_input($_POST["vmodel"]); 
	  }
	  
	  //rdTax
	  if (empty($_POST["rdtaxexpiry"])) {
		$rdTax = "";
	  } else{
		$rdTax = $_POST["rdtaxexpiry"]; 
		if (!preg_match("/^[\d]{4}-[\d]{2}-[\d]{2}$/",$rdTax)) {
			$rdTaxErr = "Invalid date format!"; 
		}
	  }
	  
	  //Insurer - required
	  if (empty($_POST["insurer"])) {
		$insurerErr = "Insurer is required";
	  } else {
		$insurer = test_input($_POST["insurer"]);
		$db = new mysqli('localhost','root','','clients');
		$sql = "SELECT insurerid FROM insurer WHERE insurername = '".$insurer."'";
		$result = $db->query($sql);
		if ($result->num_rows < 1) {
			$insurerErr = "This insurer does not exist";
		}
	  }

	  //premium
	  if (empty($_POST["premium"])) {
		$premium = "";
	  } else {
		  if (!preg_match("/^[\d]+(\.[\d]{1,2})?$/",$premium)) {
			//$premiumErr = "Invalid number!";
		  } else{
			$premium = test_input($_POST["premium"]);
		  }
	  }
	  
	  //effective date
	  if (empty($_POST["effdate"])) {
		$eff = "";
	  } else{
		$eff = $_POST["effdate"]; 
		if (!preg_match("/^[\d]{4}-[\d]{2}-[\d]{2}$/",$eff)) {
			$rdTaxErr = "Invalid date format!"; 
		}
	  }
	  
	  //expiry date
	  if (empty($_POST["exdate"])) {
		$exp = "";
	  } else{
		$exp = $_POST["exdate"]; 
		if (!preg_match("/^[\d]{4}-[\d]{2}-[\d]{2}$/",$exp)) {
			$rdTaxErr = "Invalid date format!"; 
		}
	  }
	}

	function test_input($data) {
	  $data = trim($data);
	  $data = stripslashes($data);
	  $data = htmlspecialchars($data);
	  return $data;
	}
	
	$errMessages = array();
	array_push($errMessages,$userErr,$comErr,$nameErr,$dobErr);
	array_push($errMessages,$addErr,$phoneErr,$emailErr,$vehicleErr);
	array_push($errMessages,$vmkErr,$vmdErr,$rdTaxErr,$insurerErr);
	array_push($errMessages,$premiumErr,$effErr,$expErr);
	
	$valid = true;
	
	foreach($errMessages as $value){
		if($value != "")
			$valid = false;
	}
	
	$t_dob = $dob;
	$t_add = $add;
	$t_phone = $phone;
	$t_email = $email;
	$t_vmake = $vmake;
	$t_vmodel = $vmodel;
	$t_rdTax = $rdTax;
	$t_premium = $premium;
	$t_eff = $eff;
	$t_exp = $exp;
	/*
	$blankData = array();
	array_push($blankData,$t_dob,$t_add,$t_phone,$t_email,$t_vmake);
	array_push($blankData,$t_vmodel,$t_rdTax,$t_premium,$t_eff,$t_exp);
	foreach($blankData as $value){
		if($value == "")
			$value = "NULL";
	}*/
	if($t_dob == ""){
		$t_dob = "NULL";
	}
	if($t_add == ""){
		$t_add = "NULL";
	}
	if($t_phone == ""){
		$t_phone = "NULL";
	}
	if($t_email == ""){
		$t_email = "NULL";
	}
	if($t_vmake == ""){
		$t_vmake = "NULL";
	}
	if($t_vmodel == ""){
		$t_vmodel = "NULL";
	}
	if($t_rdTax == ""){
		$t_rdTax = "NULL";
	}
	if($t_premium == ""){
		$t_premium = "NULL";
	}
	if($t_eff == ""){
		$t_eff = "NULL";
	}
	if($t_exp == ""){
		$t_exp = "NULL";
	}
	
	$insertErr = "";	
	if($valid){
		$newid=0;
		$insurerid;
		$db = new mysqli('localhost','root','','clients');
		$sql = "SELECT max(clientid) FROM client;";
		$result = $db->query($sql);
		if ($result->num_rows > 0) {
			while($row = $result->fetch_assoc()) {
				$newid = $row["max(clientid)"];
				$newid += 1;
			}
		}
		$sql = "SELECT insurerid FROM insurer WHERE insurername = '".$insurer."'";
		$result = $db->query($sql);
		if ($result->num_rows > 0) {
			while($row = $result->fetch_assoc()) {
				$insurerid = $row["insurerid"];
			}
		}
		
		session_start();
		$_SESSION['clientid'] = $newid;
		
		
		$sql = "INSERT INTO client(clientid,insurerid,premium,effectivedate,expirydate) VALUES(";
		$sql .= $newid.",'".$insurerid."',".$t_premium.",'".$t_eff."','".$t_exp."')";
		if ($db->query($sql) !== TRUE) {
			$insertErr = "Error: " . $sql .", ". $db->error;
		}
		
		$sql = "INSERT INTO vehicle VALUES(";
		$sql .= "'".$vehicle."',".$newid.",'".$t_vmake."','".$t_vmodel."','".$t_rdTax."')";
		if ($db->query($sql) !== TRUE) {
			$insertErr = "Error: " . $sql .", ". $db->error;
		}
		
		if($indiv){
			$sql = "INSERT INTO individual(clientid,ic,name,dob,address,phoneno,email) VALUES(";
			$sql .= $newid.",'".$user."','".$name."','".$t_dob."','".$t_add."',".$t_phone.",'".$t_email."')";
			if ($db->query($sql) !== TRUE) {
				$insertErr1 = "Error: " . $sql .", ". $db->error;
			}
			if($insertErr == "")
				header('Location: '."individualAcc.html");
		} else{
			$sql = "INSERT INTO commercial(clientid,roc,companyname,pic_name,pic_phoneno,address,email) VALUES(";
			$sql .= $newid.",'".$user."','".$com."','".$name."',".$t_phone.",'".$t_add."','".$t_email."')";
			if ($db->query($sql) !== TRUE) {
				$insertErr1 = "Error: " . $sql .", ". $db->error;
			}
			if($insertErr == "")
				header('Location: '."commercialAcc.html");
		}
	}
	
	?>
  <!-- multistep form -->
<form id="msform" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
  <!-- progressbar -->
  <ul id="progressbar">
    <li class="active">Register your IC/ROC</li>
	<li>Your details</li>
    <li>Vehicle details</li>
    <li>Policy Details</li>
  </ul>
  <!-- fieldsets -->
  <fieldset>
    <h2 class="fs-title">Create your account</h2>
    <h3 class="fs-subtitle">Register an identifier</h3>
	<?php if($userErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$nameErr."</p>";?>
	<?php if($insertErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$insertErr."</p>";?>
    <input type="text" name="identifier" placeholder="IC/ROC" value="<?php echo $user;?>" />
	<!--
	<input type="text" name="test" placeholder="Test" />
    <input type="password" name="pass" placeholder="Password" />
    <input type="password" name="cpass" placeholder="Confirm Password" />
	-->
    <input type="button" name="next" class="next action-button" value="Next" />
  </fieldset>
  <fieldset>
    <h2 class="fs-title">Insured Details</h2>
    <h3 class="fs-subtitle">Enter your details</h3>
	<?php if($comErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$comErr."</p>";?>
    <input type="text" name="com" placeholder="Company name (if applicable)" value="<?php echo $com;?>" />
	<?php if($nameErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$nameErr."</p>";?>
	<input type="text" name="name" placeholder="Name" value="<?php echo $name;?>" />
	<?php if($dobErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$dobErr."</p>";?>
	<input type="text" name="dob" placeholder="DOB (yyyy-mm-dd)" value="<?php echo $dob;?>" />
	<?php if($addErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$add."</p>";?>
	<input type="text" name="address" placeholder="Address" value="<?php echo $add;?>" />
	<?php if($phoneErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$phone."</p>";?>
    <input type="text" name="phoneno" placeholder="Handphone No." value="<?php echo $phone;?>" />
	<?php if($emailErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$email."</p>";?>
	<input type="text" name="email" placeholder="Email" value="<?php echo $email;?>" />
	
	<input type="button" name="previous" class="previous action-button" value="Previous" />
    <input type="button" name="next" class="next action-button" value="Next" />
  </fieldset>
  <fieldset>
    <h2 class="fs-title">Insured Details</h2>
    <h3 class="fs-subtitle">Enter vehicle details</h3>
	<?php if($vehicleErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$vehicleErr."</p>";?>
    <input type="text" name="vehicleno" placeholder="Vehicle No." value="<?php echo $vehicle;?>" />
	<?php if($vmkErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$vmkErr."</p>";?>
    <input type="text" name="vmake" placeholder="Vehicle Make" value="<?php echo $vmodel;?>" />
	<?php if($vmdErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$vmdErr."</p>";?>
    <input type="text" name="vmodel" placeholder="Vehicle Model" value="<?php echo $vmake;?>" />
	<?php if($rdTaxErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$rdTaxErr."</p>";?>
	<input type="text" name="rdtaxexpiry" placeholder="Road Tax Expiry Date (yyyy-mm-dd)" value="<?php echo $rdTax;?>" />
    <input type="button" name="previous" class="previous action-button" value="Previous" />
    <input type="button" name="next" class="next action-button" value="Next" />
  </fieldset>
  <fieldset>
    <h2 class="fs-title">Insured Details</h2>
    <h3 class="fs-subtitle">Policy details</h3>
	<?php if($insurerErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$insurerErr."</p>";?>
    <input type="text" name="insurer" placeholder="Insurance Company" value="<?php echo $insurer;?>" />
	<?php if($premiumErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$premiumErr."</p>";?>
    <input type="text" name="premium" placeholder="Premium" value="<?php echo $premium;?>" />
	<?php if($effErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$effErr."</p>";?>
    <input type="text" name="effdate" placeholder="Effective Date (yyyy-mm-dd)" value="<?php echo $eff;?>" />
	<?php if($expErr != "") echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>* ".$expErr."</p>";?>
	<input type="text" name="exdate" placeholder="Expiry Date (yyyy-mm-dd)" value="<?php echo $exp;?>" />
    <input type="button" name="previous" class="previous action-button" value="Previous" />
    <input type="submit" name="submit" class="action-button" value="Submit" />
  </fieldset>
</form>
	<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js'></script>
    <script src="js/signup.js"></script>
	<script>
	
	$(document).ready(function() {

		$('body').css('display', 'none');
		$('body').fadeIn(1000);
		$('.link').click(function() {
			event.preventDefault();
			newLocation = this.href;
			$('body').fadeOut(1000, newpage);
		});

		function newpage() {
			window.location = newLocation;
		}

	});
	
	</script>

</body>
</html>
