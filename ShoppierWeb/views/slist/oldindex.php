<!--/* TODO: 
 * -shopping list items need to be store specific
 * -shopping list items need to allow dummy input (and fixes that go with it)
 * -change .clicks to .on(click) as nec.
 * -quantity & pricing tracking (DONE?)
 * -favorites?
 */-->

<div class="content">

    <h1 style="margin-top: 50px;">Shopping Lists</h1>
    <div class="well well-sm" style="width: 11em;">
    <a href="#newListModal" role="button" class ="btn btn-danger" id="create_anchor" data-toggle="modal">Create New List</a>
    </div>
    <br/>
    <div class="modal fade" id="newListModal" tabindex="-1" role="dialog" aria-labelledby="newListModal" aria-hidden="true">  
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                    <h3 id="myModalLabel">Create a Shopping List</h3>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <form action="<?php echo URL . "slist/create"?>" method="post" role="form">
                            <div class="col-md-5">
                                <div class="form-group">
                                    <label for="list_name">List Name</label>
                                    <input type="text" id="list_name" class="form-control" name="list_name" required />
                                </div>
                                <div class="form-group">
                                    <label for="store_select">Store</label>
                                    <select id="store_select" class="form-control" name="store_id" required>
                                        <?php  
                                        foreach($this->stores as $key => $value)
                                        {
                                            echo '<option value=' . $value->store_pk . ' data-address="' . $value->store_address . ', ' . 
                            $value->store_city . ' MI">'.$value->store_name . '-' . $value->store_city .'</option>';
                                        }
                                        ?>
                                    </select>
                                </div>
                                <button type="submit" class="btn">Create!</button>
                                
                                
                                   
                            </div>
                            <br />
                            <div class="col-md-5  well col-md-offset-1" id="map-canvas" style="height: 20em;"></div>
                        </form>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    <!--<form method="post" action="<?php echo URL . "slist/create"?>"><div class="input-append">
    	<input class="col-md-2" name="list_name" id="newListText" type="text" placeholder="List Name"><button class="btn" type="submit">Create New List</button>
    </div></form>-->
    <div class="tabbable">
    <ul class="nav nav-tabs">
    <?php
        
       if ($this->slists) 
       {
            $i = 1;
            foreach($this->slists as $key => $value) 
            {
                
                if($value->list_pk == $this->active)
                {
                    echo '<li class="active tab" id="'. $value->list_pk. '">'; 
                }
                else
                {
                    echo '<li class="tab" id="'. $value->list_pk. '">';
                }
                echo '<a href="#tab'. ($i) .'" data-toggle="tab">' . $value->list_name . '</a></li>';
                $i=$i+1;
            }
            
        }
    ?>
    </ul>
    <div class="tab-content">
    <?php
       $i = 1;

        foreach($this->slists as $key => $value)
        {
            $items = json_decode($value->list_items);
            //if($i == 1)
            if($value->list_pk == $this->active)
            {
                echo '<div class="tab-pane active" id="tab' .$i. '">';
            }
            else
            {
                echo '<div class="tab-pane" id="tab' . $i . '">';
            }
            
            echo '<table class="table table-hover itemTable" id="table' .$value->list_pk .'">';
            echo '<thead><tr><td></td><td>Quantity</td><td>Price</td><td>Brand</td><td>Item</td><td><a href="' . URL . 'slist/delete/' . $value->list_pk .'" class="btn btn-danger">Delete This List</a></td></tr></thead>';
            echo '<tbody>';
            foreach($items as $item)
            {
            	echo '<tr id="itempk'.$item[1].'"><td><input type="checkbox" class="update-option enable-item"';
		echo ($item[0] == "true" ? 'checked':'');
            	echo '></td><td class="col-md-1"><input type="number" class="form-control update-option quantity" style="width:75px" min="0" value="'.$item[4].'"></td><td class="col-md-1"><div class="input-group"><label class="input-group-addon">$</label><input type="number" class="form-control update-option price" style="width:75px" min="0" step="0.01" value="'.$item[5].'"></div></td><td>'.$item[3].'</td><td>'.$item[2].'</td><td><button class="btn deleteItem">Delete</button></td></tr>';
	    }
            echo '</tbody>';
            echo '</table>';
            echo '</div>';
            $i=$i+1;
                        	

        }
        echo '<button class="btn" id="testerbtn1">Sync</button>';
    ?>
    </div>
    </div>

    <br/>
    <hr/>
    <br/>

    <form method="post" onSubmit="return addItemToList()">
        <label>Item to add to list: </label>
        <select id="items">
        	<?php
        		$this->items= json_decode($this->items);
        		if ($this->items)
        		{
        			foreach($this->items as $key => $value) {
        				echo '<option value=' . $value->item_pk . '">' . $value->item_name .' - '. $value->item_brand .'</option>';
        			}
        		}
        	?>
        </select>
        <button type="submit" class="btn btn-info">Add Item</button>
    </form>
    
    <form method="post" class="" onSubmit="">
        <label >WORK IN PROGRESS SUPERADD:</label>
        <input id="itemSearch" class="">
        <button type="submit" class="btn btn-info">Add Item</button>
    </form>

    <button class="btn" id="route" href="">Route your List</button>
</div>

<script>

var list;
var hyperlink;
var listValues = [];

$(document).ready(function()
{
        $("#items").select2();
	
    	// Initial click events to properly initiate the onclick functions.
        $("li.active.tab").click();
        $(".enable-item").click().click();
        
	$itemsearch = <?php 
	
        		//$this->items = json_decode($this->items);
        		$i = sizeof($this->items)-1;
        		if ($i != 0)
        		{
        			echo '[';
        			foreach($this->items as $key => $value) {
        				echo '["' . $value->item_pk . '","' . $value->item_name .'","'. $value->item_brand .'"]';
        				echo ',';
        			}
        			echo '["0","test","item"]]';
        		}
        	?>;
        
        
        	
        //console.log($itemsearch);
        
        $("#itemSearch").typeahead({
        	local: $itemsearch,
        	limit: 10
       });
});


