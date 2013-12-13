<?php
 
class DB_Functions {
 
    private $db;
   
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        require_once '/home1/jonnykle/public_html/shoppier/libs/external/PasswordCompatibilityLibrary.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO users(unique_id, user_name, user_email, user_password_hash) VALUES('$uuid', '$name', '$email', '$encrypted_password'");
        // check for successful store
        if ($result) {
            // get user details 
            $uid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM users WHERE uid = $uid");
            // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
  public function getUserByEmailAndPassword($username, $password) {
	
        $result = mysql_query("SELECT * FROM users WHERE user_name = '$username'") or die(mysql_error());
        
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
           
         if (password_verify($password,  $result['user_password_hash'])){
                return $result;
            } 
        } else {
            // user not found
            return false;
        }
    }
    
    
  public function getUsersGrocList($UID){
  
  	
  	$miniLists = array();
  	$listItems= array();
  	$finalList= array();

  	$result = mysql_query("SELECT * FROM list WHERE list_userfk = '$UID'") or die(mysql_error());
  	$no_of_rows = mysql_num_rows($result);
  	//file_put_contents("/home1/jonnykle/public_html/shoppier/Android_DB_API/output.txt",$UID);
  	if ($no_of_rows > 0) {
  	  
  	   while ($row = mysql_fetch_array($result)) {

		$jsonString = $row['list_items'];
		
		$listFK = $row['list_pk'];
  	   	$listItem2 =json_decode($jsonString);
  	   	foreach($listItem2 as $listItem){
  	   		$item_checked = $listItem[0];
	  	   	$list_ID = $listItem[1];
	  	   	$list_text = $listItem[2];
	  	   	$list_Item_Brand = $listItem[3];
	  	   	$item_price= $listItem[5];
	  	   	$item_qty = $listItem[4];
	  	   	$item_catVal = $listItem[6];
	  	   	$item_x = $listItem[7];
	  	   	$item_y = $listItem[8];
	  	   		if($item_x < 0 or $item_x == NULL){
	  	   	    		$item_x = 0; 
	  	   	    	}else {
	  	   	    		$item_x = $listItem[7];
	  	   	    	}


	  	   		if($item_y < 0 or $item_y == NULL){
	  	   	    		$item_y = 0; 
	  	   	    	}else {
	  	   	    		$item_y = $listItem[8];
	  	   	    	}
	  	   	array_push($listItems, array('list_Search_item_id' => $list_ID, 'list_item_text' => $list_text, 'list_Item_brand'
	  	   	 => $list_Item_Brand , 'item_price' => $item_price, 'list_fk' => $listFK,
	  	   	 'item_qty' => $item_qty, 'item_catVal' => $item_catVal , 'item_x' => $item_x , 'item_y' => $item_y, 'checked' => $item_checked ));
	  	   	 //file_put_contents("/home1/jonnykle/public_html/shoppier/Android_DB_API/output.txt",print_r($listItems, true), FILE_APPEND);
  	   	}
  	     }
  	     
  	    $finalList = array('list' =>  $listItems);
  	    
  	    return $finalList ;
  	} else {
  	
  	
  		return false; 
  	}
  	
  }
  
  public function getSearchableItems(){
	
	$listItems= array();
  	$finalList= array();
  	
  	$result = mysql_query("SELECT * FROM item") or die(mysql_error());
  	$no_of_rows = mysql_num_rows($result);
  	if ($no_of_rows > 0) {
  	  
  	   while ($row = mysql_fetch_array($result)) {
  	   	$item_pk = $row["item_pk"]; 
  	   	$item_name = $row["item_name"]; 
  	   	$item_brand = $row["item_brand"]; 
  	   	$item_cat = $row["item_catfk"]; 
		
  	   	array_push($listItems, array('item_pk' => $item_pk, 'item_name' => $item_name,
  	   		'item_brand' => $item_brand, 'item_cat' => $item_cat));
  	     }
  	     
  	    $finalList = array('items' =>  $listItems);
  	    return $finalList ;
  	} else {
  	
  		return false; 
  	}
  
  }
  
  public function syncList($LISTPK, $newList, $UID){
  
  	//file_put_contents("/home1/jonnykle/public_html/shoppier/Android_DB_API/output.txt",print_r($newList, true));

 
  	//file_put_contents("/home1/jonnykle/public_html/shoppier/Android_DB_API/output.txt",print_r($inputList, true));

  	$result = mysql_query("UPDATE list SET list_items = '$newList' WHERE list_pk = '$LISTPK' AND list_userfk = '$UID'");
  }
  
