<div id="demo">
  <h1>Policy Status</h1>
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
				$sql = "SELECT * FROM client WHERE clientid=".$_SESSION['clientid'].";";
				$result = $db->query($sql);		
				if ($result->num_rows > 0) {
					// output data of each row
					while($row = $result->fetch_assoc()) {
						echo "<tr><td data-title='Item'>Client ID</td><td data-title='Data'>".$row["clientid"]."</td></tr>";
						if(is_null($row["referrerid"]))
							echo "<tr><td data-title='Item'>Referrer/td><td data-title='Data'>No referrer</td></tr>";
						else{
							$subq = "SELECT referrername FROM referrer WHERE referrerid=".$row['referrerid'];
							$subr = $db->query($subq);
							echo $db->error;							
							$rname = $subr->fetch_assoc();
							echo "<tr><td data-title='Item'>Referrer</td><td data-title='Data'>".$rname["referrername"]."</td></tr>";
						}
						if($row["effectivedate"]=="0000-00-00")
							$row["effectivedate"] = "";
						echo "<tr><td data-title='Item'>Effective Date</td><td data-title='Data'>".$row["effectivedate"]."</td></tr>";
						if($row["expirydate"]=="0000-00-00")
							$row["expirydate"] = "";
						echo "<tr><td data-title='Item'>Expiry Date</td><td data-title='Data'>".$row["expirydate"]."</td></tr>";
						echo "<tr><td data-title='Item'>Premium</td><td data-title='Data'>$".$row["premium"]."</td></tr>";
						echo "<tr><td data-title='Item'>Gross Premium</td><td data-title='Data'>$".$row["grosspremium"]."</td></tr>";
						$subq = "SELECT insurername FROM insurer WHERE insurerid=".$row['insurerid'];
						$subr = $db->query($subq);
						$iname = $subr->fetch_assoc();
						echo "<tr><td data-title='Item'>Insurer</td><td data-title='Data'>".$iname["insurername"]."</td></tr>";
						if($row["renewalstatus"]==0)
							echo "<tr><td data-title='Item'>Renewal Status</td><td data-title='Data'>Not renewed</td></tr>";
						else
							echo "<tr><td data-title='Item'>Renewal Status</td><td data-title='Data'>Renewed</td></tr>";
					}
				} else {
					echo "<tr><td data-title='Item'>NO DATA</td><td data-title='Data'>NO DATA</td></tr>";
				}
				
			?>
		  </tbody>
		</table>
	  </div>
</div>