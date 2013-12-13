<div class="content">
    <div class="well well-large">
        <form id= "itemform" class="form-horizontal" method="post" onSubmit="addItem(); return false;">
            <h1>Add item</h1>
            <div class="control-group">
                <label class="control-label" for="item_name">Item Name </label>
                <div class="controls">
                    <input id="item_name" type="text" placeholder="Example: Cookies"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="item_brand">Brand</label>
                <div class="controls">
                    <input id="item_brand" type="text" placeholder="Example: Oreos"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="store">Store</label>
                <div class="controls">
                    <select class="" style="width:250px;" id="store">
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
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="aisle">Aisle</label>
                <div class="controls">
                    <select class="" style="width:150px;" id="aisle">
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="location">Locations</label>
                <div class="controls">
                    <select class="" style="width:150px;"  id="location">
                    </select>
                </div>
            </div>
            <br />
            <div class="form-actions">
                <INPUT TYPE="SUBMIT" class="btn btn-primary" VALUE="Add Item"/>
            </div>
        </form>

        <h1 id="success" style="margin-top: 50px;"></h1>

    </div>
</div>
<script>

function loadAisles()
{
    var store = $("#store option:selected").attr("value");
        //load appropriate aisle information
        $.post(
        
            "<?php echo URL;?>additem/aisles", 
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
        
            "<?php echo URL;?>additem/locations", 
            {aisle: aisle}, 
            function(data){
                var arr = JSON.parse(data);
                $("#location").html("");
                for(key in arr)
                {
                    $("#location").append("<option value="+(arr[key]['cat_pk'])+">" + arr[key]['cat_name']+ "</option");
                    
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
 function addItem()
{
    console.log($('#item_name').val());
    console.log($("#item_cat").val());
    var itemstr = $("#location option:selected").attr("value") + "," + $('#item_name').val() + "," + 
        $("#item_brand").val() + ","  + $("#item_cat").val() ;
    $.post(
            "<?php echo URL;?>additem/add", 
            {item: itemstr}, 
            function(){
                //add error checking
                $('#item_name').val("");
                $('#item_brand').val("");
                $("#item_cat").val("");
                $("#success").html("Item Added");
            }

        );

}
</script>