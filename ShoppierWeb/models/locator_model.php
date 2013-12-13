<?php

class Locator_Model extends Model
{
    public $errors = array();
    
    public function __construct()
    {
        parent::__construct();
    }

	public function getAllItems()
    {
    	$sth = $this->db->prepare("SELECT item_pk, item_name, item_brand
    				   FROM item;");
		$sth->execute();    
                
                return json_encode($sth->fetchAll());
    }
    public function drawLocation($item)
    {
    	$sth = $this->db->prepare("SELECT item_pk, item_name, aisle_name, cat_name, cat_x, cat_y FROM
    								item  Inner Join category on item_catfk = cat_pk inner join 
    								aisle on cat_locfk = aisle_pk where item_pk
    								= :item
    							     ");
    	$sth->execute(array(':item' => $item));
    	return json_encode($sth->fetchAll());
    	//should only execute once
    	
    	
    }

}
