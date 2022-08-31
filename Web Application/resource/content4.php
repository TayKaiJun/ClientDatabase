	
<div id="demo">
  <h1>Payment</h1>
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
				$sql = "SELECT paymentid, mypdate, clientpdate, amount, name, insurername
						FROM payment, insurer, client, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t
						WHERE payment.clientid = t.clientid AND payment.clientid = client.clientid AND client.insurerid = insurer.insurerid AND payment.clientid=".$_SESSION['clientid'].";";
				$result = $db->query($sql);		
				if ($result->num_rows > 0) {
					// output data of each row
					while($row = $result->fetch_assoc()) {
						if(is_null($row["mypdate"]) && !is_null($row["clientpdate"])){
							echo "<tr><td data-title='Item'>Payment ID</td><td data-title='Data'>".$row["paymentid"]."</td></tr>";
							echo "<tr><td data-title='Item'>Payer</td><td data-title='Data'>".$row["name"]."</td></tr>";
							echo "<tr><td data-title='Item'>Receiver</td><td data-title='Data'>".$row["insurername"]."</td></tr>";
							echo "<tr><td data-title='Item'>Payment Amount</td><td data-title='Data'>$".$row["amount"]."</td></tr>";
							if($row["clientpdate"]=="0000-00-00")
								$row["clientpdate"] = "";
							echo "<tr><td data-title='Item'>Payment Date</td><td data-title='Data'>".$row["clientpdate"]."</td></tr>";
						} elseif(!is_null($row["mypdate"]) && !is_null($row["clientpdate"])){
							echo "<tr><td data-title='Item'>Payment ID</td><td data-title='Data'>".$row["paymentid"]."</td></tr>";
							echo "<tr><td data-title='Item'>Payer</td><td data-title='Data'>TKS</td></tr>";
							echo "<tr><td data-title='Item'>Receiver</td><td data-title='Data'>".$row["insurername"]."</td></tr>";
							echo "<tr><td data-title='Item'>Payment Amount</td><td data-title='Data'>$".$row["amount"]."</td></tr>";
							if($row["mypdate"]=="0000-00-00")
								$row["mypdate"] = "";
							echo "<tr><td data-title='Item'>Payment Date</td><td data-title='Data'>".$row["mypdate"]."</td></tr>";
						}
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
<br>
<div id="demo">
  <h1>Loans</h1>
	<!-- Responsive table starts here -->
	  <!-- For correct display on small screens you must add 'data-title' to each 'td' in your table -->
	  <div class="table-responsive-vertical shadow-z-1">
	  <!-- Table starts here -->
	  <table id="table" class="table table-hover table-mc-light-blue">
		  <thead>
			<tr>
			  <th>Payer</th>
			  <th>Receiver</th>
			  <th>Amount</th>
			  <th>Payment Date</th>
			</tr>
		  </thead>
		  <tbody>
		    <?php
				$db = new mysqli('localhost', 'root', '', 'clients');
			
				$sql = "SELECT paymentid, mypdate, clientpdate, amount, name, insurername
						FROM payment, insurer, client, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t
						WHERE payment.clientid = t.clientid AND payment.clientid = client.clientid AND client.insurerid = insurer.insurerid AND payment.clientid=".$_SESSION['clientid'].";";
				$result = $db->query($sql);		
				if ($result->num_rows > 0) {
					// output data of each row
					while($row = $result->fetch_assoc()) {
						if(!is_null($row["mypdate"]) && is_null($row["clientpdate"])){
							echo "<tr>";
							echo "<td data-title='Payer'>".$row["name"]."</td>";
							echo "<td data-title='Receiver'>TKS</td>";
							echo "<td data-title='Payment Amount'>$".$row["amount"]."</td>";
							echo "<td data-title='Payment Date'>UNPAID</td>";
							echo "</tr>";
						} elseif(!is_null($row["mypdate"]) && !is_null($row["clientpdate"])){
							echo "<tr>";
							echo "<td data-title='Payer'>".$row["name"]."</td>";
							echo "<td data-title='Receiver'>".$row["insurername"]."</td>";
							echo "<td data-title='Payment Amount'>$".$row["amount"]."</td>";
							echo "<td data-title='Payment Date'>".$row["clientpdate"]."</td>";
							echo "</tr>";
						} elseif(is_null($row["mypdate"]) && !is_null($row["clientpdate"])){
							echo "<tr>";
							echo "<td data-title='Payer'>NIL</td>";
							echo "<td data-title='Receiver'>NIL</td>";
							echo "<td data-title='Payment Amount'>NIL</td>";
							echo "<td data-title='Payment Date'>NIL</td>";
							echo "</tr>";
						}
					}
				} else {
					echo "<tr><td data-title='Item'>NO DATA</td><td data-title='Data'>NULL</td></tr>";
				}
				
			?>
		  </tbody>
		</table>
	  </div>
</div>