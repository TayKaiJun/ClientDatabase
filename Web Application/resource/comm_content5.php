	
<div id="demo">
  <h1>Insurance Company</h1>
	<!-- Responsive table starts here -->
	  <!-- For correct display on small screens you must add 'data-title' to each 'td' in your table -->
	  <div class="table-responsive-vertical shadow-z-1">
	  <!-- Table starts here -->
	  <table id="table" class="table table-hover table-mc-light-blue">
		  <thead>
			<tr>
			  <th>Insurer ID</th>
			  <th>Company Name</th>
			  <th>Address</th>
			  <th>Office No.</th>
			  <th>Website</th>
			</tr>
		  </thead>
		  <tbody>
		    <?php
				$db = new mysqli('localhost', 'root', '', 'clients');
			
				$sql = "SELECT * FROM insurer";
				$result = $db->query($sql);		
				if ($result->num_rows > 0) {
					// output data of each row
					while($row = $result->fetch_assoc()) {
						echo "<tr>";
						echo "<td data-title='Insurer ID'>".$row["insurerid"]."</td>";
						echo "<td data-title='Company Name'>".$row["insurername"]."</td>";
						echo "<td data-title='Address'>".$row["address"]."</td>";
						echo "<td data-title='Office No.'>".$row["officeno"]."</td>";
						echo "<td data-title='Website'>";
						echo "<a href='".$row["website"]."'>".$row["website"]."</a>";
						echo "</td>";
						echo "</tr>";
					}
				} else {
					echo "<tr><td data-title='Item'>NO DATA</td><td data-title='Data'>NULL</td></tr>";
				}
				
			?>
		  </tbody>
		</table>
	  </div>
</div>