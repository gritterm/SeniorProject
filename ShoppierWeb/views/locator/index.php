<div class="well well-large">
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
    <div>

    </div>
    <div id="location" class="well">
        <?php
            echo '<canvas id="locCanvas" width="500" height="1200"></canvas>';
            //echo '<img src="' . URL . 'maps/jenisonmeijer2.jpg">';
        ?>
    </div>
	

</div>
<script>
	$(document).ready(function()
    {
        //make them pretty
        $("#items").select2();
        
    });
    var canvas = document.getElementById("locCanvas");
        var context = canvas.getContext("2d"); 
        var img = new Image;
        img.src = 'http://shoppier.net/public/images/maps/meijer_map_text.png';
        img.onload = function(){
            context.drawImage(img, 0, 0);
        }
        var pin = new Image;
        pin.src = 'http://shoppier.net/public/images/maps/red_dot.png';
        pin.onload=function(){
            
        }
    function drawPin()
    {
        var item_cat = $("#items option:selected").attr("value");
        $.post(

            "<?php echo URL;?>locator/getLoc",
            {itemid: item_cat},
            function(data){
                console.log(data);
                var arr = JSON.parse(data);
                for(key in arr)
                {
                    context.drawImage(img, 0,0);
                    if(arr[key]['cat_x'] == null)
                    {
                        console.log("no loc data");
                    }
                    else
                    {  
                        
                        context.width = context.width;
                        context.clearRect ( 0, 0 , 249 ,721 );
                        context.drawImage(img, 0, 0);
                        context.drawImage(pin, arr[key]['cat_x']*18, (arr[key]['cat_y'] -8)*10+90);
                    }
                }
            }
        );

    }
    $("#items").change(function()
    {
        drawPin();
    });
</script>