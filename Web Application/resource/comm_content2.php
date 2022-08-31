	
<div id="demo">
  <h1>Company Details</h1>
	<!-- Responsive table starts here -->
	  <!-- For correct display on small screens you must add 'data-title' to each 'td' in your table -->
	  <div class="table-responsive-vertical shadow-z-1">
	  <!-- Table starts here -->
	  <table id="table" class="table table-hover table-mc-light-blue">
		  <thead>
			<tr>
			  <th>Item</th>
			  <th>Data</th>
			</tr>
		  </thead>
		  <tbody>
		    <?php
				$db = new mysqli('localhost', 'root', '', 'clients');
			
				session_start();
				$sql = "SELECT * FROM commercial WHERE clientid=".$_SESSION['clientid'].";";
				$result = $db->query($sql);		
				if ($result->num_rows > 0) {
					// output data of each row
					while($row = $result->fetch_assoc()) {
						echo "<tr><td data-title='Item'>Client ID</td><td data-title='Data'>".$row["clientid"]."</td></tr>";
						echo "<tr><td data-title='Item'>Company Name</td><td data-title='Data'>".$row["companyname"]."</td></tr>";
						echo "<tr><td data-title='Item'>ROC</td><td data-title='Data'>".$row["roc"]."</td></tr>";
						echo "<tr><td data-title='Item'>Person-in-Charge</td><td data-title='Data'>".$row["pic_name"]."</td></tr>";
						echo "<tr><td data-title='Item'>PIC Phone No.</td><td data-title='Data'>".$row["pic_phoneno"]."</td></tr>";
						echo "<tr><td data-title='Item'>Address</td><td data-title='Data'>".$row["address"]."</td></tr>";
						echo "<tr><td data-title='Item'>Office No.</td><td data-title='Data'>".$row["officeno"]."</td></tr>";
						echo "<tr><td data-title='Item'>Email</td><td data-title='Data'>".$row["email"]."</td></tr>";
					}
				} else {
					echo "<tr><td data-title='Item'>NO DATA</td><td data-title='Data'>NO DATA</td></tr>";
				}
				
			?>
		  </tbody>
		</table>
	  </div>
</div>
<br>
<?php
	$db = new mysqli('localhost', 'root', '', 'clients');
	$sql = "SELECT name, ic, dob, maritalstatus, occupation, nationality, drivingexp 
			FROM driver 
			WHERE clientid = ".$_SESSION['clientid'].";";
	$result = $db->query($sql);	
	$i=0;	
	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()) {
			echo "<br>";
			echo "<div id='demo'>";
			if($i==0)
				echo "<h1>Insured Driver Details</h1>";
			echo "<div class='table-responsive-vertical shadow-z-1'>";
			echo "<table id='table' class='table table-hover table-mc-light-blue'>";
			echo "<thead>";
			echo "<tr>";
			echo "<th>Item</th>";
			echo "<th>Data</th>";
			echo "</tr>";
			echo "</thead>";
			echo "<tbody>";
			echo "<tr><td data-title='Item'>Occupation</td><td data-title='Data'>".$row["occupation"]."</td></tr>";
			echo "<tr><td data-title='Item'>Name</td><td data-title='Data'>".$row["name"]."</td></tr>";
			echo "<tr><td data-title='Item'>IC</td><td data-title='Data'>".$row["ic"]."</td></tr>";
			echo "<tr><td data-title='Item'>DOB</td><td data-title='Data'>".$row["dob"]."</td></tr>";
			echo "<tr><td data-title='Item'>Marital Status</td><td data-title='Data'>".$row["maritalstatus"]."</td></tr>";
			echo "<tr><td data-title='Item'>Nationality</td><td data-title='Data'>".$row["nationality"]."</td></tr>";
			echo "<tr><td data-title='Item'>Driving Experience</td><td data-title='Data'>".$row["drivingexp"]." Years</td></tr>";
			echo "</tbody>";
			echo "</table>";
			echo "</div>";
			echo "</div>";
			$i++;
		}
	}
?>