<!doctype html>
<html>
<head>
	<title>Shoppier</title>
    	<link rel="stylesheet" href="<?php echo URL; ?>public/css/reset.css" />
	<link rel="stylesheet" href="<?php echo URL; ?>public/css/default.css" />
	<link rel="stylesheet" href="<?php echo URL; ?>public/css/select2.css" />
	<script type="text/javascript" src="<?php echo URL; ?>public/js/jquery-1.10.1.min.js"></script>
	<script type="text/javascript" src="<?php echo URL; ?>public/js/custom.js"></script>
	<script type="text/javascript" src="<?php echo URL; ?>public/js/select2.min.js"></script>
</head>
<body>
    
    <!--DEBUG HELPER. Uncomment to show information about current pages view
    <div class="debug-helper-box">
        DEBUG HELPER: you are in the view: <?php echo $filename; ?>
    </div>-->

    <div class='title-box'>
        <img href="<?php echo URL; ?>" src="<?php echo URL; ?>public/images/shoppier.png" alt="Shoppier" />
    </div>
    
    <div class="header">

        <div class="header_left_box">
        <ul id="menu">
            <li <?php if ($this->checkForActiveController($filename, "index")) { echo ' class="active" '; } ?> >
                <a href="<?php echo URL; ?>index/index">Index</a>
            </li>
            <li <?php if ($this->checkForActiveController($filename, "help")) { echo ' class="active" '; } ?> >
                <a href="<?php echo URL; ?>help/index">Help</a>
            </li>
            <li <?php if ($this->checkForActiveController($filename, "overview")) { echo ' class="active" '; } ?> >
                <a href="<?php echo URL; ?>overview/index">Overview</a>
            </li>            
            <?php if (Session::get('user_logged_in') == true):?>
            <li <?php if ($this->checkForActiveController($filename, "dashboard")) { echo ' class="active" '; } ?> >
                <a href="<?php echo URL; ?>dashboard/index">Dashboard</a>	
            </li>   
            <?php endif; ?>                    
            <?php if (Session::get('user_logged_in') == true):?>
            <li <?php if ($this->checkForActiveController($filename, "slist")) { echo ' class="active" '; } ?> >
                <a href="<?php echo URL; ?>slist/index">Shopping Lists</a>
            </li>
            <li <?php if ($this->checkForActiveController($filename, "additem")) { echo ' class="active" '; } ?> >
                <a href="<?php echo URL; ?>additem/index">Add Item</a>
            </li>
            <?php endif; ?>                    


            <?php if (Session::get('user_logged_in') == true):?>
                <li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="active" '; } ?> >
                    <!--<a href="#">My Account</a>-->
                    <a href="<?php echo URL; ?>login/showprofile">My Account</a>
                    <ul class="sub-menu">
                        <!--
                        <li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="active" '; } ?> >
                            <a href="<?php echo URL; ?>login/showprofile">Show my profile</a>
                        </li>
                        -->
                        <li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="active" '; } ?> >
                            <a href="<?php echo URL; ?>login/changeaccounttype">Change account type</a>
                        </li>                           
                        <li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="active" '; } ?> >
                            <a href="<?php echo URL; ?>login/uploadavatar">Upload an avatar</a>
                        </li>                          
                        <li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="active" '; } ?> >
                            <a href="<?php echo URL; ?>login/editusername">Edit my username</a>
                        </li>
                        <li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="active" '; } ?> >
                            <a href="<?php echo URL; ?>login/edituseremail">Edit my email</a>
                        </li>
                        <li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="active" '; } ?> >
                            <a href="<?php echo URL; ?>login/logout">Logout</a>
                        </li>
                    </ul>
                </li>
            <?php endif; ?>          

            <!-- for not logged in users -->
            <?php if (Session::get('user_logged_in') == false):?>

                <li <?php if ($this->checkForActiveControllerAndAction($filename, "login/index")) { echo ' class="active" '; } ?> >
                    <a href="<?php echo URL; ?>login/index">Login</a>
                </li>  
                <li <?php if ($this->checkForActiveControllerAndAction($filename, "login/register")) { echo ' class="active" '; } ?> >
                    <a href="<?php echo URL; ?>login/register">Register</a>
                </li>         
                <li <?php if ($this->checkForActiveControllerAndAction($filename, "login/requestpasswordreset")) { echo ' class="active" '; } ?> >
                    <a href="<?php echo URL; ?>login/requestpasswordreset">Forgot my Password</a>
                </li>

            <?php endif; ?>

        </ul>   
        </div>

        <?php if (Session::get('user_logged_in') == true): ?>
            <div class="header_right_box">
                
                <div class="namebox">
                    Hello <?php echo Session::get('user_name'); ?> !
                </div>
                
                <div class="avatar">
                    <?php if (USE_GRAVATARS) { ?>
                        <img src='<?php echo Session::get('user_gravatar_image_url'); ?>' />
                    <?php } else { ?>
                        <img src='<?php echo Session::get('user_avatar_file'); ?>' />
                    <?php } ?>
                </div>                

            </div>
        <?php endif; ?>

        <div class="clear-both"></div>

    </div>	
	