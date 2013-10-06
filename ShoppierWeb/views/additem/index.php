
<div class="content">
	<h1>Add item</h1>
    <h3>Select a store, an aisle, and then a location </h3>

    <form method="post" action="<?php echo URL;?>additem/create">
        <label>Item Name </label><input type="text" name="note_text" />
        <label>Brand</label><input type="text" name="note_text" />
        <label>Category</label><input type="text" name="note_text" />
        <select id="store">
            <?php
                $this->stores = json_decode($this->stores);
                if ($this->stores)
                {
                    foreach($this->stores as $key => $value)
                    {
                        echo '<option value=' . $value->store_pk . '>'. $value->store_name . '</option>';
                    }
                }
            ?>
      
        </select>
        <select id="aisle">
        </select>
        <select id="location">
        </select>
        <input type="submit" value='Add Item' autocomplete="off" />
    </form>

    <h1 style="margin-top: 50px;">List of your notes</h1>


</div>
<script>

function loadAisles()
{
    var store = $("#store option:selected").attr("value");
        //load appropriate aisle information
        $.post(
        
            "http://jonnyklemmer.com/shoppier/additem/aisles", 
            {store: store}, 
            function(data){
                var arr = JSON.parse(data);
                $("#aisle").html("");
                for(key in arr)
                {
                    $("#aisle").append("<option value="+arr[key]['aisle_pk']+">" + arr[key]['aisle_name']+ "</option");
                    
                }   
                $("#aisle").prop('disabled', false); 
            }

        );     
}
function loadLocations()
{
    var aisle = $("#aisle option:selected").attr("value");
    $.post(
        
            "http://jonnyklemmer.com/shoppier/additem/locations", 
            {aisle: aisle}, 
            function(data){
                var arr = JSON.parse(data);
                $("#location").html("");
                for(key in arr)
                {
                    $("#location").append("<option value="+(arr[key]['loc_pk'])+">" + arr[key]['loc_loc']+ "</option");
                    
                }   
            }

        );   
}
 $(document).ready(function() 
    { 
        //get selected store  
        loadAisles();
        $("#aisle option").eq(1).prop('selected', true);
        //loadLocations();
        //make them pretty 
        $("#store").select2();
        $("#aisle").select2();
        $("#location").select2();    
    });
 $("#store").change(function()
 { 

   loadAisles();   
 });
 $("#aisle").change(function()
 { 
    loadLocations();
         
 });
</script>