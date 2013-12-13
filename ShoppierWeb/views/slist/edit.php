<div class="content">
    
    <h1>Shopping List: (name)</h1>

    <br />

    <div class="container">
    <?php
    if ($this->slist->list_items) { 
    	$items = json_decode($this->slist->list_items);
    	echo '<table class="table table-hover">';
    	foreach($items as $item) {
                echo '<tr>';
                echo '<td> ' . $item[0] . '</td>';
                echo '<td> ' . $item[1] . '</td>';
                echo '<td> ' . $item[2] . '</td>';
                echo '<td><a href=""><i class="icon-trash"></i></a></td>';
                echo '</tr>';
        }
        echo '</table>';
    ?>
    
    </div>
    
    <br />
    
    <?php } else { ?>
    
    <p>This list is empty. Get started adding items below!</p>
    
    <?php } ?>
    
    <form method="post" onSubmit="addItemToList(); return false;">
        <label>Item to add to list: </label>
        <select id="items">
        	<?php
        		$this->items= json_decode($this->items);
        		if ($this->items)
        		{
        			foreach($this->items as $key => $value) {
        				echo '<option value=' . $value->item_pk . '>' . $value->item_name .' - '. $value->item_brand .'</option>';
        			}
        		}
        	?>
        </select>
        <button type="submit" class="btn btn-primary">Add Item</button>
    </form>
    
</div>

<script>
$(document).ready(function()
    {
        //TODO: find a better select option
        $("#items").select2();
    });
    
function addItemToList(olditems)
{
	// JSON item format: [item_id,"item_name","item_brand"]
	var list_item = '[' + $('#items').val() + ',"' + $('#items option:selected').text().replace(' - ','","') + '"]';
	
	//if(<?php echo $this->slist->list_items; ?> != '') {
	//	console.log("list already has items");
		var list_old = JSON.stringify(<?php echo ($this->slist->list_items); ?>);
		list_old = list_old.substr(0,list_old.length-1);
		var list_new = list_old + "," + list_item + "]"
	//} else {
	//	console.log("this is the first item in list");
	//	var list_new = "[" + list_item + "]";
	//}
	console.log(list_new);
	list_new = $.parseJSON(list_new);
	
	$.post(
		"<?php echo URL;?>slist/addToList/<?php echo $this->slist->list_pk; ?>",
		{list_items: list_new},
		function() {
			$("#success").html("Item added to list.");
		});
		
	//TODO: check into a better solution then setTimeout()
	setTimeout('pagerefresh()',1);
}

function pagerefresh()
{
	window.location.reload();
}
</script>   