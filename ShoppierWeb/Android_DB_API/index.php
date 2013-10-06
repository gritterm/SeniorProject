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
   // require_once 'include/DB_Functions.php';
   // $db = new DB_Functions();
    
  
	
    //include login model
   // require_once 'public_html/shoppier/libs/*';
    require_once '/home1/jonnykle/public_html/shoppier/libs/Database.php';
    require_once '/home1/jonnykle/public_html/shoppier/libs/Model.php';
    require_once '/home1/jonnykle/public_html/shoppier/models/login_model.php';
    $lm = new login_model();
 
 
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
    // check for tag type
    if ($tag == 'login') {
        // Request type is check Login
        $username= $_POST['email'];
        $password = $_POST['password'];
 
        // check for user
       // $user = $db->getUserByEmailAndPassword($email, $password);
        
        
        //check for login sucess 
        $sucess = $lm->login($username, $password);
        
        file_put_contents("/home1/jonnykle/public_html/shoppier/Android_DB_API/sucess.txt", "Sucess = ".$sucess);
       
  
        if ($sucess != false) {
            // user found
            // echo json with success = 1
             
            $response["success"] = 1;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["user_name"];
            $response["user"]["email"] = $user["user_email"];
           // $response["user"]["created_at"] = $user["created_at"];
          //  $response["user"]["updated_at"] = $user["updated_at"];
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
    } else {
        echo "Invalid Request";
    }
} else {
    echo "Access Denied";
}
?>