<!DOCTYPE html>
<html >
<head>
  <meta charset="UTF-8">
  
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css">
  
  <link rel="stylesheet" href="css/loginstyle.css">

  
</head>

<body>
  <!-- multistep form -->
<form id="msform" method="post" action="validateData.php">
  <fieldset>
    <h2 class="fs-title">LOG IN TO VIEW ACCOUNT</h2>
    <h3 class="fs-subtitle">Don't have an account? Sign up now!</h3>
	<?php
		if(isset($_GET["error"]))
			echo "<p style='color: red;font-size:12px;margin-bottom: 10px;'>".$_GET["error"]."</p>";
	?>
	<input type="text" name="user" placeholder="IC/ROC number" />
    <!--<input type="password" name="pass" placeholder="Password" />-->
    <input type="submit" name="login" class="action-button" value="Log In"/>
	<input type="button" name="signup" class="link action-button" value="Sign Up" onclick="window.location.href='signuppage.html'"/>
  </fieldset>
</form>

	<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js'></script>
    <script src="js/login.js"></script>

	<script>
	
	$(document).ready( function() {

		$('body').css('display', 'none');
		$('body').fadeIn(1000);
		$('.link').click(function(event) {

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
