<?php
/**
 * File to handle all API requests
 * Accepts GET and POST
 * 
 * Each request will be identified by TAG
 * Response will be JSON data
 
  /**
 * check for POST request 
 */
if (isset($_POST['tag']) && $_POST['tag'] != '') {

    // get tag
    $tag = $_POST['tag'];
 
    // include db handler
    require_once 'include/DB_Functions.php';
    //require_once '/home1/jonnykle/public_html/shoppier/models/route_model.php;
    require_once 'include/routing.php';
    
    $db = new DB_Functions();
    $route = new routing();
 
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
    // check for tag type
    if ($tag == 'login') {
    
        // Request type is check Login
        $username= $_POST['email'];
        $password = $_POST['password'];
 
        // check for user
        $user = $db->getUserByEmailAndPassword($username, $password);
       
  	//file_put_contents("/home1/jonnykle/public_html/shoppier/Android_DB_API/output.txt",$user );

        if ($user) {
            // user found
            // echo json with success = 1
             
            $response["success"] = 1;
            $response["uid"] = $user["user_id"];
            $response["user"]["name"] = $user["user_name"];
            $response["user"]["email"] = $user["user_email"];
            echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    } else if ($tag == 'register') {
        // Request type is Register new user
        $name = $_POST['name'];
        $email = $_POST['email'];
        $password = $_POST['password'];
 
        // check if user is already existed
        if ($db->isUserExisted($email)) {
            // user is already existed - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        } else {
            // store user
            $user = $db->storeUser($name, $email, $password);
            if ($user) {
                // user stored successfully
                $response["success"] = 1;
                $response["uid"] = $user["unique_id"];
                $response["user"]["name"] = $user["name"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["created_at"] = $user["created_at"];
                $response["user"]["updated_at"] = $user["updated_at"];
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in Registartion";
                echo json_encode($response);
            }
        }
    } else if($tag == 'getUsersList'){
 
    	 $uid= $_POST['userID'];
    	 $list = $db->getUsersGrocList($uid);  
//file_put_contents("/home1/jonnykle/public_html/shoppier/Android_DB_API/output.txt",print_r($list, true));
    	echo json_encode($list);

    } else if($tag == 'getSearchableItems'){
    	$list = $db -> getSearchableItems();
    	  //file_put_contents("/home1/jonnykle/public_html/shoppier/Android_DB_API/output.txt","hello");

    	echo json_encode($list);
    	
    }else if($tag == 'syncList'){
    	$listID = $_POST['listID'];
    	$list = $_POST['list'];
    	$UID = $_POST['userID'];
    	$return = $db->syncList($listID, $list, $UID);
    	echo json_encode($return);
    }else if($tag == 'getListIDs'){
    	$UID = $_POST['userID'];
    	
    	$return = $db->getListIDs($UID);
    	echo json_encode($return);
    }else if ($tag == 'sentCrowdSourcedItem'){
    	$item_name = $_POST['item_name'];
    	$item_brand = $_POST['item_brand'];
    	$item_catFK = $_POST['item_catFK'];
    	$return =  $db -> sentCrowdSourcedItem($item_name, $item_brand, $item_catFK);
    	if($return == true){
    	    echo json_encode('1');
    	}else{
    	    echo json_encode('0');
    	}
    }else if ($tag == 'getStores'){
    	$return = $db -> getStores();
    	echo json_encode($return);
    }else if($tag == 'newList'){
    	$UID = $_POST['UID'];
    	$list_name= $_POST['list_name'];
    	$store_fk= $_POST['store_fk'];
    	$list_items= $_POST['list_items'];
    	$return = $db->addList($list_items, $UID, $list_name, $store_fk);
    	 echo json_encode($return);
    }else if($tag == 'routeList'){
    	$UID = $_POST['userID'];
    	$list = $_POST['list'];
    	$return = $route->routeList(json_decode($list));
    	file_put_contents("/home1/jonnykle/public_html/shoppier/Android_DB_API/output.txt",print_r($list, true));
    	echo json_encode($return );
    }else{
        echo "Invalid Request";
    }
} else {
    echo "Access Denied";
}
?>