<div class="content">

    <h1>Register</h1>

    <?php 

    if (isset($this->errors)) {

        foreach ($this->errors as $error) {
            echo '<div class="system_message">'.$error.'</div>';
        }

    }

    ?>

    <!-- register form -->
    <form method="post" role="form" action="<?php echo URL; ?>login/register_action" name="registerform">
	<div class="input-group">
		<label for="login_input_username" class="input-group-addon">Username</label>
        	<input id="login_input_username" class="login_input form-control" type="text" pattern="[a-zA-Z0-9]{2,64}" name="user_name" required />
	</div><span class="help-block">(ONLY letters and numbers)</span>
	
	<div class="input-group">
		<label for="login_input_email" class="input-group-addon">Email</label>

		<input id="login_input_email" class="login_input form-control" type="email" name="user_email" required />
	</div>		<span class="help-block">(valid email required for verification!)</span>
        
	<div class="input-group">
        <label for="login_input_password_new" class="input-group-addon">Password</label>
        <input id="login_input_password_new" class="login_input form-control" type="password" name="user_password_new" pattern=".{6,}" required autocomplete="off" />  
	</div><span class="help-block">(min. 6 characters!)</span>

	<div class="input-group">
        <label for="login_input_password_repeat" class="input-group-addon">Repeat password</label>
        <input id="login_input_password_repeat" class="login_input form-control" type="password" name="user_password_repeat" pattern=".{6,}" required autocomplete="off" />
        </div>
        
	<!--
        <img src="<?php echo URL; ?>login/showCaptcha" />
        <label>Please enter those characters</label>
        <input type="text" name="captcha" required />
        -->
        <br />
               
        <input type="submit" class="btn btn-primary" name="register" value="Register" />
        
    </form>
    
</div>