
<?php

            $items = json_decode($this->list);
            echo '<table class="table table-hover itemTable">';
            echo '<thead><tr><td></td><td>Quantity</td><td>Price</td><td>Brand</td><td>Item</td><td><a href="' . URL . 'slist/delete/' . $this->route .'" class="btn btn-danger">Delete This List</a></td></tr></thead>';
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

?>