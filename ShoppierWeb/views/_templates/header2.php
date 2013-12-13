<!doctype html>
<html lang="en">
<head>
	<title>Shoppier</title>
    <script type="text/javascript" src="<?php echo URL; ?>public/js/jquery-1.10.1.min.js"></script>
    <script src="<?php echo URL; ?>public/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="<?php echo URL; ?>public/bootstrap/css/bootstrap.css" type="text/css"/>
    <link rel="stylesheet" href="<?php echo URL; ?>public/bootstrap/css/bootstrap.min.css" type="text/css"/>
    <link rel="stylesheet" href="<?php echo URL; ?>public/bootstrap/css/bootstrap-responsive.min.css" type="text/css"/>
    <link rel="stylesheet" href="<?php echo URL; ?>public/css/select2.css" />
	
	<script type="text/javascript" src="<?php echo URL; ?>public/js/custom.js"></script>
	<script type="text/javascript" src="<?php echo URL; ?>public/js/select2.min.js"></script>
</head>
<body>   
    <!--DEBUG HELPER. Uncomment to show information about current pages view
    <div class="debug-helper-box">
        DEBUG HELPER: you are in the view: <?php echo $filename; ?>
    </div>-->
    <!-- <img href="<?php echo URL; ?>" src="<?php echo URL; ?>public/images/shoppier.png" alt="Shoppier" />-->
    <div class="navbar navbar-inverse">
        <div class="navbar-inner">
            <div class="container">
                <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                <a class="brand" href="<?php echo URL;?>"><img href="<?php echo URL; ?>" src="<?php echo URL; ?>public/images/shoppier.png" alt="Shoppier" /></a>
                
                   

               
                <div class="nav-collapse collapse">
                    <ul class="nav">
                    
                    <li <?php if ($this->checkForActiveController($filename, "help")) { echo ' class="active" '; } ?> >
                    <a href="<?php echo URL; ?>help/index">Help</a>
                    </li>
                    <li <?php if ($this->checkForActiveController($filename, "overview")) { echo ' class="active" '; } ?> >
                    <a href="<?php echo URL; ?>overview/index">Overview</a>
                    </li> 
                    <li><a href="./contact.html">Contact</a></li>
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
                <li class="dropdown" <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="active" '; } ?> >
                    <!--<a href="#">My Account</a>-->
                    <a  href="#" class="dropdown-toggle" id="drop1" role="button" data-toggle="dropdown">My Account<b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu" role="menu" aria-labelledby="drop1">
                        <!--
                        <li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="active" '; } ?> >
                            <a href="<?php echo URL; ?>login/showprofile">Show my profile</a>
                        </li>
                        -->
                        <li role="presentation" <?php if ($this->checkForActiveController($filename, "login")) { echo '  '; } ?> >
                            <a role="menuitem" data-target="#" href="<?php echo URL; ?>login/showprofile">Show Profile</a>
                        </li>
                        <li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class=" " '; } ?> >
                            <a role="menuitem" data-target="#"  href="<?php echo URL; ?>login/changeaccounttype">Change account type</a>
                        </li>                           
                        <li role="presentation" <?php if ($this->checkForActiveController($filename, "login")) { echo '  '; } ?> >
                            <a role="menuitem" data-target="#"  href="<?php echo URL; ?>login/uploadavatar">Upload an avatar</a>
                        </li>                          
                        <li role="presentation" <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="" '; } ?> >
                            <a role="menuitem" data-target="#"  href="<?php echo URL; ?>login/editusername">Edit my username</a>
                        </li>
                        <li role="presentation" <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="" '; } ?> >
                            <a role="menuitem" data-target="#"  href="<?php echo URL; ?>login/edituseremail">Edit my email</a>
                        </li>
                        <li role="presentation" <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="" '; } ?> >
                            <a role="menuitem" data-target="#"  href="<?php echo URL; ?>login/logout">Logout</a>
                        </li>
                    </ul>
                </li>
                
            <?php endif; ?>          

            <!-- for not logged in users -->
            <?php if (Session::get('user_logged_in') == false):?>
                <li>
                    <a href="#myModal" role="button"  data-toggle="modal">Login</a>
                    <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" 
                        aria-labelledby="myModalLabel" aria-hidden="true">  
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                            <h3 id="myModalLabel">Login</h3>
                        </div>
                        <div class="modal-body">
                          <div class="row">
                            <!--<div class="span7">-->
                            <form action="<?php echo URL; ?>login/login" method="post">
                                <div class="row">
                                <div class="span3 pull-left">
                                    <label style="clear:both;">Username</label>
                                    <input style= "float: left; clear:both;" type="text" name="user_name" required />
                        
                                    <label style="float: none; clear: both;">Password</label>
                                    <input style="float:left; clear:both;" type="password" name="user_password" required />
                                    
                                    <input type="checkbox" name="user_rememberme" style=" min-width: 0; margin: 3px 10px 15px 0; clear:left; float:left" />
                                    <label style="min-width: 0; font-size: 12px; color: #888; clear:right; float: left;">Keep me logged in (for 2 weeks)</label>
                                    <button type="submit" class="btn" style="float: left; clear: both;"> Login</button>
                                </div>
                                <div class="span2 well pull-left">
                                    <p>Not a Member?</p>
                                    <a role="button" href="<?php echo URL; ?>login/register">Register</a>
                                    <br /><br />
                                    <p>Forgot Your Password?</p>
                                    <a role="button" href="<?php echo URL; ?>login/requestpasswordreset">Reset my Password</a>
                                </div>
                                <!--</div>-->
                             </form>
                          </div>    
                        </div>
                    </div>
                </li>
               
                <!--
                <li <?php if ($this->checkForActiveControllerAndAction($filename, "login/register")) { echo ' class="active" '; } ?> >
                    <a href="<?php echo URL; ?>login/register">Register</a>
                </li>         
                <li <?php if ($this->checkForActiveControllerAndAction($filename, "login/requestpasswordreset")) { echo ' class="active" '; } ?> >
                    <a href="<?php echo URL; ?>login/requestpasswordreset">Forgot my Password</a>
                </li>
                -->

            <?php endif; ?>

                </ul>
                 <?php if (Session::get('user_logged_in') == true): ?>
<!--		    <div class="header_right_box brand">
                        
                        <div class="namebox">
                            Hello <?php echo Session::get('user_name'); ?>!
                        </div>
                        
                        <div class="avatar">
                            <?php if (USE_GRAVATARS) { ?>
                                <img src='<?php echo Session::get('user_gravatar_image_url'); ?>' />
                            <?php } else { ?>
                                <img src='<?php echo Session::get('user_avatar_file'); ?>' />
                            <?php } ?>
                        </div>     
-->

                    </div>
                    <?php endif; ?>
                </div>
            </div>
        </div>
    </div>

   

        
        <div class="clear-both"></div>

    </div>	
	