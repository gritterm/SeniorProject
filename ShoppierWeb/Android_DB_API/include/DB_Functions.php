<?php
 
class DB_Functions {
 
    private $db;
   
    
    //put your code here
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

  	$result = mysql_query("SELECT list_pk, list_items, list_routefk FROM list WHERE list_userfk = '$UID'") or die(mysql_error());
  	$no_of_rows = mysql_num_rows($result);
  	if ($no_of_rows > 0) {
  	  
  	   while ($row = mysql_fetch_array($result)) {

		$jsonString = $row['list_items'];
		
		$listFK = $row['list_pk'];
  	   	$listItem2 =json_decode($jsonString);
  	   	foreach($listItem2 as $listItem){
	  	   	$list_ID = $listItem[0];
	  	   	$list_text = $listItem[1];
	  	   	$list_Item_Brand = $listItem[2];
	  	   	$list_routefk = 0;
	  	   	array_push($listItems, array('list_Search_item_id' => $list_ID, 'list_item_text' => $list_text,'list_Item_brand'
	  	   	 => $list_Item_Brand , 'list_routefk' => $list_routefk, 'list_fk' => $listFK));
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