  public function addList($list_items, $UID, $list_name, $store_fk){
  
  	 mysql_query("INSERT INTO list (list_items, list_userfk, list_name, list_storefk)
  	 		VALUES ('$list_items', '$UID', '$list_name','$store_fk')");
  
  }
  
  //will return a json array of users list IDS and list name
  public function getListIDs($UID){
  	$finalListID = array();
  	$listsID = array();
  	
  	$result = mysql_query("SELECT list_pk, list_name FROM list WHERE list_userfk = '$UID'") or die(mysql_error());
  	$no_of_rows = mysql_num_rows($result);
  	
  	if ($no_of_rows > 0) {
  	  
  	   while ($row = mysql_fetch_array($result)) {
  	   	$list_pk= $row['list_pk']; 
  	   	$list_name = $row['list_name'];
  	   	array_push($listsID , array('list_pk' => $list_pk, 'list_name' => $list_name));
  	     }
  	     
  	    $finalListID = array('listIds' =>  $listsID );
  	    return $finalListID ;
  	} else {
  	
  		return false; 
  	}
  }
  
  public function sentCrowdSourcedItem($item_name, $item_brand, $item_catFK){
  
  	$results = mysql_query("INSERT INTO item(item_name, item_brand, item_catfk) VALUES('$item_name', '$item_brand', '$item_catFK')");

  	//file_put_contents("/home1/jonnykle/public_html/shoppier/Android_DB_API/output.txt", $results );
  	if ($results) {
            return true;
        } else {
            return false;
        }

  }
  
public function getStores(){
	
  	$stores = array();
  	$aisle = array();
  	$category = array();
  	
  	$finalListStore = array();
  	$finalListAisle = array();
  	$finalListCategory = array();
  	
  	$finalList = array();
  	
  	$result = mysql_query("SELECT * FROM store") or die(mysql_error());
  	$resultAisle = mysql_query("SELECT * FROM aisle") or die(mysql_error());
  	$resultCategory = mysql_query("SELECT * FROM category") or die(mysql_error());
  	
  	$no_of_rows = mysql_num_rows($result);
  	$no_of_rows_aisle = mysql_num_rows($resultAisle);
  	$no_of_rows_cat = mysql_num_rows($resultCategory);
  	
  	if ($no_of_rows > 0) {
  	  
  	   while ($row = mysql_fetch_array($result)) {
  	   	$store_pk = $row['store_pk'];
		$store_name = $row['store_name'];
		$store_address = $row['store_address'];
		$store_city = $row['store_city'];
		$store_zipcode = $row['store_zipcode'];
		$store_type = $row['store_type'];
		$store_image = $row['store_image'];
  	   	array_push($stores , array('store_pk' => $store_pk, 'store_name' => $store_name, 'store_address ' => $store_address , 'store_city ' => $store_city , 'store_zipcode ' => $store_zipcode ,
'store_type ' => $store_type , 'store_image ' => $store_image ));
  	     }
  	     
  	    
  	 if($no_of_rows_aisle  > 0){
  	 	while ($row1 = mysql_fetch_array($resultAisle)) {
  	 	    $aisle_pk = $row1['aisle_pk'];
  	 	    $aisle_strfk = $row1['aisle_strfk'];
  	 	    $aisle_name	= $row1['aisle_name'];	
  	 	    array_push($aisle, array('aisle_pk' => $aisle_pk, 'aisle_strfk' => $aisle_strfk, 'aisle_name' => $aisle_name));
  	 	 }
  	 }
  	 
  	 if($no_of_rows_cat  > 0){
  	 	while ($row2 = mysql_fetch_array($resultCategory)) {
  	 	    $cat_pk = $row2['cat_pk'];
  	 	    $cat_locfk = $row2['cat_locfk'];
  	 	    $cat_name	= $row2['cat_name'];
  	 	    $cat_value = $row2['cat_value'];
  	 	    $cat_x = $row2['cat_x'];
  	 	    $cat_y = $row2['cat_y'];	
  	 	    array_push($category, array('cat_pk' => $cat_pk, 'cat_locfk' => $cat_locfk, 'cat_name' => $cat_name,
  	 	    'cat_value ' => $cat_value , 'cat_x ' => $cat_x , 'cat_y' => $cat_y));
  	 	 }
  	 }


  	
  	array_push($finalList, array('stores' =>  $stores ));
  	array_push($finalList, array('aisle' =>  $aisle));
  	array_push($finalList, array('cat' =>  $category));
  	 $finalListReturn = array('DropDownItem' =>  $finalList);
  	//file_put_contents("/home1/jonnykle/public_html/shoppier/Android_DB_API/output.txt",print_r($finalListReturn , true));
  	    return $finalListReturn ;
  	} else {
  	
  		return false; 
  	}
}
  
  
  
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $result = mysql_query("SELECT email from users WHERE user_email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed 
            return true;
        } else {
            // user not existed
            return false;
        }
    }
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
 
}
 
?>