/* Checkbox Item Table - Opacity Code */
$('.itemTable tbody').on('click', '.enable-item', function() {
	var row = $(this).parent().parent();
	if($(this).is(":checked")) {
		row.animate({ opacity: 0.5});
	} else {
		row.animate({ opacity: 1});
	}
});
 
 $(".setactive").click(function(){
    list = $(this).attr("id");
    console.log(list);
    $.post("<?php echo URL;?>slist/activeList/" + list, function(data){console.log(data);});
 });
      
 $("li.tab").click(function() {
            list = $(this).attr("id");
            //console.log(hyperlink);
            $(".route").attr("href", "<?php echo URL?>route/index/" + list);
    });
    
    
function createList()
{
	$.post(
		"<?php echo URL;?>slist/create",
		{list_name: $('#newListText').val()},
		function() {
			$("#success").html("New list created.");
		}
	);
}


function addItemToList()
{
	var table = "#table" + list + " tbody";
	
	$.post(
		"<?php echo URL;?>slist/addToList/"+list + "/" + $('#items option:selected').val(),
		function(data) {
            		var item = JSON.parse(data);
            		console.log(item);
			$(table).append('<tr id="itempk'+item[1]+'"><td><input type="checkbox" class="update-option enable-item" value="'+item[0]+'"></td><td class="col-md-1"><input type="number" class="form-control update-option quantity" style="width:75px" min="0" value="'+item[4]+'"></td><td class="col-md-1"><div class="input-group"><label class="input-group-addon">$</label><input type="number" class="form-control update-option price" style="width:75px" min="0" step="0.01" value="'+item[5]+'"></div></td><td>'+item[3]+'</td><td>'+item[2]+'</td><td><button class="btn deleteItem">Delete</button></td></tr>');
    });
	return false;
}

$(".deleteItem").click(function() {
	// Fetch itempk from the table row and store just the primary key
	var itempk = $(this).parent().parent().attr("id").replace("itempk","");
	console.log("listid:"+list+" itempk:"+itempk);
	
	$.post(
		"<?php echo URL;?>slist/deleteFromList/" + list + "/" + itempk,
		function(data) {
			$("#itempk"+itempk).attr("style", "display: none;");
		}
	);
});

$(".itemTable tbody").on("blur", ".update-option", function() {
	var updateType = this.className;
	var itempk;
	var checked, quantity, price;
	
	// Determine which field was called and grab 
	if(updateType.indexOf('enable-item') !== -1) {
		console.log("checker");
		itempk = $(this).parent().parent().attr("id").replace("itempk","");
		checked = $(this).is(":checked");
		quantity = $(this).parent().parent().find(".quantity").val();
		price = $(this).parent().parent().find(".price").val();
	} else if(updateType.indexOf('quantity') !== -1) {
		console.log("qty");
		itempk = $(this).parent().parent().attr("id").replace("itempk","");
		checked = $(this).parent().parent().find(".enable-item").is(":checked");
		quantity = $(this).val();
		price = $(this).parent().parent().find(".price").val();
	} else if(updateType.indexOf('price') !== -1) {
		console.log("price");
		itempk = $(this).parent().parent().parent().attr("id").replace("itempk","");
		checked = $(this).parent().parent().parent().find(".enable-item").is(":checked");
		quantity = $(this).parent().parent().parent().find(".quantity").val();
		price = $(this).val();
	}
	
	listValues.push([list,itempk,checked,quantity,price]);
});


function setActiveList()
{
   console.log("new active list: "+list);
}


window.onbeforeunload = function(){
    console.log("unloading"+list);
    //$.post("<?php echo URL;?>slist/activelist/" + list, function(data){console.log(data);});
     $.ajax({
        type:"POST",
        url: "<?php echo URL;?>slist/activelist/" + list,
        success: function(data){
            console.log(data);
        },
    async: false
    });
    
    	$.ajax({
		type: "POST",
		url: "<?php echo URL;?>slist/updateList/",
		data: {list_values: listValues},
    		async: false
	}).done(function() {
		console.log(listValues);
	});
};

/* Google Maps Location Tracking */

var geocoder;
var map;
var marker
var init = 0;

function initialize() {
  geocoder = new google.maps.Geocoder();
  var latlng = new google.maps.LatLng(42.9612, 85.6557);
  var mapOptions = {
    zoom: 15,
    center: latlng
  }
  map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
}

$("#newListModal").on('shown.bs.modal', function()
{
	google.maps.event.trigger(map, 'resize');
	codeAddress($("#store_select option:selected").attr("data-address"));
});

$("#create_anchor").click(function()
{
	if( init == 0)
	{
		initialize();
		init = init + 1;
	}
});

$("#store_select").change(function(){
	codeAddress($("#store_select option:selected").attr("data-address"));
}); 

//centers the google map on an address
function codeAddress(address) {
  geocoder.geocode( { 'address': address}, function(results, status) {
    if (status == google.maps.GeocoderStatus.OK) {
      map.setCenter(results[0].geometry.location);
      
      if(marker) {
      	marker.setPosition(results[0].geometry.location);
      } else {
        marker = new google.maps.Marker({
          map: map,
          position: results[0].geometry.location
      	});
      }
    } else {
      alert('Geocode was not successful for the following reason: ' + status);
    }
  });
}
</script>