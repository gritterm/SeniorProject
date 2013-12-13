<?php

class SList_Model extends Model
{
    public $errors = array();
    
    public function __construct()
    {
        parent::__construct();
    }
    public function getAllStores()
    {
        $sth = $this->db->prepare("SELECT *
                                    FROM store;");
        $sth->execute();
        return $sth->fetchAll();
    }
    public function getAllLists()
    {
	$sth = $this->db->prepare("SELECT *
                                   FROM list
                                   WHERE list_userfk = :user_id ;");
	$sth->execute(array(':user_id' => $_SESSION['user_id']));    
        
        return $sth->fetchAll();
    }
    
    public function getAllItems()
    {
    	$sth = $this->db->prepare("SELECT item_pk, item_name, item_brand
    				   FROM item;");
	$sth->execute();    
        
        return json_encode($sth->fetchAll());
    }
    
    public function getItemsPerStore($list_id)
    {
    	$sth = $this->db->prepare("SELECT list_storefk
    				FROM list
    				WHERE list_pk = :list_id;");
    	$sth -> execute(array(':list_id' => $list_id));
    	
    	// Fetch the row object and traverse to find the store_id value
    	$store_id = $sth->fetch();
    	foreach($store_id as $store)
        {
            $val = $store[0];
        }
    	
    	$rth = $this->db->prepare("SELECT item_name as name, item_brand as brand, item_pk as value
				FROM item
				INNER JOIN category ON item_catfk = cat_pk
				INNER JOIN aisle ON cat_locfk = aisle_pk
				INNER JOIN store ON aisle_strfk = store_pk
				WHERE store_pk = :list_storefk;");
	$rth -> execute(array(':list_storefk' => $val));

	$items_before = $rth->fetchAll();
	$items_after = $items_before;
    	$newarr = array();
	$i = 0;
	foreach($items_before as $key => $value)
	{
		$newarr[$i]['name']  = $value->name;
        	$newarr[$i]['brand'] = $value->brand;
        	$newarr[$i]['value'] = $value->value;
        	$newarr[$i]['tokens'] = array();
        	//push stuff into this array here
	        $strings = explode(" ", $value->name);
        	foreach($strings as $word)
        	{
            		array_push($newarr[$i]['tokens'], $word);
        	}
        	$i = $i + 1;
	}

	return json_encode($newarr);
    }
    
    public function getList($list_id)
    {
        $sth = $this->db->prepare("SELECT *
        			   FROM list
        			   WHERE list_userfk = :user_id AND list_pk = :list_id;");
        $sth->execute(array(
            ':user_id' => $_SESSION['user_id'],
            ':list_id' => $list_id));

        return $sth->fetch();
    }
    
    
    public function create($list_name, $store_id)
    {
        $sth = $this->db->prepare("INSERT INTO list
                                   (list_items, list_userfk, list_routefk, list_name, list_storefk)
                                   VALUES (:list_items, :user_id, 0, :list_name, :list_storefk);");
        $sth->execute(array(
            ':list_items' => "[]",
            ':user_id' => $_SESSION['user_id'],
            ':list_name' => $list_name,
            ':list_storefk' => $store_id));
        /*
        $count =  $sth->rowCount();
        if ($count == 1) {
            return true;
        } else {
            $this->errors[] = FEEDBACK_LIST_CREATION_FAILED;
            return false;
        }*/
    }
    
     /** Adds an item to a shopping list from items available in the database.
      * Returns a JSON encoded string including the new item info.              
      */
     public function addToList($list_id, $item_pk)
     {
        $sth = $this->db->prepare("SELECT 'false' as checked, item_pk, item_name, item_brand, 1 as quantity, 0.00 as price,
                                          cat_pk, cat_x, cat_y 
                                   FROM item
                                   INNER JOIN category on cat_pk = item_catfk
                                   WHERE item_pk = :item;");
        $sth->execute(array(
            ':item' => $item_pk
            ));
        $itemrow = $sth->fetch();

        $rth = $this->db->prepare("SELECT *
                                   FROM list
                                   WHERE list_pk = :list;");
        $rth->execute(array(
            ':list' => $list_id
            ));
        $listrow = $rth->fetch();
        $json = $listrow->list_items;
        $list = json_decode($json);
        if(count($list)<1)
          $list = array();
        $newitem = array();
        foreach($itemrow as $column)
        {
          array_push($newitem, $column);
        }
        $newitemjson = json_encode($newitem);
        array_push($list, $newitem);
        $listjson = json_encode($list);
        $qth = $this->db->prepare("UPDATE list SET list_items = :list WHERE list_pk = :listpk;");
        $qth->execute(array(
            ':list' => $listjson,
            ':listpk' => $list_id
          ));
        return $newitemjson;
    }
    
    /** Adds an item to a shopping list from item name only (dummy item).
     * Returns a JSON encoded string including the new item info.              
     */
    public function addSimpleToList($list_id,$item_name)
    {
    	$rth = $this->db->prepare("SELECT *
                                   FROM list
                                   WHERE list_pk = :list;");
        $rth->execute(array(
            ':list' => $list_id
            ));
	$listrow = $rth->fetch();
        $json = $listrow->list_items;
        $list = json_decode($json);
        if(count($list)<1)
          $list = array();

        $newitem = ["false","0",$item_name," ","1","0.00","0","0","0"];
        
        $newitemjson = json_encode($newitem);
        
        array_push($list, $newitem);
        $listjson = json_encode($list);
        $qth = $this->db->prepare("UPDATE list
        			SET list_items = :list
        			WHERE list_pk = :listpk;");
        $qth->execute(array(
            ':list' => $listjson,
            ':listpk' => $list_id
        ));
        return $newitemjson ;
    }
    
    public function deleteFromList($list_id, $item_pk, $item_name)
    {
        $sth = $this->db->prepare("SELECT * 
                                   FROM list
                                   WHERE list_pk = :list;");
        $sth->execute(array(
            ':list' => $list_id
            ));
        $listjson = $sth->fetch()->list_items;
        $list = json_decode($listjson);
        $count = 0;
        foreach($list as $row)
        {
          if($row[1] == $item_pk) {
            if(strcmp($row[2], $item_name) == 0) {
            	array_splice($list, $count, count($list), array_slice($list, $count+1));
            }
          } else {
            $count = $count + 1;
          }
        }
        $newlist = json_encode($list);
        $qth = $this->db->prepare("UPDATE list SET list_items = :list WHERE list_pk = :listpk AND list_userfk = :user_id;");
        $qth->execute(array(
            ':list' => $newlist,
            ':listpk' => $list_id,
            ':user_id' => $_SESSION['user_id']
        ));
    }
    
    public function updateList($list_values)
    {
    	
        foreach($list_values as $list)
        {
        	$list_id = $list[0];
        	$item_pk = $list[1];
        	$item_name = $list[2];
	        $sth = $this->db->prepare("SELECT * 
	                                   FROM list
	                                   WHERE list_pk = :listpk;");
	        $sth->execute(array(
	            ':listpk' => $list_id
	            ));          
	            
	        $oldlistjson = $sth->fetch()->list_items;
	        $oldlist = json_decode($oldlistjson);
		$count = 0;
	        for($i=0; $i<sizeof($oldlist); $i++)
	        {
	          if($oldlist[$i][1] == $item_pk && strcmp($oldlist[$i][2],$item_name) == 0) {
	            // Checkbox boolean value
	            $oldlist[$i][0] = $list[3];
	            // Quantity
	            $oldlist[$i][4] = $list[4];
	            // Price
	            $oldlist[$i][5] = $list[5];
	          }
	        }
	        $newlist = json_encode($oldlist);
	        $qth = $this->db->prepare("UPDATE list
	        			   SET list_items = :list
	        			   WHERE list_pk = :listpk AND list_userfk = :user_id;");
	        $qth->execute(array(
	            ':list' => $newlist,
	            ':listpk' => $list_id,
	            ':user_id' => $_SESSION['user_id']
	        ));
	}
    }
    
    public function editSave($list_id, $list_text)
    {
                
        $sth = $this->db->prepare("UPDATE list 
                                   SET list_text = :list_text
                                   WHERE list_id = :list_id AND user_id = :user_id;");
        $sth->execute(array(
            ':list_id' => $list_id,
            ':list_text' => $list_text,
            ':user_id' => $_SESSION['user_id']));   
        
        $count =  $sth->rowCount();
        if ($count == 1) {
            return true;
        } else {
            $this->errors[] = FEEDBACK_LIST_EDITING_FAILED;
            return false;
        }                
                
                
    }
    public function setActiveList($active_list)
    {
        $user = $_SESSION['user_id'];
        $sth = $this->db->prepare("UPDATE users SET users_activelist = :activelist WHERE user_id = :user;");
        $sth->execute(array(
            ':activelist' => $active_list,
            ':user' => $user));
        Session::set('active_list', $active_list);
        return true;
    }
    public function getFirstList()
    {
        $sth = $this->db->prepare("SELECT *
                                 FROM list
                                           WHERE list_userfk = :user_id ;");
        $sth->execute(array(':user_id' => $_SESSION['user_id']));    
        $row = $sth->fetch();
        return $row->list_pk;
    }
    public function getUserActive()
    {
        $user = $_SESSION['user_id'];
        $sth = $this->db->prepare("SELECT users_activelist FROM users where user_id = :user;");
        $sth->execute(array(
            ':user' =>$user
        ));
        $row = $sth->fetch();
        $active = $row->users_activelist;
        if($active == null)
            $active = $this->getFirstList();
        
        Session::set('active_list', $active);
        return $active;
    }
    public function delete($list_id)
    {
        $sth = $this->db->prepare("DELETE FROM list 
                                   WHERE list_pk = :list_id AND list_userfk = :user_id;");
        $sth->execute(array(
            ':list_id' => $list_id,
            ':user_id' => $_SESSION['user_id']));   
        
        $count =  $sth->rowCount();
        
        $newactive = $this->getUserActive();
        $this->setActiveList($newactive);

        if ($count == 1) {
            return true;
        } else {
            $this->errors[] = FEEDBACK_LIST_DELETION_FAILED;
            return false;
        }
    }
}