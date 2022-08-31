	
<div id="demo">
  <h1>Vehicle Details</h1>
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
				$sql = "SELECT * FROM vehicle WHERE clientid=".$_SESSION['clientid'].";";
				$result = $db->query($sql);		
				if ($result->num_rows > 0) {
					// output data of each row
					while($row = $result->fetch_assoc()) {
						echo "<tr><td data-title='Item'>Client ID</td><td data-title='Data'>".$row["clientid"]."</td></tr>";
						echo "<tr><td data-title='Item'>Vehicle No.</td><td data-title='Data'>".$row["vehicleno"]."</td></tr>";
						echo "<tr><td data-title='Item'>Vehicle Make</td><td data-title='Data'>".$row["vmake"]."</td></tr>";
						echo "<tr><td data-title='Item'>Vehicle Model</td><td data-title='Data'>".$row["vmodel"]."</td></tr>";
						if($row["rdtaxexpiry"]=="0000-00-00")
							$row["rdtaxexpiry"] = "";
						echo "<tr><td data-title='Item'>RoadTax Expiry</td><td data-title='Data'>".$row["rdtaxexpiry"]."</td></tr>";
					}
				} else {
					echo "<tr><td data-title='Item'>NO DATA</td><td data-title='Data'>NULL</td></tr>";
				}
			?>
		  </tbody>
		</table>
	  </div>
	  
</div>
<br>
<?php
	$db = new mysqli('localhost', 'root', '', 'clients');
	$sql = "SELECT * FROM accidentrecord WHERE clientid=".$_SESSION['clientid'].";";
	$result = $db->query($sql);			
	$i=0;
	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()) {
			echo "<br>";
			echo "<div id='demo'>";
			if($i==0)
				echo "<h1>Accident Records</h1>";
			echo "<div class='table-responsive-vertical shadow-z-1'>";
			echo "<table id='table' class='table table-hover table-mc-light-blue'>";
			echo "<thead>";
			echo "<tr>";
			echo "<th>Item</th>";
			echo "<th>Data</th>";
			echo "</tr>";
			echo "</thead>";
			echo "<tbody>";
			echo "<tr><td data-title='Item'>Date of Accident</td><td data-title='Data'>".$row["doa"]."</td></tr>";
			echo "<tr><td data-title='Item'>Claim Amount</td><td data-title='Data'>$".$row["claimamount"]."</td></tr>";
			echo "<tr><td data-title='Item'>Remarks</td><td data-title='Data'>".$row["remarks"]."</td></tr>";
			echo "</tbody>";
			echo "</table>";
			echo "</div>";
			echo "</div>";
			$i++;
		}
	}
?>