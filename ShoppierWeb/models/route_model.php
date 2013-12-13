<?php
include 'libPathfinding.php';
include 'libNodeGraph.2D.php';
include 'routing_functions.php';
class Route_Model extends Model
{

    public $errors = array();
    public function __construct()
    {
        parent::__construct();
    }
    function getOrderedList($list)
    {	
    	$sth = $this->db->prepare("SELECT * FROM list where list_pk = :list;");
    	$sth->execute(array(':list'=>$list));
        $listarr = json_decode($sth->fetch()->list_items);
    	return getList($listarr);
    }
    

}
?>