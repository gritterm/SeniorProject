<?php
    /*foreach($this->edges as $edge)
    {
        echo '<tr><td>' . $edge['loc1'] . '</td><td>' .$edge['loc2']. '</td><td>' .$edge['dist'] . '</td></tr>';
    }*/
    $items = json_decode($this->list);
    $i = 0;
    echo '<div class="tab-pane" id="tab' . $i . '">';
     echo '<table class="table table-hover">';
            echo '<thead><tr><td>Check</td><td>Quantity</td><td>Price</td><td>Brand</td><td>Item</td><td></td></tr></thead>';
            echo '<tbody>';
            foreach($items as $item)
            {
                echo '<tr><td><input type="checkbox" class="enableItem"></td><td><input type="number" class="span1" min="0"></td><td><input type="number" class="span1" min="0" step="0.01"></td><td>'.$item[2].'</td><td>'.$item[1].'</td><td><button class="btn">Delete</button></td>';  
        echo '</tr>';
        }
            echo '</tbody>';
            echo '</table>';
            echo '</div>';


/*function merge_sort(&$list){
    if( count($list) <= 1 ){
        return $list;
    }

    $left =  array();
    $right = array();

    $middle = (int) ( count($list)/2 );

    // Make left
    for( $i=0; $i < $middle; $i++ ){
        $left[] = $list[$i];
    }

    // Make right
    for( $i = $middle; $i < count($list); $i++ ){
        $right[] = $list[$i];
    }

    // Merge sort left & right
    merge_sort($left);
    merge_sort($right);

    // Merge left & right
    return merge($left, $right);
}


<p> sup </p>*/
?>