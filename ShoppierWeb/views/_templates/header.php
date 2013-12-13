<!DOCTYPE html>
<html lang="en">
<head>
    <title>Shoppier</title>
    <script type="text/javascript" src="<?php echo URL; ?>public/js/jquery-1.10.1.min.js"></script>
    <script type="text/javascript" src="<?php echo URL; ?>public/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<?php echo URL; ?>public/js/typeahead.min.js"></script>
    <script type="text/javascript" src="<?php echo URL; ?>public/js/hogan-2.0.0.js"></script>
    <script type="text/javascript" src="<?php echo URL; ?>public/js/custom.js"></script>
    <script type="text/javascript" src="<?php echo URL; ?>public/js/select2.min.js"></script>
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBtZjnQXhHLRoOW7edBjdy_i25MAb4W-cQ&sensor=true"></script>
    <link rel="stylesheet" type="text/css" href="<?php echo URL; ?>public/bootstrap/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="<?php echo URL; ?>public/css/typeahead.css" />
    <link rel="stylesheet" type="text/css" href="<?php echo URL; ?>public/css/select2.css" />
    <link rel="stylesheet" type="text/css" href="<?php echo URL; ?>public/css/main.css" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>   
    <nav class="navbar navbar-default" style="background-color:#48c2c5;" role="navigation">
        <!-- Brand information and toggle button (mobile) -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#main-navbar-collapse">
                <span class="sr-only">Toggle Navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<?php echo URL;?>"><img src="<?php echo URL; ?>public/images/shoppier.png" alt="Shoppier" /></a>
        </div>
        
        <!-- All other content used by the toggle -->
        <div class="collapse navbar-collapse" id="main-navbar-collapse">
            <ul class="nav navbar-nav">
                <!--<li <?php if ($this->checkForActiveController($filename, "help")) { echo ' class="active" '; } ?> >
                    <a href="<?php echo URL; ?>help/index">Help</a>
                </li>-->
                <li <?php if ($this->checkForActiveController($filename, "overview")) { echo ' class="active" '; } ?> >
                    <a href="<?php echo URL; ?>overview/index">About Us</a>
                </li>
                
                <!-- Check if user is logged in  -->
                <?php if (Session::get('user_logged_in') == true) {?>
                    <!--<li <?php if ($this->checkForActiveController($filename, "dashboard")) { echo ' class="active" '; } ?> >
                        <a href="<?php echo URL; ?>dashboard/index">Dashboard</a>   
                    </li>-->
                    <li <?php if ($this->checkForActiveController($filename, "slist")) { echo ' class="active" '; } ?> >
                        <a href="<?php echo URL; ?>slist/index">Shopping Lists</a>
                    </li>
                    <li <?php if ($this->checkForActiveController($filename, "additem")) { echo ' class="active" '; } ?> >
                        <a href="<?php echo URL; ?>additem/index">Add Item To Database</a>
                    </li>
                    <li <?php if ($this->checkForActiveController($filename, "locator")) { echo ' class="active" '; } ?> >
                        <a href="<?php echo URL; ?>locator/index">Find an Item</a>
                    </li>

                    <li class="dropdown" <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="active" '; } ?> >
                        <!--<a href="#">My Account</a>-->
                        <a  href="#" class="dropdown-toggle" data-toggle="dropdown">My Account<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <!--
                            <li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="active" '; } ?> >
                                <a href="<?php echo URL; ?>login/showprofile">Show my profile</a>
                            </li>
                            -->
                            <!--<li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="" '; } ?> >
                                <a href="<?php echo URL; ?>login/showprofile">Show Profile</a>
                            </li>-->
                            <!--<li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="" '; } ?> >
                                <a href="<?php echo URL; ?>login/changeaccounttype">Change account type</a>
                            </li>  -->                         
                            <!--<li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="" '; } ?> >
                                <a href="<?php echo URL; ?>login/uploadavatar">Upload an avatar</a>
                            </li> -->                         
                            <!--<li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="" '; } ?> >
                                <a href="<?php echo URL; ?>login/editusername">Edit my username</a>
                            </li>-->
                            <li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="" '; } ?> >
                                <a href="<?php echo URL; ?>login/setnewpassword">Change Password</a>
                            </li>
                            <li <?php if ($this->checkForActiveController($filename, "login")) { echo ' class="" '; } ?> >
                                <a href="<?php echo URL; ?>login/logout">Logout</a>
                            </li>
                        </ul>
                    </li>
                <?php } else { ?>


                    <!-- Modal window for login -->
                    <li>
                        <a href="#myModal" role="button" data-toggle="modal">Login</a>
                    </li>
                    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">  
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                                    <h3 id="myModalLabel">Login</h3>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                        <form action="<?php echo URL; ?>login/login" method="post" role="form">
                                            <div class="col-md-5">
                                                <div class="form-group">
                                                    <label for="user_name">Username</label>
                                                    <input type="text" id="user_name" class="form-control" name="user_name" required />
                                                </div>
                                                <div class="form-group">
                                                    <label for="user_password">Password</label>
                                                    <input type="password" id="user_password" class="form-control" name="user_password" required />
                                                </div>
                                                
                                                <label class="checkbox">
                                                    <input type="checkbox" name="user_rememberme" />Keep me logged in
                                                </label>
                                                
                                                <button type="submit" class="btn">Login</button>
                                            </div>
                                            <br />
                                            <div class="col-md-4 well col-md-offset-2">
                                                <p>Not a Member?</p>
                                                <a role="button" href="<?php echo URL; ?>login/register">Register</a>
                                                <br /><br />
                                                <p>Forgot Your Password?</p>
                                                <a role="button" href="<?php echo URL; ?>login/requestpasswordreset">Reset my Password</a>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div><!-- /.modal-content -->
                        </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->
                <?php } ?>

            </ul>
        </div>

        <div class="clear-both"></div>
    </nav>
    