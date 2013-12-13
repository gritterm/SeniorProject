<?php
 
class routing{

 public function __construct()
    {
        include '/home1/jonnykle/public_html/shoppier/models/libPathfinding.php';
        include '/home1/jonnykle/public_html/shoppier/models/libNodeGraph.2D.php';
        include '/home1/jonnykle/public_html/shoppier/models/routing_functions.php';
    }
    
    function routeList($list){
    	 $route = getList($list);
    	 $finalList = array('routelist' =>  json_decode($route ));
    	 return $finalList ; 
    }
}
 
?>